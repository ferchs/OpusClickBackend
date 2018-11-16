package com.espiritware.opusclick.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.annotations.PasswordMatches;
import com.espiritware.opusclick.annotations.ValidEmail;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.State;
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
	
	@NotNull
	private Set<Location> userLocations;
	
	@JsonIgnore
	private String photo="https://s3-sa-east-1.amazonaws.com/opusclick.com/provider-profile-images/default-profile-photo.png";

	@JsonIgnore
    private final State state = State.WAITING_EMAIL_CONFIRMATION;
	
	@JsonIgnore
    private final int userOpusCoins=10;
}
