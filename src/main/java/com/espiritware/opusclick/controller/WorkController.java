package com.espiritware.opusclick.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.WorkGetDto;
import com.espiritware.opusclick.dto.WorkUpdateDto;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.Contract;
import com.espiritware.opusclick.model.Milestone;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.WorkService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class WorkController {

	@Autowired
	private WorkService workService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Publisher publisher;
	
	
	@RequestMapping(value = "/works", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> getWorks(@RequestParam(value = "rol", required = true) String rolName,
			@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "state", required = false) String state, UriComponentsBuilder uriComponentsBuilder) {
	
		if (rolName.equalsIgnoreCase("user")) {
			if (state != null) {
				String [] statesName=state.split("\\,");
				if(statesName.length>1) {
					List<WorkGetDto> workDtos =new ArrayList<WorkGetDto>();
					workDtos =generateWorkDtos(workService.findWorksOfUserByStates(id,statesName));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}else {
					List<WorkGetDto> workDtos = generateWorkDtos(workService.findWorksOfUserByState(id, State.valueOf(state)));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}
			} else {
				List<WorkGetDto> worksDto = generateWorkDtos(workService.findAllWorksOfUser(id));
				return new ResponseEntity<>(worksDto, HttpStatus.OK);
			}
		} else if (rolName.equalsIgnoreCase("provider")) {
			if (state != null) {
				String [] statesName=state.split("\\,");
				if(statesName.length>1) {
					List<WorkGetDto> workDtos =new ArrayList<WorkGetDto>();
					workDtos =generateWorkDtos(workService.findWorksOfProviderByStates(id,statesName));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}else {
					List<WorkGetDto> workDtos = generateWorkDtos(workService.findWorksOfProviderByState(id, State.valueOf(state)));
					return new ResponseEntity<>(workDtos, HttpStatus.OK);
				}
			} else {
				List<WorkGetDto> worksDto = generateWorkDtos(workService.findAllWorksOfProvider(id));
				return new ResponseEntity<>(worksDto, HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>("El parametro rol debe ser 'user' o 'provider'", HttpStatus.BAD_REQUEST);
		}
	}
	
	private List<WorkGetDto> generateWorkDtos(List<Work> works){
		List<WorkGetDto> worksDto = new ArrayList<WorkGetDto>();
		for (Work work : works) {
			WorkGetDto dto = modelMapper.map(work, WorkGetDto.class);
			worksDto.add(dto);
		}
		return worksDto;
	}

	private State[] convertStringArrayToStateArray(String[] elements) {
		State[] states= new State[elements.length];
		for (int i = 0; i < states.length; i++) {
			states[i]=State.valueOf(elements[i]);
		}
		return states;
	}
	
	@RequestMapping(value = "/works", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateWork(@DTO(WorkUpdateDto.class) Work work, UriComponentsBuilder uriComponentsBuilder,
			final HttpServletRequest request) {

		if (work.getState().equals(State.LABEL_CHANGE)) {
			State previousState = workService.findWorkById(work.getId()).getState();
			work.setState(previousState);
			workService.updateWork(work);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			/*NO_AGREEMENT_BY_USER*/
			String previousStateChanges = workService.findWorkById(work.getId()).getHistoryStateChanges();
			
			if (work.getState().equals(State.IN_PROGRESS)) {
				work.setHistoryStateChanges(work.getContract().getWork().getHistoryStateChanges() + work.getState().state() + ",");
				work.getContract().setState(work.getState());
				work.getContract().setHistoryStateChanges(work.getContract().getHistoryStateChanges() +work.getState().state() + ",");
				work.getContract().setStartDate(calculateStartContractDate(work.getContract().getCreationDate()));
				work.getContract().setEndDate(calculateContractEnd(work.getContract().getStartDate(), work.getContract().getMilestones()));
				work.getContract().setMilestones(updateMilestones(work.getContract().getCreationDate(),work.getContract().getMilestones()));
				work.getContract().setHistoryStateChanges(work.getContract().getHistoryStateChanges() + work.getContract().getState().state() + ",");
				work.getContract().getWork().setState(work.getContract().getState());
				work.getContract().getWork().setHistoryStateChanges(work.getContract().getWork().getHistoryStateChanges() + work.getContract().getState().state() + ",");
				workService.updateWork(work.getContract().getWork());
				publisher.publishUserMakesPaymentEvent(work.getContract());
				return new ResponseEntity<>(HttpStatus.OK);
			} else if (work.getState().equals(State.CANCELLED_BY_USER)) {
				if (previousStateChanges != null) {
					work.setHistoryStateChanges(previousStateChanges + "CANCELLED_BY_USER,");
				} else {
					work.setHistoryStateChanges("CANCELLED_BY_USER,");
				}
				work = finalizeWork(work, State.CANCELLED_BY_USER);
				workService.updateWork(work);
				publisher.publishUserCancelWorkEvent(work);
				return new ResponseEntity<>(HttpStatus.OK);
			} else if (work.getState().equals(State.CANCELLED_BY_PROVIDER)) {
				if (previousStateChanges != null) {
					work.setHistoryStateChanges(previousStateChanges + "CANCELLED_BY_PROVIDER,");
				} else {
					work.setHistoryStateChanges("CANCELLED_BY_PROVIDER,");
				}
				work = finalizeWork(work, State.CANCELLED_BY_PROVIDER);
				workService.updateWork(work);
				publisher.publishProviderCancelWorkEvent(work);
				return new ResponseEntity<>(HttpStatus.OK);
			} else if (work.getState().equals(State.PENDING_BY_VISIT)) {
				workService.updateWork(work);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	
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
	
	
//	 else if (work.getState().equals(State.REJECTED_BY_USER)) {
//			if (previousStateChanges != null) {
//				work.setHistoryStateChanges(previousStateChanges + "REJECTED_BY_USER,");
//			} else {
//				work.setHistoryStateChanges("REJECTED_BY_USER,");
//			}
//			work = finalizeWork(work, State.REJECTED_BY_USER);
//			workService.updateWork(work);
//			publisher.publishUserWorkRejectedEventEvent(work);
//			return new ResponseEntity<>(HttpStatus.OK);
//		} else if (work.getState().equals(State.REJECTED_BY_PROVIDER)) {
//			if (previousStateChanges != null) {
//				work.setHistoryStateChanges(previousStateChanges + "REJECTED_BY_PROVIDER,");
//			} else {
//				work.setHistoryStateChanges("REJECTED_BY_PROVIDER,");
//			}
//			work = finalizeWork(work, State.REJECTED_BY_PROVIDER);
//			workService.updateWork(work);
//			publisher.publishProviderWorkRejectedEventEvent(work);
//			return new ResponseEntity<>(HttpStatus.OK);
//		}

	/*Aqui pasa algo particular con el SET simplemente recorro, hago modificaciones y vuelvo a retornar
	 * Analizar y no borrar */
	/*aqui se evidencia lo que es crear un apuntador a un objeto NO NECESITO CREAR UNA LISTA ADICIONAL*/
	private Work finalizeWork(Work work, State state) {
		
		if(work.getVisits()!=null) {
			Set<Visit> visits=work.getVisits();
			for(Visit visit:visits) {
				visit.setState(state);
				visit.setHistoryStateChanges(visit.getHistoryStateChanges()+","+state.toString());
			}
		}
		if(work.getOnlineQuote()!=null) {
			work.getOnlineQuote().setState(state);
		}
		if(work.getContract()!=null) {
			work.getContract().setState(state);
			work.setHistoryStateChanges(work.getContract().getHistoryStateChanges()+","+state.toString());
		}
		
		return work;
	}
	
}
