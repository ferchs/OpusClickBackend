package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.OnlineQuote;
import lombok.Getter;

@Getter
public class UserQuotationEvent extends GenericEvent{

	private static final long serialVersionUID = 1L;
	
	private OnlineQuote onlineQuote;
	
	
	public UserQuotationEvent(Object source, OnlineQuote onlineQuote) {
		super(source);
		this.onlineQuote=onlineQuote;
	}
	
}
