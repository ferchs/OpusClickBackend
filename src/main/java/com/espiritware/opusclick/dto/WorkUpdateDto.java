package com.espiritware.opusclick.dto;
import javax.persistence.Id;
import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class WorkUpdateDto {
	
	@Id
	private int id;

	private String providerLabel;

	private String userLabel;
	
	private State state;
	
	private String comment;
}
