package com.espiritware.opusclick.dto;

import java.util.Date;
import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class WorkGetDto {

	private int id;
	
	private String workNumber;
	
	private String providerLabel;

	private String userLabel;
	
	private Date creationDate;

	private State state;
	
	private int contractId;
}
