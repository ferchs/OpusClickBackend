package com.espiritware.opusclick.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ProviderUpdateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.service.ProviderService;

@Controller
@RequestMapping("/v1")
public class ProviderController {
	
	public static final String PROVIDERS_UPLOADED_FOLDER="Images/Providers/";
	
	@Autowired
	private ProviderService providerService;
	
	@RequestMapping(value = "/providers", method = RequestMethod.PUT, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> updateProvider(@DTO(ProviderUpdateDto.class) Provider provider,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		return null;
	}
	
	@RequestMapping(value = "/providers", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<List<Provider>> getProvidersByProfessionName(
			@RequestParam(value = "profession", required = false) String professionName,
			UriComponentsBuilder uriComponentsBuilder) {
		List<Provider> providers = new ArrayList<Provider>();
		if (professionName == null) {
			providers = providerService.findAllProviders();
			if (providers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<Provider>>(providers, HttpStatus.OK);
			}
		} else {
			providers=providerService.findProvidersByProfessionName(professionName);
			Collections.sort(providers);
			if(providers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else {
				return new ResponseEntity<List<Provider>>(providers,HttpStatus.OK);
			}
		}
	}
	
	@RequestMapping(value="/providers/images", method = RequestMethod.POST, headers ="content-type=multipart/form-data")
	@Transactional
	//ResponseEntity<byte[]>
	public ResponseEntity<byte[]> uploadProviderImage(@RequestParam("id") Long idProvider, @RequestParam("file") MultipartFile multipartFile,
			UriComponentsBuilder uriComponentsBuilder){
		if(idProvider==null) {
			return new ResponseEntity(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		if(multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		Provider provider= providerService.findProviderById(providerService+"");
		if(provider==null) {
			return new ResponseEntity(new CustomErrorType("Provider with id: "+idProvider+" not found"), HttpStatus.NOT_FOUND);
		}
		if(!provider.getPhoto().isEmpty() || provider.getPhoto() !=null) {
			String fileName=provider.getPhoto();
			Path path= Paths.get(fileName);
			File file= path.toFile();
			if(file.exists()) {
				file.delete();
			}
		}
		try {
			Date date= new Date();
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateName= dateFormat.format(date);
			String filename= String.valueOf(idProvider) + "-providerPhoto-" + dateName + "." + multipartFile.getContentType().split("/")[1];
			provider.setPhoto(PROVIDERS_UPLOADED_FOLDER + filename);
			byte[] photoBytes= multipartFile.getBytes();
			Path path= Paths.get(PROVIDERS_UPLOADED_FOLDER + filename);
			Files.write(path, photoBytes);
			providerService.updateProvider(provider);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error during upload " + multipartFile.getOriginalFilename()), HttpStatus.CONFLICT);
		}
	}
	
	@RequestMapping(value="/providers/{id_provider}/images", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<byte[]> getProviderImage(@PathVariable("id_provider") Long idProvider){
		if(idProvider==null) {
			return new ResponseEntity(new CustomErrorType("Please set id_provider"), HttpStatus.NO_CONTENT);
		}
		Provider provider= providerService.findProviderById(providerService+"");
		if(provider==null) {
			return new ResponseEntity(new CustomErrorType("Provider with id: "+idProvider+" not found"), HttpStatus.NOT_FOUND);
		}
		try {
			String filename= provider.getPhoto();
			Path path= Paths.get(filename);
			File file=path.toFile();
			if(!file.exists()) {
				return new ResponseEntity(new CustomErrorType("Image not found"), HttpStatus.NOT_FOUND);
			}
			byte [] photo= Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image"), HttpStatus.CONFLICT);
		}
	}
}
