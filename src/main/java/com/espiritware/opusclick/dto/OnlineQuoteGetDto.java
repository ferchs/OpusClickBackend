package com.espiritware.opusclick.dto;

import java.util.Date;
import com.espiritware.opusclick.model.State;
import lombok.Getter;

@Getter
public class OnlineQuoteGetDto {
	
	private int id;
	
	private String quotationNumber;

	private Date date;

	private String requirements;
	
	private State state;


}
