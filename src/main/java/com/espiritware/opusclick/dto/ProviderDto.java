package com.espiritware.opusclick.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.espiritware.opusclick.validation.PasswordMatches;
import com.espiritware.opusclick.validation.ValidEmail;

@PasswordMatches
public class ProviderDto {
	
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
	private String profession;
	
	@NotNull
    @NotEmpty
	private String city;

	@NotNull
    @NotEmpty
	private String password;
	private String matchingPassword;

	
	public ProviderDto() {
	}

	public ProviderDto(String email, String name, String lastname, String profession, String city, String password,
			String matchingPassword) {
		this.email = email;
		this.name = name;
		this.lastname = lastname;
		this.profession = profession;
		this.password = password;
		this.matchingPassword = matchingPassword;
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

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
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

	public void setConfirmPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
}
