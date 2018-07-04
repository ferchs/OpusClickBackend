package com.espiritware.opusclick.dto;

import java.util.Set;
import javax.persistence.Id;
import com.espiritware.opusclick.model.Location;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
	
	@Id
	private int id;
	
	private String accountEmail;

	private String accountName;
	
	private String accountLastname;
		
	private String identificationNumber;
	    
	private String phone;
						
	public Set<Location> locations;
	
}
