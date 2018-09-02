package com.espiritware.opusclick.dto;

import java.util.Date;
import com.espiritware.opusclick.model.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class ReviewDto {
	
	@JsonIgnore
    private final Date datetime = new Date();
	
	private double satisfactionLevel;
	
	private Type type;
	
	private String comment;
	
	private String image;
	
	private boolean recommend;
}
