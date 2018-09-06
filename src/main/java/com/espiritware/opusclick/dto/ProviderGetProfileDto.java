package com.espiritware.opusclick.dto;
import com.espiritware.opusclick.model.Availability;
import com.espiritware.opusclick.model.GlobalRating;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.State;

import lombok.Getter;

@Getter
public class ProviderGetProfileDto {
	
	private int id;
		
	private String identificationNumber;
	
	private String accountEmail;
	
	private String accountName;
	
	private String accountLastname;
	
	private String phone;
	
	private Availability availability;
	
	private String photo;
	
	private int experience;
	
	private String aboutMe;
	
	private int opusCoins;
	
	private int workDone;
	
	private State state;

	private Profession profession;
	
	private Location location;
		
	private GlobalRating globalRating;
		
}
