package com.espiritware.opusclick.service;

import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.State;

public interface OnlineQuoteService {
	
	OnlineQuote createQuote (OnlineQuote quote);
			
	OnlineQuote updateQuote(OnlineQuote quote);
	
	OnlineQuote findQuoteById(int id);
	
	State getQuoteState(int id);
}
