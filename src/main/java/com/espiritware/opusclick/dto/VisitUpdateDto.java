package com.espiritware.opusclick.dto;

import java.sql.Time;
import java.util.Date;
import javax.persistence.Id;
import com.espiritware.opusclick.model.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitUpdateDto {
	
	@Id
	public int id;
	
	public State state;
	
	public Date date;
	
	public Date alternativeDate;
	
	public String address;
	
	public String neighborhood;
	
	public String description;
	
	public Time lowerLimit;
	
	public Time alternativeLowerLimit;
	
	public Time upperLimit;
		
	public Time alternativeUpperLimit;
	
	public String breachDescription;
	
}
