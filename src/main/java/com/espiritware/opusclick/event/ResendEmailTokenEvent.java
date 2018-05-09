package com.espiritware.opusclick.event;

import java.util.Locale;
import org.springframework.context.ApplicationEvent;
import com.espiritware.opusclick.model.User;

public class ResendEmailTokenEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final Locale locale;
	private final User user;

	public ResendEmailTokenEvent(Object source, User user,Locale locale, String appUrl) {
        super(source);
        this.user=user;
        this.locale=locale;
        this.appUrl=appUrl;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public User getUser() {
		return user;
	}



}
