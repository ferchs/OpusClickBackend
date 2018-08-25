package com.espiritware.opusclick.dto;

import javax.persistence.Id;

import lombok.Getter;

@Getter
public class ItemUpdateDto {
	
	private int id;
	
	private String name;
	
	private double value;
	
	private double durationValue;
	
	private String durationTime;
	
	private double warrantyValue;
	
	private String warrantyTime;
	
	private String workDescription;
	
	private String warrantyDescription;
		
	private String imageContract;
		
	private String commentContract;
}
