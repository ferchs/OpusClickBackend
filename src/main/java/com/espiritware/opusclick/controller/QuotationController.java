package com.espiritware.opusclick.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.OnlineQuoteDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.QuotationService;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.service.WorkService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class QuotationController {
	
	private static final String QUOTATION_IMAGES_FOLDER="quotation-images/";
	
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
	private QuotationService quotationService;

	
	@RequestMapping(value = "/quotes", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createOnlineQuote(@Valid @DTO(OnlineQuoteDto.class) OnlineQuote onlineQuote,
			@RequestParam(value = "user", required = false) String userId,
			@RequestParam(value = "provider", required = false) String providerId,
			@RequestParam(value = "work", required = false) String workId,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
	
		if (workId != null) {
			Work work = workService.findWorkById(Integer.parseInt(workId));
			if (work == null) {
				return new ResponseEntity<>("No se encontraron Works con el Id suministrado", HttpStatus.NOT_FOUND);
			} else {
				if (work.getQuotes() == null) {
					Set<OnlineQuote> quotes = new HashSet<OnlineQuote>();
					quotes.add(onlineQuote);
					onlineQuote.setWork(work);
					work.setQuotes(quotes);
					workService.updateWork(work);
					publisher.publishUserQuotationEvent(onlineQuote);
					return new ResponseEntity<>("quoteId: "+onlineQuote.getId(),HttpStatus.OK);
				} else {
					work.getQuotes().add(onlineQuote);
					onlineQuote.setWork(work);
					workService.updateWork(work);
					publisher.publishUserQuotationEvent(onlineQuote);
					return new ResponseEntity<>("quoteId: "+onlineQuote.getId(),HttpStatus.OK);
				}
			}
		} else {
			if (userId != null && providerId != null) {
				Work work = new Work();// Cual es el problema si yo mismo decido instanciar el objeto Work, y no uso el
										// @Autowired para este objetivo.
				work.setWorkNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
				work.setCreationDate(new Date());
				work.setState(State.PENDING_BY_QUOTATION);
				work.setHistoryStateChanges("PENDING_BY_QUOTATION, ");
				work.setUser(userService.findUserById(Integer.parseInt(userId)));
				work.setProvider(providerService.findProviderById(Integer.parseInt(providerId)));
				Set<OnlineQuote> quotes = new HashSet<OnlineQuote>();
				onlineQuote.setWork(work);
				// visit.setHistoryStateChanges("PENDING_BY_PROVIDER_ACCEPT, ");
				quotes.add(onlineQuote);
				work.setQuotes(quotes);
				workService.createWork(work);
				publisher.publishUserQuotationEvent(onlineQuote);
				return new ResponseEntity<>("quoteId:"+onlineQuote.getId(),HttpStatus.OK);
			}
			return new ResponseEntity<>("Los request params user and provider son necesarios", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/quotes/images", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadQuoteImage(@RequestParam("quote") String quotationId,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (quotationId == null || quotationId.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_work"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}

		OnlineQuote quote = quotationService.findQuoteById(Integer.parseInt(quotationId));
		if (quote == null) {
			return new ResponseEntity<>(new CustomErrorType("Quotation with id: " + quotationId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		try {
			String fileUrl = amazonClient.uploadFile(QUOTATION_IMAGES_FOLDER, multipartFile);
			ObjectNode node = (ObjectNode) new ObjectMapper().readTree(quote.getRequirements().asText());
			node.put("image",fileUrl);
			//perform operations on node
			JsonNode jsonNode = (JsonNode) new ObjectMapper().readTree(node.toString());
			quote.setRequirements(jsonNode);
			quotationService.updateQuote(quote);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image provider with id: " + quotationId + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
