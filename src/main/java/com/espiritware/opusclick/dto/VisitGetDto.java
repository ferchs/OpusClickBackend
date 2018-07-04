package com.espiritware.opusclick.dto;

import java.sql.Time;
import java.util.Date;
import com.espiritware.opusclick.model.State;

public class VisitGetDto {
	
	public int id;
	
	public String code;
	
	public String securityPin;

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
	
	public int workId;
}
