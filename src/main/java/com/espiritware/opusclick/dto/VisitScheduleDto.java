package com.espiritware.opusclick.dto;

import java.sql.Time;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import com.espiritware.opusclick.model.State;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitScheduleDto {
	
	public Date date;
	
	public Date alternativeDate;
	
	public String address;
	
	public String neighborhood;
	
	public String description;
	
	public Time lowerLimit;
	
	public Time alternativeLowerLimit;
	
	public Time upperLimit;
		
	public Time alternativeUpperLimit;
	
	@JsonIgnore
    private final String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	
	@JsonIgnore
    private final String securityPin = RandomStringUtils.randomNumeric(4);
	
	@JsonIgnore
    private final State state = State.PENDING_BY_PROVIDER_ACCEPT;
	
}
