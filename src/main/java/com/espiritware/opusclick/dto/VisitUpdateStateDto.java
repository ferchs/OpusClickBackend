package com.espiritware.opusclick.dto;

import javax.persistence.Id;
import com.espiritware.opusclick.model.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitUpdateStateDto {
	
	@Id
	public int id;
	
	public State state;
}
