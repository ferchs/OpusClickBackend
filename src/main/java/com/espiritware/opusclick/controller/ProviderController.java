package com.espiritware.opusclick.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ProviderGetByProfessionDto;
import com.espiritware.opusclick.dto.ProviderGetContractReviewDto;
import com.espiritware.opusclick.dto.ProviderGetProfileDto;
import com.espiritware.opusclick.dto.ProviderUpdateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.util.ReverseSortProviderByReputation;
import com.espiritware.opusclick.util.ReverseSortProviderReviewByDate;

@Controller
//@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class ProviderController {
	
	private static final String PROVIDER_IMAGES_FOLDER="provider-profile-images/";
	
	private static final String PROVIDER_DEFAULT_IMAGE="https://s3.amazonaws.com/opusclick.com/provider-profile-images/default-profile-photo.png";

	
	@Autowired
	private ProviderService providerService;
		
	@Autowired
	private AmazonClient amazonClient;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "/providers", method = RequestMethod.PUT, headers = "Accept=application/json")
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
	
	@RequestMapping(value = "/providers", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<List<?>> getProviders(@RequestParam(value = "profession", required = false) String professionName,
			UriComponentsBuilder uriComponentsBuilder) {
		List<Provider> providers = new ArrayList<Provider>();
		if (professionName != null) {
			providers = providerService.findProvidersByProfessionId(Integer.parseInt(professionName));
			if (providers.isEmpty()) {
				return new ResponseEntity<List<?>>(providers, HttpStatus.OK);
			} else {
				providers.sort(new ReverseSortProviderByReputation());
				List<ProviderGetByProfessionDto> dtoList=getProviderDtoList(providers);
				return new ResponseEntity<List<?>>(dtoList, HttpStatus.OK);
			}
		} else {
			providers = providerService.findAllProviders();
			if (providers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				providers.sort(new ReverseSortProviderByReputation());
				return new ResponseEntity<List<?>>(providers, HttpStatus.OK);
			}
		}
	}
	
	private List<ProviderGetByProfessionDto> getProviderDtoList(List<Provider> providers) {
		List<ProviderGetByProfessionDto> dtoList= new ArrayList<ProviderGetByProfessionDto>();
		for(Provider provider:providers) {
			ProviderGetByProfessionDto dto=modelMapper.map(provider, ProviderGetByProfessionDto.class);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	@RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderById(@PathVariable("id") String providerId, Principal principal,
			UriComponentsBuilder uriComponentsBuilder) {
		Provider provider = providerService.findProviderById(Integer.parseInt(providerId));
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id " + providerId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		
		ProviderGetProfileDto dto=modelMapper.map(provider, ProviderGetProfileDto.class);
		//Medida temporal para evitar que un proveedor autenticado pueda averiguar proveedor de otro usuario
//		if(!principal.getName().equals(provider.getAccount().getEmail())) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		}		
		return new ResponseEntity<ProviderGetProfileDto>(dto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/providers/{id}/images", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderImage(@PathVariable("id") String providerId){
		if (providerId == null) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		Provider provider = providerService.findProviderById(Integer.parseInt(providerId));
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id: " + providerId + " not found"),
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
	
	
	@RequestMapping(value = "/providers/{id}/reviews", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<List<?>> getProviderWorks(@PathVariable("id") String providerId, Principal principal,
			UriComponentsBuilder uriComponentsBuilder) {
		Provider provider = providerService.findProviderById(Integer.parseInt(providerId));
		if (provider == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
		List<ProviderGetContractReviewDto> dtoList=getProviderReviews(provider);
		return new ResponseEntity<List<?>>(dtoList, HttpStatus.OK);
	}
	
	private List<ProviderGetContractReviewDto> getProviderReviews(Provider provider) {
		List<ProviderGetContractReviewDto> dtoList= new ArrayList<ProviderGetContractReviewDto>();
		for(Work work:provider.getWorks()) {
			if(work.getContract()!=null) {
				if(work.getContract().getState().equals(State.QUALIFIED)) {
					ProviderGetContractReviewDto dto=modelMapper.map(work.getContract(), ProviderGetContractReviewDto.class);
					dtoList.add(dto);
				}
			}
		}
		dtoList.sort(new ReverseSortProviderReviewByDate());
		return dtoList;
	}
	
//	@RequestMapping(value = "/providers/{email:.+}", method = RequestMethod.GET)
//	@Transactional
//	public ResponseEntity<?> getProviderById(@PathVariable("email") String providerEmail, Principal principal,
//			UriComponentsBuilder uriComponentsBuilder) {
//		Provider provider = accountService.findAccountByEmail(providerEmail).getProvider();
//		if (provider == null) {
//			return new ResponseEntity<>(new CustomErrorType("Provider with email " + providerEmail + " not found"),
//					HttpStatus.NOT_FOUND);
//		}
//		//Medida temporal para evitar que un proveedor autenticado pueda averiguar proveedor de otro usuario
//		if(!principal.getName().equals(providerEmail)) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		}		
//		return new ResponseEntity<Provider>(provider, HttpStatus.OK);
//	}
	
	@RequestMapping(value="/providers/{id}/images", method = RequestMethod.POST, headers ="content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadProviderImage(@PathVariable("id") String providerId,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (providerId == null || providerId.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		Provider provider = providerService.findProviderById(Integer.parseInt(providerId));
		if (provider == null) {
			return new ResponseEntity<>(new CustomErrorType("Provider with id: " + providerId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		// || !provider.getPhoto().isEmpty()
		if (provider.getPhoto() != null) {
			if (!provider.getPhoto().isEmpty()) {
				try {
					if(!provider.getPhoto().equalsIgnoreCase(PROVIDER_DEFAULT_IMAGE)) {
						amazonClient.deleteFileFromS3Bucket(PROVIDER_IMAGES_FOLDER,provider.getPhoto());
					}
					
				} catch (Exception e) {
					return new ResponseEntity<>(
							new CustomErrorType("Provider with id: " + providerId + " can't be erased"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		try {
			String fileUrl = amazonClient.uploadFile(PROVIDER_IMAGES_FOLDER,multipartFile);
			provider.setPhoto(fileUrl);
			providerService.updateProvider(provider);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image provider with id: " + providerId + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
