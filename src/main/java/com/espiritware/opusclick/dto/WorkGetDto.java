package com.espiritware.opusclick.dto;

import java.util.Date;
import com.espiritware.opusclick.model.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkGetDto {

	public int id;
	
	public String workNumber;
	
	public String providerLabel;

	public String userLabel;
	
	public Date creationDate;

	public State state;
}
