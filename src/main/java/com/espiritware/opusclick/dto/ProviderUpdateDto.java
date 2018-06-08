package com.espiritware.opusclick.dto;

import javax.persistence.Id;

import com.espiritware.opusclick.model.Location;
import com.espiritware.opusclick.model.Profession;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)		
public class ProviderUpdateDto {
	
	@Id
	private int id;

	private String accountName;
	
	private String accountLastname;
		
	private String identificationNumber;
	    
	private String phone;
	
	private int experience;
	
	//private String aboutMe;
	
	private int opusCoins;
	
	private int workDone;
	
	private Profession profession;
	
	private Location location;
	
}
