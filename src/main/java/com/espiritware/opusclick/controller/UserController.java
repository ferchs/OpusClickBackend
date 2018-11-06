package com.espiritware.opusclick.controller;

import java.security.Principal;
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
import com.espiritware.opusclick.dto.UserGetProfileDto;
import com.espiritware.opusclick.dto.UserUpdateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.model.User;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AccountService;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.service.WorkService;

@Controller
//@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 4800, allowCredentials = "false")
@RequestMapping("/v1")
public class UserController {
	
	private static final String USER_IMAGES_FOLDER="user-profile-images/";


	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AmazonClient amazonClient;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WorkService workService;
	
	
	@RequestMapping(value = "/users/{email:.+}", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderById(@PathVariable("email") String userEmail, Principal principal,
			UriComponentsBuilder uriComponentsBuilder) {
		User user = accountService.findAccountByEmail(userEmail).getUser();
		if (user == null) {
			return new ResponseEntity<>(new CustomErrorType("User with email " + userEmail + " not found"),
					HttpStatus.NOT_FOUND);
		}
		//Medida temporal para evitar que un proveedor autenticado pueda averiguar proveedor de otro usuario
		if(!principal.getName().equals(userEmail)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		UserGetProfileDto dto=modelMapper.map(user, UserGetProfileDto.class);
		return new ResponseEntity<UserGetProfileDto>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/images", method = RequestMethod.POST, headers ="content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadProviderImage(@RequestParam("email") String emailUser,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder uriComponentsBuilder) {
		if (emailUser == null || emailUser.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_user"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		User user = accountService.findAccountByEmail(emailUser).getUser();
		if (user == null) {
			return new ResponseEntity<>(new CustomErrorType("User with id: " + emailUser + " not found"),
					HttpStatus.NOT_FOUND);
		}
		// || !provider.getPhoto().isEmpty()
		if (user.getPhoto() != null) {
			if (!user.getPhoto().isEmpty()) {
				try {
					amazonClient.deleteFileFromS3Bucket(USER_IMAGES_FOLDER,user.getPhoto());
				} catch (Exception e) {
					return new ResponseEntity<>(
							new CustomErrorType("Provider with id: " + emailUser + " can't be erased"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		try {
			String fileUrl = amazonClient.uploadFile(USER_IMAGES_FOLDER,multipartFile);
			user.setPhoto(fileUrl);
			userService.updateUser(user);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image user with id: " + emailUser + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/users/{email_user:.+}/images", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getProviderImage(@PathVariable("email_user") String emailUser){
		if (emailUser == null) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_user"), HttpStatus.NO_CONTENT);
		}
		User user = accountService.findAccountByEmail(emailUser).getUser();
		if (user == null) {
			return new ResponseEntity<>(new CustomErrorType("User with id: " + emailUser + " not found"),
					HttpStatus.NOT_FOUND);
		}
		if (user.getPhoto() != null) {
			if (!user.getPhoto().isEmpty()) {
				return new ResponseEntity<String>(user.getPhoto(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/users/{email:.+}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateProvider(@DTO(UserUpdateDto.class) User user,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if(user!=null) {
			userService.updateUser(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	//Metodo usado para saber si se le hace el descuento del 8% por el primer servicio
	@RequestMapping(value = "/users/{id}/bills", method = RequestMethod.GET, headers = "Accept=application/json")
	@Transactional
	public ResponseEntity<?> existsBillForUser(@PathVariable("id") String userId,
			UriComponentsBuilder uriComponentsBuilder) {
		if(billExists(workService.findAllWorksOfUser(Integer.parseInt(userId)))) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	private boolean billExists(List<Work> works) {
		boolean billFound=false;
		for (Work work : works) {
			if(work.getContract()!= null) {
				if(work.getContract().getBill()!=null) {
					billFound=true;
					break;
				}
			}
		}
		return billFound;
	}

}
