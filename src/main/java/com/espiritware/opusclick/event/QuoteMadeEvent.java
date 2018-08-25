package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.ProviderQuote;
import lombok.Getter;

@Getter
public class QuoteMadeEvent extends GenericEvent{

	private static final long serialVersionUID = 1L;
	
	private ProviderQuote providerQuote;
	
	public QuoteMadeEvent(Object source, ProviderQuote quote) {
		super(source);
		this.providerQuote=quote;
	}
}
