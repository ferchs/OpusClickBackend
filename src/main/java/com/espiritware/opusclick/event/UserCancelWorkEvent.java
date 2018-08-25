package com.espiritware.opusclick.event;

import com.espiritware.opusclick.model.Work;
import lombok.Getter;

@Getter
public class UserCancelWorkEvent extends GenericEvent{

	private static final long serialVersionUID = 1L;
	
	private Work work;
	
	public UserCancelWorkEvent(Object source, Work work) {
		super(source);
		this.work=work;
	}
}
