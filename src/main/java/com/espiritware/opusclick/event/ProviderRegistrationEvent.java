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
	private final String name;

	public ProviderRegistrationEvent(Object source, int id, String email, String name, Locale locale, String appUrl) {
        super(source);
        this.id=id;
        this.email=email;
        this.locale=locale;
        this.appUrl=appUrl;
        this.name=name;
    }

}
