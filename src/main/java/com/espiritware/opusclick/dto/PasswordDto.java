package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.annotations.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class PasswordDto {
	
	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;
	
}
