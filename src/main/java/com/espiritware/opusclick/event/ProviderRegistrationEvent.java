package com.espiritware.opusclick.event;

import java.util.Locale;

public class ProviderRegistrationEvent extends GenericEvent {

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final Locale locale;
	private final String email;

	public ProviderRegistrationEvent(Object source, String email, Locale locale, String appUrl) {
        super(source);
        this.email=email;
        this.locale=locale;
        this.appUrl=appUrl;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getEmail() {
		return email;
	}
}
