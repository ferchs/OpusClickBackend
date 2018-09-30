package com.espiritware.opusclick.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ContractDto;
import com.espiritware.opusclick.dto.ContractGetDto;
import com.espiritware.opusclick.dto.ContractUpdateDto;
import com.espiritware.opusclick.dto.ContractUpdateMilestonesStateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.Contract;
import com.espiritware.opusclick.model.Milestone;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ContractService;
import com.espiritware.opusclick.service.ItemService;
import com.espiritware.opusclick.service.WorkService;

@Controller
@RequestMapping("/v1")
public class ContractController {

	private static final String CONTRACT_IMAGES_FOLDER="contract-images/";
	
	private static final String TMP_FOLDER="tmp/";
	
	@Autowired
	private WorkService workService;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Publisher publisher;
	
	@Autowired
	private AmazonClient amazonClient;
	
	
	
	@RequestMapping(value = "/contracts/{id}", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderById(@PathVariable("id") String contractId, Principal principal,
			UriComponentsBuilder uriComponentsBuilder) {
		Contract contract = contractService.findContractById(Integer.parseInt(contractId));
		if (contract == null) {
			return new ResponseEntity<>(new CustomErrorType("Contract with id " + contractId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		
		ContractGetDto dto=modelMapper.map(contract, ContractGetDto.class);
		return new ResponseEntity<ContractGetDto>(dto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/contracts", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createContract(@Valid @DTO(ContractDto.class) Contract contract,
			@RequestParam(value = "work", required = true) String workId, UriComponentsBuilder uriComponentsBuilder,
			final HttpServletRequest request) {
		
		if (contract != null) {
			try {
				contract.setMilestones(updateItems(contract.getMilestones()));
			} catch (AmazonClientException | IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Work work = workService.findWorkById(Integer.parseInt(workId));
			work.setState(contract.getState());
			work.setHistoryStateChanges(work.getHistoryStateChanges()+contract.getState().state() + ",");
			UpdateVisitsState(work);
			workService.updateWork(work);
			contract.setWork(work);
			work.setContract(contract);
			contract.setHistoryStateChanges(contract.getState().state() + ",");
			contractService.createContract(contract);
			sendNotificationEmail(contract);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	private void UpdateVisitsState(Work work) {
		for (Visit visit : work.getVisits()) {
			visit.setState(State.VISIT_MADE);
			visit.setHistoryStateChanges(visit.getHistoryStateChanges()+visit.getState().state() + ",");
		}
	}
	
	
	@RequestMapping(value = "/contracts", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateContract(@DTO(ContractUpdateDto.class) Contract contract,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if (contract != null) {
			if (contract.getState().equals(State.IN_PROGRESS)) {
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.setStartDate(calculateStartContractDate(contract.getCreationDate()));
				contract.setEndDate(calculateContractEnd(contract.getStartDate(), contract.getMilestones()));
				contract.setMilestones(updateMilestones(contract.getCreationDate(),contract.getMilestones()));
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(contract.getState());
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
				workService.updateWork(contract.getWork());
				sendNotificationEmail(contract);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setContract(contract);
				contract.getWork().setState(contract.getState());
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
				workService.updateWork(contract.getWork());
				try {
					contract.setMilestones(updateItems(contract.getMilestones()));
				} catch (AmazonClientException | IOException e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				sendNotificationEmail(contract);
				return new ResponseEntity<>(HttpStatus.OK);

			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	private Set<Milestone> updateItems(Set<Milestone> milestones) throws AmazonServiceException, AmazonClientException, IOException {
		for (Milestone milestone : milestones) {
			if(milestone.getItem().getImageContract()!=null) {
				if(milestone.getItem().getImageContract().contains("data:image")) {
					String url = amazonClient.uploadFile(CONTRACT_IMAGES_FOLDER, createTmpFile(milestone.getItem().getImageContract()));
					milestone.getItem().setImageContract(url);
				}
			}
			itemService.updateItem(milestone.getItem());
		}
		return milestones;
	}
	
	private File createTmpFile(String base64) throws IOException {
		String base64Image = base64.split(",")[1];
		String metadata= base64.split(",")[0];
		String extension = "."+metadata.substring(metadata.indexOf("/")+1, metadata.indexOf(";"));
		byte[] decodedFile = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
		File file = File.createTempFile("tmp",extension, new File(TMP_FOLDER));
		file.deleteOnExit();
		Files.write(file.toPath(), decodedFile);
		return file;
	}
	
	
	private void sendNotificationEmail(Contract contract) {
		if(contract.getState().equals(State.IN_PROGRESS)) {
			publisher.publishUserMakesPaymentEvent(contract);
		}else if(contract.getState().equals(State.CONTRACT_MODIFIED_BY_USER)) {
			publisher.publishUserModifiesContractEvent(contract);
		}else if(contract.getState().equals(State.CONTRACT_ACCEPTED_BY_PROVIDER)) {
			publisher.publishProviderAcceptContractEvent(contract);
		} else if(contract.getState().equals(State.CONTRACT_MODIFIED_BY_PROVIDER)) {
			publisher.publishProviderModifiesContractEvent(contract);
		}
	}
	
	@RequestMapping(value = "/contracts/{id}/milestones", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateContractMilestones(@PathVariable("id") String contractId,
			//Se usa para saber si un usuario aprueba un pago, o es el proveedor que solicita un pago
			@RequestParam(value = "operation", required = false) String operation,
			@DTO(ContractUpdateMilestonesStateDto.class) Contract contract,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		
		if(contract.getMilestones()!=null && contract!=null) {
			updateWorkState(contract);
			workService.updateWork(contract.getWork());
			sendNotificationEmailOperation(contract,operation);
			sendNotificationEmail(contract);
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	private void sendNotificationEmailOperation(Contract contract, String operation) {
		if(operation.equalsIgnoreCase("requestPayment")) {
			publisher.publishProviderRequestPaymentEvent(contract);
		}else if(operation.equalsIgnoreCase("authorizePayment")) {
			publisher.publishUserAuthorizesPaymentEvent(contract);
		}else if(operation.equalsIgnoreCase("denyPayment")) {
			publisher.publishUserDenyPaymentEvent(contract);
		}
	}
	
	private Contract updateWorkState(Contract contract) {
		boolean allSameState = true;
		State referenceState = null;
		int i = 0;
		for (Milestone milestone : contract.getMilestones()) {
			if (i == 0) {
				referenceState = milestone.getState();
			}
			if (!referenceState.equals(milestone.getState())) {
				allSameState = false;
			}
			if (milestone.getState().equals(State.REPROBATE)) {
				contract.setState(State.REPROBATE);
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(State.REPROBATE);
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
				return contract;
			}
			if (milestone.getState().equals(State.UNFULFILLED)) {
				contract.setState(State.UNFULFILLED);
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(State.UNFULFILLED);
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
				return contract;
			}
			if (milestone.getState().equals(State.FINALIZED)) {
				contract.setState(State.PARTIALLY_FINISHED);
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(State.PARTIALLY_FINISHED);
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
				return contract;
			}
			i++;
		}
		if (allSameState) {
			if (referenceState.equals(State.PAID_OUT)) {
				contract.setState(State.PAID_OUT);
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(State.PAID_OUT);
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
			} else {
				contract.setState(referenceState);
				contract.setHistoryStateChanges(contract.getHistoryStateChanges() + contract.getState().state() + ",");
				contract.getWork().setState(referenceState);
				contract.getWork().setHistoryStateChanges(contract.getWork().getHistoryStateChanges() + contract.getState().state() + ",");
			}

		}
		return contract;
	}
	
//	@RequestMapping(value = "/contracts/{id}/states", method = RequestMethod.PUT, headers = "Accept=application/json")
//	@ResponseBody
//	@Transactional
//	public ResponseEntity<?> updateState(@DTO(ContractUpdateStateDto.class) Contract contract,
//			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
//		if(user!=null) {
//			userService.updateUser(user);
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//		
//	}
	
//	if (work != null) {
//		if (payment == true) {
//			contract.setWork(work);
//			work.setContract(contract);
//			contract.setStartDate(calculateStartContractDate(contract.getCreationDate()));
//			contract.setEndDate(calculateContractEnd(contract.getStartDate(), contract.getMilestones()));
//			contract.setHistoryStateChanges(contract.getState().state()+",");
//			contract.setMilestones(calculateMilestonesDates(contract.getCreationDate(),contract.getMilestones()));
//			contractService.createContract(contract);
//			try {
//				uploadContractImages(contract.getMilestones());
//			} catch (AmazonClientException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			return new ResponseEntity<>(HttpStatus.OK);
//		} else {
//			contract.setWork(work);
//			work.setContract(contract);
//			contract.setHistoryStateChanges(contract.getState().state()+",");
//			try {
//				contract.setMilestones(uploadContractImages(contract.getMilestones()));
//			} catch (AmazonClientException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			contractService.createContract(contract);
//			return new ResponseEntity<>(HttpStatus.OK);
//		}
//	} else {
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
	
	private Date calculateStartContractDate(Date paymentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paymentDate);
		calendar.add(Calendar.DATE, 1);
		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
			calendar.add(Calendar.DATE, 1);
		}
		return calendar.getTime();
	}
	
	private Set<Milestone> updateMilestones(Date start, Set<Milestone> milestones) {
		Set<Milestone> calculatedMilestones = new HashSet<Milestone>();
		Date previousLastDate = null;
		int i = 0;
		for (Milestone milestone : milestones) {
			if (i != 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(previousLastDate);
				int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
					calendar.add(Calendar.HOUR, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
					calendar.add(Calendar.DATE, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
					calendar.add(Calendar.MONTH, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
					calendar.add(Calendar.YEAR, roundedValue);
				}
				milestone.setStartDate(previousLastDate);
				milestone.setHistoryStateChanges(milestone.getState().state() + State.IN_PROGRESS.state() + "," );
				milestone.setState(State.IN_PROGRESS);
				milestone.setEndDate(calendar.getTime());
				calculatedMilestones.add(milestone);
				previousLastDate = calendar.getTime();
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(start);
				int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
					calendar.add(Calendar.HOUR, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
					calendar.add(Calendar.DATE, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
					calendar.add(Calendar.MONTH, roundedValue);
				}
				if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
					calendar.add(Calendar.YEAR, roundedValue);
				}
				milestone.setHistoryStateChanges(milestone.getState().state() + State.IN_PROGRESS.state() + "," );
				milestone.setState(State.IN_PROGRESS);
				milestone.setStartDate(start);
				milestone.setEndDate(calendar.getTime());
				calculatedMilestones.add(milestone);
				previousLastDate = calendar.getTime();
			}
		}
		return calculatedMilestones;
	}
	
	private Date calculateContractEnd(Date start, Set<Milestone> milestones) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		for (Milestone milestone : milestones) {
			int roundedValue = (int) Math.round(milestone.getItem().getDurationValue());
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Hora(s)")) {
				calendar.add(Calendar.HOUR, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Día(s)")) {
				calendar.add(Calendar.DATE, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Mes(es)")) {
				calendar.add(Calendar.MONTH, roundedValue);
			}
			if (milestone.getItem().getDurationTime().equalsIgnoreCase("Año(s)")) {
				calendar.add(Calendar.YEAR, roundedValue);
			}
		}
		return calendar.getTime();
	}


//	@RequestMapping(value = "/provider_quotes/images", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
//	@Transactional
//	public ResponseEntity<?> uploadQuoteImage(@RequestParam("quote") String quotationId,
//			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
//		if (quotationId == null || quotationId.isEmpty()) {
//			return new ResponseEntity<>(new CustomErrorType("Please set id_work"), HttpStatus.NO_CONTENT);
//		}
//		if (multipartFile.isEmpty()) {
//			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
//		}
//
//		ProviderQuote quote = providerQuoteService.findQuoteById(Integer.parseInt(quotationId));
//		if (quote == null) {
//			return new ResponseEntity<>(new CustomErrorType("Quotation with id: " + quotationId + " not found"),
//					HttpStatus.NOT_FOUND);
//		}
//		try {
//			String fileUrl = amazonClient.uploadFile(PROVIDER_QUOTE_IMAGES_FOLDER, multipartFile);
//			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(
//					new CustomErrorType("Image provider with id: " + quotationId + " can't be upload"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@RequestMapping(value = "/provider_quotes/{id}", method = RequestMethod.GET)
//	@Transactional
//	public ResponseEntity<?> getProviderQuoteById(@PathVariable("id") String quoteId,
//			UriComponentsBuilder uriComponentsBuilder) {
//
//		ProviderQuoteGetDto dto = modelMapper.map(providerQuoteService.findQuoteById(Integer.parseInt(quoteId)),
//				ProviderQuoteGetDto.class);
//		return new ResponseEntity<ProviderQuoteGetDto>(dto, HttpStatus.OK);
//	}
}
