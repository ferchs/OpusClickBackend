package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.Contract;
import lombok.Getter;

@Getter
public class UserMakesPaymentEvent extends GenericEvent{
	
	private static final long serialVersionUID = 1L;
	
	private Contract contract;
	
	public UserMakesPaymentEvent(Object source, Contract contract) {
		super(source);
		this.contract=contract;
	}
}
