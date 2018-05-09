package com.espiritware.opusclick.event;

import org.springframework.context.ApplicationEvent;

public class GenericEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public GenericEvent(Object source) {
		super(source);
	}

	
	
}
