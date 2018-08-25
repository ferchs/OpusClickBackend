package com.espiritware.opusclick.dto;

import java.util.Date;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import com.espiritware.opusclick.model.Milestone;
import com.espiritware.opusclick.model.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ContractDto {

	@JsonIgnore
    private final String contractNumber = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	
	@JsonIgnore
    private final Date creationDate = new Date();
	
	private String clarifications;
	
	private State state;
		
	private double subtotal;

	private double administrationFee;
	
	private double totalValue;
	
	private Set<Milestone> milestones;
}
