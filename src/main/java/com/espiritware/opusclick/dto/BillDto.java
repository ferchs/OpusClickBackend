package com.espiritware.opusclick.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDto {
		
	private String billNumber;
	
	@JsonIgnore
    private final Date date = new Date();
	
	private double value;
	
	private String transactionNumber;
	
	private String paymentMethod;

}
