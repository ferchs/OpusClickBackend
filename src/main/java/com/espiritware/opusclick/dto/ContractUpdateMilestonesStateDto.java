package com.espiritware.opusclick.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Id;

import com.espiritware.opusclick.model.State;

import lombok.Getter;

@Getter
public class ContractUpdateMilestonesStateDto {

	@Id
	private int id;
	
	private String contractNumber;
	
	private Date creationDate;
	
	private Date startDate;
	
	private Date endDate;
	
	private String clarifications;
	
	private State state;
	
	private double subtotal;

	private double administrationFee;
	
	private double totalValue;
		
	private Set<MilestoneUpdateStateDto> milestones;

}
