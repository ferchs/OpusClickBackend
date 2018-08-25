package com.espiritware.opusclick.dto;

import java.util.Date;
import java.util.Set;

import com.espiritware.opusclick.model.Item;

import lombok.Getter;

@Getter
public class ProviderQuoteGetDto {
	
	private int id;
	
	private String number;
	
	private Date date;
	
	private String clarifications;
	
	private double subtotal;

	private double administrationFee;
	
	private double total;
	
	private Set<Item> items;
}
