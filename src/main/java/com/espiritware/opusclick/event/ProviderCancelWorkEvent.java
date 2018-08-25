package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.Work;
import lombok.Getter;

@Getter
public class ProviderCancelWorkEvent extends GenericEvent {

	private static final long serialVersionUID = 1L;
	
	private Work work;
	
	public ProviderCancelWorkEvent(Object source, Work work) {
		super(source);
		this.work=work;
	}
}
