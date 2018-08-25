package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.ProviderQuote;

public interface ProviderQuoteService {
	
	ProviderQuote createProviderQuote(ProviderQuote providerQuote);

	ProviderQuote findQuoteById(int id);

	ProviderQuote updateProviderQuote(ProviderQuote providerQuote);
}
