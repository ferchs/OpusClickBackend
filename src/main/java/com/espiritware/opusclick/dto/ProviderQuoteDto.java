package com.espiritware.opusclick.dto;

import java.util.Date;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import com.espiritware.opusclick.model.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ProviderQuoteDto {
		
	@JsonIgnore
    private final String number = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	
	@JsonIgnore
    private final Date date = new Date();
	
	private String clarifications;
	
	private double subtotal;

	private double administrationFee;
	
	private double total;
	
	private Set<Item> items;
}
