package com.espiritware.opusclick.dto;

import com.espiritware.opusclick.model.Account;
import com.espiritware.opusclick.model.GlobalRating;
import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.espiritware.opusclick.model.State;

public class ProviderGetProfileDto {
	
	public String providerId;
	
	public String identificationNumber;
	
	public String phone;
		
	public String photo;
		
	public int opusCoins;
	
	public int workDone;
	
	//public State state;
	
	public String professionName;
	
	public String locationAddress;
	
	public String locationCityName;
	
	public String accountName;
	
	public String accountLastname;
	
    //public GlobalRating globalRating;
}
