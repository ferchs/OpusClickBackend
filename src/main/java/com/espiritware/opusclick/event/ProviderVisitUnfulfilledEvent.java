package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.Visit;

import lombok.Getter;

@Getter
public class ProviderVisitUnfulfilledEvent extends GenericEvent {

	private static final long serialVersionUID = 1L;
	
	private Visit visit;
	
	public ProviderVisitUnfulfilledEvent(Object source, Visit visit) {
		super(source);
		this.visit=visit;
	}


}
