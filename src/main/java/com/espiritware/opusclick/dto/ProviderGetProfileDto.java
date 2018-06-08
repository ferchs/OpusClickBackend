package com.espiritware.opusclick.dto;

import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.GlobalRating;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.State;

public class ProviderGetProfileDto {
	
	public int id;
	
	public String identificationNumber;
	
	public String accountName;
	
	public String accountLastname;
	
	public String phone;
	
	public Availability availability;
	
	public String photo;
	
	public int experience;
	
	public String aboutMe;
	
	public int opusCoins;
	
	public int workDone;
	
	public State state;
	
	public Profession profession;
	
	public Location location;
		
	public GlobalRating globalRating;
		
}
