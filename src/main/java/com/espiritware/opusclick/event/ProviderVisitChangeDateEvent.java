package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.Visit;

import lombok.Getter;

@Getter
public class ProviderVisitChangeDateEvent extends GenericEvent{

	private static final long serialVersionUID = 1L;
	
	private Visit visit;
	
	public ProviderVisitChangeDateEvent(Object source, Visit visit) {
		super(source);
		this.visit=visit;
	}

}
