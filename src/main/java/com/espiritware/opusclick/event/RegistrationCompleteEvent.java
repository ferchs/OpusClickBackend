package com.espiritware.opusclick.event;

import java.util.Locale;

public class RegistrationCompleteEvent extends GenericEvent {

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final Locale locale;
	private final String email;
	private final boolean isUser;

	public RegistrationCompleteEvent(Object source, String email, boolean isUser, Locale locale, String appUrl) {
        super(source);
        this.email=email;
        this.locale=locale;
        this.appUrl=appUrl;
        this.isUser=isUser;
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

	public boolean isUser() {
		return isUser;
	}
	
}
