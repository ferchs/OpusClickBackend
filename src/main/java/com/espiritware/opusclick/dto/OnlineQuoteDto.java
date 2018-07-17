package com.espiritware.opusclick.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.RandomStringUtils;
import com.espiritware.opusclick.model.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnlineQuoteDto {

	@JsonIgnore
    private final String quotationNumber=RandomStringUtils.randomAlphanumeric(8).toUpperCase();
	
	@JsonIgnore
	private Date date = new Date();
	
	@NotNull
	private JsonNode requirements;
	    
	@JsonIgnore
    private final State state = State.PENDING_BY_QUOTATION;
			
}
