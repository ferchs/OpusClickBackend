package com.espiritware.opusclick.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
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
import com.espiritware.opusclick.dto.ProviderUpdateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class ProviderController {
	
	public static final String PROVIDERS_UPLOADED_FOLDER="Images/Providers/";
	
	@Autowired
	private ProviderService providerService;
		
	@Autowired
	private AmazonClient amazonClient;
	
	@RequestMapping(value = "/providers/{id:.+}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateProvider(@DTO(ProviderUpdateDto.class) Provider provider,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if(provider!=null) {
			providerService.updateProvider(provider);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
//	@RequestMapping(value = "/providers", method = RequestMethod.GET, headers = "Accept=application/json")
//	@Transactional
//	public ResponseEntity<List<Provider>> getProviders(@RequestParam(value = "id", required = false) String providerId,
//			@RequestParam(value = "profession", required = false) String professionName,
//			UriComponentsBuilder uriComponentsBuilder) {
//		List<Provider> providers = new ArrayList<Provider>();
//		if (professionName != null) {
//			providers = providerService.findProvidersByProfessionName(professionName);
//			Collections.sort(providers);
//			if (providers.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			} else {
//				return new ResponseEntity<List<Provider>>(providers, HttpStatus.OK);
//			}
//		} else {
//			providers = providerService.findAllProviders();
//			if (providers.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			} else {
//				return new ResponseEntity<List<Provider>>(providers, HttpStatus.OK);
//			}
//		}
//	}
	
	
	@RequestMapping(value = "/providers/{id:.+}", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderById(@PathVariable("id") String providerId, Principal principal,
			UriComponentsBuilder uriComponentsBuilder) {
		Provider provider = providerService.findProviderById(providerId);
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id " + providerId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		//Medida temporal para evitar que un proveedor autenticado pueda averiguar proveedor de otro usuario
		if(!principal.getName().equals(providerId)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Provider>(provider, HttpStatus.OK);
	}
	
	@RequestMapping(value="/providers/images", method = RequestMethod.POST, headers ="content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadProviderImage(@RequestParam("id") String idProvider,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (idProvider == null || idProvider.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		Provider provider = providerService.findProviderById(idProvider);
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id: " + idProvider + " not found"),
					HttpStatus.NOT_FOUND);
		}
		// || !provider.getPhoto().isEmpty()
		if (provider.getPhoto() != null) {
			if (!provider.getPhoto().isEmpty()) {
				try {
					amazonClient.deleteFileFromS3Bucket(provider.getPhoto());
				} catch (Exception e) {
					return new ResponseEntity<>(
							new CustomErrorType("Provider with id: " + idProvider + " can't be erased"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		try {
			String fileUrl = amazonClient.uploadFile(multipartFile);
			provider.setPhoto(fileUrl);
			providerService.updateProvider(provider);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image provider with id: " + idProvider + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/providers/{id_provider:.+}/images", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderImage(@PathVariable("id_provider") String idProvider){
		if (idProvider == null) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		Provider provider = providerService.findProviderById(idProvider);
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id: " + idProvider + " not found"),
					HttpStatus.NOT_FOUND);
		}
		if (provider.getPhoto() != null) {
			if (!provider.getPhoto().isEmpty()) {
				return new ResponseEntity<String>(provider.getPhoto(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
