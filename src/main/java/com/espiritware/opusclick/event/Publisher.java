package com.espiritware.opusclick.event;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
	
    public void publishUserRegistrationEvent(final int id, final String email,final Locale locale, final String appUrl) {
    		UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(this, id, email, locale, appUrl);
        applicationEventPublisher.publishEvent(userRegistrationEvent);
    }
    
    public void publishProviderRegistrationEvent(final int id, final String email,final Locale locale, final String appUrl ) {
		ProviderRegistrationEvent registrationCompleteEvent = new ProviderRegistrationEvent(this, id, email,locale, appUrl);
		applicationEventPublisher.publishEvent(registrationCompleteEvent);
    }
    
    public void publishResetPasswordEvent(final String email, final Locale locale, final String appUrl ) {
        ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent(this, email,locale, appUrl);
        applicationEventPublisher.publishEvent(resetPasswordEvent);
    }
}
