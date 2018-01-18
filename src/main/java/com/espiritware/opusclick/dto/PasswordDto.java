package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.espiritware.opusclick.validation.PasswordMatches;

@PasswordMatches
public class PasswordDto {
	
	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;
	
	public PasswordDto() {
		
	}
	
	public PasswordDto(String password, String matchingPassword) {
		super();
		this.password = password;
		this.matchingPassword = matchingPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
	
}
