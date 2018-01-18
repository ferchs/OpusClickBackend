package com.espiritware.opusclick.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import com.espiritware.opusclick.dto.PasswordDto;
import com.espiritware.opusclick.dto.ProviderDto;
import com.espiritware.opusclick.dto.UserDto;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.User;
import com.espiritware.opusclick.security.TokenService;
import com.espiritware.opusclick.service.AccountService;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.State;

@Controller
@RequestMapping("/v1")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private Publisher publisher;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/users/registration", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> registerUserAccount(@Valid @RequestBody UserDto userAccountDto,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if (accountService.accountExist(userAccountDto.getEmail())) {
			return new ResponseEntity<String>(
					"Ya existe una cuenta registrada con este email",
					HttpStatus.CONFLICT);
		} else {
			accountService.registerAccount(userAccountDto.getEmail(), userAccountDto.getName(),
					userAccountDto.getLastname(), userAccountDto.getPassword());
			final User userRegistered = userService.registerUser(userAccountDto);
			publisher.publishRegistrationCompleteEvent(userRegistered.getEmail(), true, request.getLocale(),
					getAppUrl(request));
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(userRegistered.getEmail()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	}
	
	@RequestMapping(value = "/providers/registration", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> registerProviderAccount(@Valid @RequestBody ProviderDto providerAccountDto,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if (accountService.accountExist(providerAccountDto.getEmail())) {
			return new ResponseEntity<String>(
					"Ya existe una cuenta registrada con este email",
					HttpStatus.CONFLICT);
		} else {
			accountService.registerAccount(providerAccountDto.getEmail(), providerAccountDto.getName(),
					providerAccountDto.getLastname(), providerAccountDto.getPassword());
			final Provider registered = providerService.registerProvider(providerAccountDto);
			publisher.publishRegistrationCompleteEvent(registered.getEmail(), false, request.getLocale(),
					getAppUrl(request));
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(
					uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(registered.getEmail()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	}
	
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public ResponseEntity<String> confirmRegistration(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "verifyCode", required = true) String token, final HttpServletRequest request) {
		if (tokenService.validateAccountEmailToken(token)) {
			String subject = tokenService.getSubjectFromEmailToken(token);
			accountService.setAccountState(subject, State.ACCOUNT_CONFIRMED);
			if (type.equalsIgnoreCase("user")) {
				userService.setUserState(subject, State.INCOMPLETED_PROFILE);
			} else if (type.equalsIgnoreCase("provider")) {
				providerService.setProviderState(subject, State.WAITING_APPROVAL);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			String subject = tokenService.getSubjectFromEmailToken(token);
			if (tokenService.isTokenExpired(token)
					&& accountService.getAccountState(subject).equals(State.WAITING_EMAIL_CONFIRMATION)) {
				if (type.equalsIgnoreCase("user")) {
					publisher.publishRegistrationCompleteEvent(subject, true, request.getLocale(), getAppUrl(request));
				} else if (type.equalsIgnoreCase("provider")) {
					publisher.publishRegistrationCompleteEvent(subject, false, request.getLocale(), getAppUrl(request));
				}
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/sendResetPasswordEmail", method = RequestMethod.POST)
	public ResponseEntity<String> sendResetPasswordEmail(@RequestBody String email, final HttpServletRequest request) {
		if (email != null) {
			if (accountService.findAccountById(email) != null) {
				publisher.publishResetPasswordEvent(email, request.getLocale(), getResetUrl(request));
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<String> resetPassword(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "verifyCode", required = false) String token,
			@Valid @RequestBody PasswordDto passwordDto, final HttpServletRequest request) {
		if (tokenService.validateResetPasswordToken(token)) {
			Account account = accountService.findAccountById(email);
			account.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
			accountService.updateAccount(account);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

//De esta manera se detecta la url y puerto en donde se encuentra desplegado
//	 private String getAppUrl(HttpServletRequest request) {
//		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//	 }
	
//Pendiente por implementarlo para que cree la url de manera automatica
	 private String getAppUrl(HttpServletRequest request) {
			return "http://" + "localhost" + ":" + "4200" + "/confirmar_registro";
	 }
	 
	 private String getResetUrl(HttpServletRequest request) {
			return "http://" + "localhost" + ":" + "4200" + "/reestablecer_contraseña";
	 }
	 
}
