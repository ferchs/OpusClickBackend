package com.espiritware.opusclick.dto;
import javax.persistence.Id;
import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class WorkUpdateDto {
	
	@Id
	public int id;

	public String providerLabel;

	public String userLabel;
	
	public State state;
}
