package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.annotations.ValidEmail;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ResendEmailDto {

	@ValidEmail
	@NotNull
    @NotEmpty
	private String email;
	
	@NotNull
	@JsonProperty("isUser")
	private Boolean isUser;
	
}
