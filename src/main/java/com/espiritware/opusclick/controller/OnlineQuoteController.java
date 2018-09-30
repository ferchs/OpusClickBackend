package com.espiritware.opusclick.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.OnlineQuoteDto;
import com.espiritware.opusclick.dto.OnlineQuoteGetDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.OnlineQuoteService;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.service.WorkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
//@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class OnlineQuoteController {
	
	private static final String ONLINE_QUOTATION_IMAGES_FOLDER="online-quotation-images/";
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private AmazonClient amazonClient;
	
	@Autowired
	private Publisher publisher;
	
	@Autowired
	private OnlineQuoteService onlineQuoteService;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@RequestMapping(value = "/online_quotes", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createOnlineQuote(@Valid @DTO(OnlineQuoteDto.class) OnlineQuote onlineQuote,
			@RequestParam(value = "user", required = false) String userId,
			@RequestParam(value = "provider", required = false) String providerId,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		
		if (userId != null && providerId != null) {
			Work work = new Work();// Cual es el problema si yo mismo decido instanciar el objeto Work, y no uso el
									// @Autowired para este objetivo.
			work.setWorkNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
			work.setProviderLabel(work.getWorkNumber());
			work.setUserLabel(work.getWorkNumber());
			work.setCreationDate(new Date());
			work.setState(State.PENDING_BY_QUOTATION);
			work.setHistoryStateChanges("PENDING_BY_QUOTATION, ");
			work.setUser(userService.findUserById(Integer.parseInt(userId)));
			work.setProvider(providerService.findProviderById(Integer.parseInt(providerId)));
			work.setOnlineQuote(onlineQuote);
			onlineQuote.setWork(work);
			workService.createWork(work);
			publisher.publishUserQuotationEvent(onlineQuote);
			return new ResponseEntity<>("quoteId:" + onlineQuote.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<>("Los request params user and provider son necesarios", HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value = "/online_quotes/images", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadQuoteImage(@RequestParam("quote") String quotationId,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (quotationId == null || quotationId.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_work"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}

		OnlineQuote quote = onlineQuoteService.findQuoteById(Integer.parseInt(quotationId));
		if (quote == null) {
			return new ResponseEntity<>(new CustomErrorType("Quotation with id: " + quotationId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		try {
			String fileUrl = amazonClient.uploadFile(ONLINE_QUOTATION_IMAGES_FOLDER, multipartFile);
			ObjectNode node = (ObjectNode) new ObjectMapper().readTree(quote.getRequirements());
			node.put("Imagen",fileUrl);			
			quote.setRequirements(node.toString());
			onlineQuoteService.updateQuote(quote);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image provider with id: " + quotationId + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/online_quotes/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public  ResponseEntity<?> getOnlineQuote(@PathVariable("id") String quoteId,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		
		OnlineQuote onlineQuote=onlineQuoteService.findQuoteById(Integer.parseInt(quoteId));

		if (onlineQuote == null) {
			return new ResponseEntity<>(new CustomErrorType("OnlineQuote with id " + quoteId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		
		OnlineQuoteGetDto dto=modelMapper.map(onlineQuote, OnlineQuoteGetDto.class);
		return new ResponseEntity<OnlineQuoteGetDto>(dto, HttpStatus.OK);
				
	}
	
}
