package com.espiritware.opusclick.dto;

import java.util.Set;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.State;

public class UserGetProfileDto {

	public int id;
	
	public String identificationNumber;
	
	public String phone;
	
	public String photo;
	
	public int opusCoins;
	
	public State state;
	
	public String accountName;
	
	public String accountLastname;
	
	public String accountEmail;
	
	public Set<Location> locations;
}
