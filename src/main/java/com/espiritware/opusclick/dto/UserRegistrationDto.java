package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.annotations.PasswordMatches;
import com.espiritware.opusclick.annotations.ValidEmail;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class UserRegistrationDto {
	
	@ValidEmail
	@NotNull
    @NotEmpty
	private String email;
	
	@NotNull
    @NotEmpty
	private String name;
	
	@NotNull
    @NotEmpty
	private String lastname;
	
	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;

	@JsonIgnore
    private final State state = State.WAITING_EMAIL_CONFIRMATION;
	
	@NotNull
	private User user;

//	@NotNull
//    @NotEmpty
//	private String password;
//	private String matchingPassword;
	
//	@ValidEmail
//	@NotNull
//    @NotEmpty
//	private String userId;
//		
//	@NotNull
//    @NotEmpty
//	private String name;
//
//	@NotNull
//    @NotEmpty
//	private String lastname;
//	
//	@NotNull
//    @NotEmpty
//	private String city;
//	
//	@NotNull
//    @NotEmpty
//	private String password;
//	private String matchingPassword;
}
