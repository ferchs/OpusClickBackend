package com.espiritware.opusclick.dto;

public class LoginDto {

	private String email;
	
	private String password;
	
	private boolean userLogin;

	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public boolean isUserLogin() {
		return userLogin;
	}

	public void setUserLogin(boolean userLogin) {
		this.userLogin = userLogin;
	}
	
	
}
