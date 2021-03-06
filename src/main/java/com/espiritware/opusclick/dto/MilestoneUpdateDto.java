package com.espiritware.opusclick.dto;

import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class MilestoneUpdateDto {
	
	private int id;
	
	private State state;
	
    private ItemUpdateDto item;
}
