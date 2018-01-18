package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.validation.PasswordMatches;
import com.espiritware.opusclick.validation.ValidEmail;


@PasswordMatches
public class UserDto {
	
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
	private String city;
	
	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;

	
	public UserDto() {
		
	}
	
	public UserDto(String email, String name, String lastname, String city,String password, String matchingPassword) {
		this.email=email;
		this.name=email;
		this.lastname=lastname;
		this.city=city;
		this.password=password;
		this.matchingPassword=matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
