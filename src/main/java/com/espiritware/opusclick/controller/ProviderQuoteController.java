package com.espiritware.opusclick.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ProviderQuoteDto;
import com.espiritware.opusclick.dto.ProviderQuoteGetDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.Item;
import com.espiritware.opusclick.model.ProviderQuote;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderQuoteService;
import com.espiritware.opusclick.service.WorkService;

@Controller
@RequestMapping("/v1")
public class ProviderQuoteController {
	
	private static final String PROVIDER_QUOTE_IMAGES_FOLDER="provider-quotation-images/";
	
	private static final String TMP_FOLDER="tmp/";
	
	@Autowired
	private WorkService workService;

	@Autowired
	private ProviderQuoteService providerQuoteService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Publisher publisher;
	
	@Autowired
	private AmazonClient amazonClient;
	
	@RequestMapping(value = "/provider_quotes", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createProviderQuote(@Valid @DTO(ProviderQuoteDto.class) ProviderQuote providerQuote,
			@RequestParam(value = "work", required = true) String workId, UriComponentsBuilder uriComponentsBuilder,
			final HttpServletRequest request) {

		Work work = workService.findWorkById(Integer.parseInt(workId));
		String previousStates = work.getHistoryStateChanges();
		try {
			providerQuote.setItems(uploadImages(providerQuote.getItems()));
		} catch (AmazonClientException | IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Image can't be upload", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		providerQuote.setWork(work);
		work.setProviderQuote(providerQuote);
		providerQuoteService.createProviderQuote(providerQuote);
		work.setState(State.QUOTE_MADE);
		previousStates = previousStates + "QUOTE_MADE,";
		work.setHistoryStateChanges(previousStates);
		workService.updateWork(work);
		publisher.publishProviderQuoteMadeEvent(providerQuote);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private Set<Item> uploadImages(Set<Item> items) throws AmazonServiceException, AmazonClientException, IOException {
		for (Item item : items) {
			if (item.getImageQuote() != null) {
				if (!item.getImageQuote().isEmpty()) {
					String url = amazonClient.uploadFile(PROVIDER_QUOTE_IMAGES_FOLDER,
							createTmpFile(item.getImageQuote()));
					item.setImageQuote(url);
				}
			}
		}
		return items;
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
	
	@RequestMapping(value = "/provider_quotes/images", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadQuoteImage(@RequestParam("quote") String quotationId,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (quotationId == null || quotationId.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_work"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}

		ProviderQuote quote = providerQuoteService.findQuoteById(Integer.parseInt(quotationId));
		if (quote == null) {
			return new ResponseEntity<>(new CustomErrorType("Quotation with id: " + quotationId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		try {
			String fileUrl = amazonClient.uploadFile(PROVIDER_QUOTE_IMAGES_FOLDER, multipartFile);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image provider with id: " + quotationId + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/provider_quotes/{id}", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderQuoteById(@PathVariable("id") String quoteId,
			UriComponentsBuilder uriComponentsBuilder) {

		ProviderQuoteGetDto dto = modelMapper.map(providerQuoteService.findQuoteById(Integer.parseInt(quoteId)),
				ProviderQuoteGetDto.class);
		return new ResponseEntity<ProviderQuoteGetDto>(dto, HttpStatus.OK);
	}
}
