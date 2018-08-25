package com.espiritware.opusclick.dto;

import java.util.Set;
import javax.persistence.Id;
import com.espiritware.opusclick.model.State;

import lombok.Getter;

@Getter
public class ContractUpdateDto {

	@Id
	private int id;
				
	private String clarifications;
	
	private State state;
		
	private double subtotal;

	private double administrationFee;
	
	private Double totalValue;
	
	private Set<MilestoneUpdateDto> milestones;

}
