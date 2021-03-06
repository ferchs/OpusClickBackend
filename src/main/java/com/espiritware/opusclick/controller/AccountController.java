package com.espiritware.opusclick.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.PasswordDto;
import com.espiritware.opusclick.dto.ProviderRegistrationDto;
import com.espiritware.opusclick.dto.ResendEmailDto;
import com.espiritware.opusclick.dto.UserRegistrationDto;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.security.TokenService;
import com.espiritware.opusclick.service.AccountService;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.UserService;
import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.State;

@Controller
@RequestMapping("/v1")
public class AccountController {
	
	@Value("${app.hostname}")
	private String hostname;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProviderService providerService;
		
	@Autowired
	private Publisher publisher;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	//Cuenta Con ususuario
	@RequestMapping(value = "/accounts/user", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> registerUserAccount(@Valid @DTO(UserRegistrationDto.class) Account account,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if (accountService.accountExist(account.getEmail())) {
			Account registredAccount = accountService.findAccountByEmail(account.getEmail());
			if (registredAccount.getUser() != null) {
				return new ResponseEntity<String>("Ya existe una cuenta registrada con este email",
						HttpStatus.CONFLICT);
			} else {
				account.getUser().setId(registredAccount.getId());
				registredAccount.setUser(account.getUser());
				registredAccount.getUser().setAccount(registredAccount);
				accountService.updateAccount(registredAccount);
				publisher.publishUserRegistrationEvent(registredAccount.getId(), registredAccount.getEmail(), registredAccount.getName(),
						request.getLocale(), getAppUrl(request));
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(
						uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(account.getEmail()).toUri());
				return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			}
		} else {
			account.setPassword(passwordEncoder.encode(account.getPassword()));
			account.getUser().setAccount(account);
			account = accountService.createAccount(account);
			publisher.publishUserRegistrationEvent(account.getId(), account.getEmail(), account.getName(),
					request.getLocale(), getAppUrl(request));
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(account.getEmail()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	}
	
	//Cuenta Con proveedor
	@RequestMapping(value = "/accounts/provider", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> registerProviderAccount(@Valid @DTO(ProviderRegistrationDto.class) Account account,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		
		if (providerService.phoneExist(account.getProvider().getPhone())) {
			return new ResponseEntity<String>("409-2", HttpStatus.CONFLICT);
		}else if (accountService.accountExist(account.getEmail())) {
			Account registredAccount = accountService.findAccountByEmail(account.getEmail());
			if (registredAccount.getProvider() != null) {
				return new ResponseEntity<String>("409-1", HttpStatus.CONFLICT);
			} else {
				account.getProvider().setId(registredAccount.getId());
				registredAccount.setProvider(account.getProvider());
				registredAccount.getProvider().setAccount(registredAccount);
				accountService.updateAccount(registredAccount);
				publisher.publishProviderRegistrationEvent(registredAccount.getId(), registredAccount.getEmail(), registredAccount.getName(),
						request.getLocale(), getAppUrl(request));
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(
						uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(account.getEmail()).toUri());
				return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			}
		} else {
			account.setPassword(passwordEncoder.encode(account.getPassword()));
			account.getProvider().setAccount(account);
			accountService.createAccount(account);
			publisher.publishProviderRegistrationEvent(account.getId(), account.getEmail(), account.getName(),
					request.getLocale(), getAppUrl(request));
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(uriComponentsBuilder.path("/cuenta_creada").buildAndExpand(account.getEmail()).toUri());
			return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	}
	
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public ResponseEntity<String> confirmRegistration(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "verifyCode", required = true) String token, final HttpServletRequest request) {
		
		int id=tokenService.getIdFromEmailToken(token);
		if (tokenService.validateAccountEmailToken(token)) {
			accountService.setAccountState(id, State.ACCOUNT_CONFIRMED);
			if (type.equalsIgnoreCase("user")) {
				userService.setUserState(id, State.INCOMPLETED_PROFILE);
			} else if (type.equalsIgnoreCase("provider")) {
				providerService.setProviderState(id, State.WAITING_APPROVAL);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			if (tokenService.isTokenExpired(token)
					&& accountService.getAccountState(id).equals(State.WAITING_EMAIL_CONFIRMATION)) {
				String subject=tokenService.getSubjectFromEmailToken(token);
				if (type.equalsIgnoreCase("user")) {
					publisher.publishUserRegistrationEvent(id,subject,accountService.findAccountById(id).getName(),request.getLocale(), getAppUrl(request));
				} else if (type.equalsIgnoreCase("provider")) {
					publisher.publishProviderRegistrationEvent(id,subject,accountService.findAccountById(id).getName(),request.getLocale(), getAppUrl(request));
				}
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/sendResetPasswordEmail", method = RequestMethod.POST)
	public ResponseEntity<String> sendResetPasswordEmail(@RequestBody String email, final HttpServletRequest request) {
		if (email != null) {
			if (accountService.findAccountByEmail(email) != null) {
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
			Account account = accountService.findAccountByEmail(email);
			account.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
			accountService.updateAccount(account);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/resendConfirmationEmail", method = RequestMethod.POST)
	public ResponseEntity<String> resendConfirmationEmail(@Valid @RequestBody ResendEmailDto resendEmailDto, final HttpServletRequest request) {
		Account registredAccount = accountService.findAccountByEmail(resendEmailDto.getEmail());
		if(registredAccount!=null) {
			if (resendEmailDto.getIsUser()) {
				publisher.publishUserRegistrationEvent(registredAccount.getId(), registredAccount.getEmail(), registredAccount.getName(),
						request.getLocale(), getAppUrl(request));
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				publisher.publishProviderRegistrationEvent(registredAccount.getId(), registredAccount.getEmail(), registredAccount.getName(),
						request.getLocale(), getAppUrl(request));
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

//De esta manera se detecta la url y puerto en donde se encuentra desplegado
//	 private String getAppUrlOpus(HttpServletRequest request) {
//		 String url="http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//		 url=request.getHeader("referer");
//		 return url;
//	 }
	
//Pendiente por implementarlo para que cree la url de manera automatica
	 private String getAppUrl(HttpServletRequest request) {
			return  hostname+ "/confirmar_registro";
		 //return "http://" + "localhost" + ":" + "8083" + "/confirmar_registro";
		 //return "http://" + "opusclick.com" + "/confirmar_registro";
	 }
	 
	 private String getResetUrl(HttpServletRequest request) {
			return hostname+ "/reestablecer_contraseña";
		 //return "http://" + "localhost" + ":" + "8083" + "/reestablecer_contraseña";
		 //return "http://" + "opusclick.com" + "/reestablecer_contraseña";
	 }
	 
}
