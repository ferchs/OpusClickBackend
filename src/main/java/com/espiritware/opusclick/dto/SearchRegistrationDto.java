package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRegistrationDto {

	@NotNull
    @NotEmpty
	private String description;
}
