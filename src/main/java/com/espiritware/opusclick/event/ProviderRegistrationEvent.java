package com.espiritware.opusclick.event;

import java.util.Locale;

import lombok.Getter;

@Getter
public class ProviderRegistrationEvent extends GenericEvent {

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final Locale locale;
	private final String email;
	private final int id;

	public ProviderRegistrationEvent(Object source, int id, String email, Locale locale, String appUrl) {
        super(source);
        this.id=id;
        this.email=email;
        this.locale=locale;
        this.appUrl=appUrl;
    }

}
