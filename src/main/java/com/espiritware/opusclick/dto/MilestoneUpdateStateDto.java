package com.espiritware.opusclick.dto;

import java.util.Date;

import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class MilestoneUpdateStateDto {
	
	private int id;
	
	private Date startDate;
	
	private Date endDate;
	
	private State state;
	
    private ItemGetDto item;
}
