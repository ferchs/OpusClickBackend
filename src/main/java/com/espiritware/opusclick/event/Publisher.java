package com.espiritware.opusclick.event;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.Visit;

@Component
public class Publisher {

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
	
    public void publishUserRegistrationEvent(final int id, final String email, String name,final Locale locale, final String appUrl) {
    		UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(this, id, email, name, locale, appUrl);
        applicationEventPublisher.publishEvent(userRegistrationEvent);
    }
    
    public void publishProviderRegistrationEvent(final int id, final String email, String name, final Locale locale, final String appUrl ) {
		ProviderRegistrationEvent registrationCompleteEvent = new ProviderRegistrationEvent(this, id, email, name, locale, appUrl);
		applicationEventPublisher.publishEvent(registrationCompleteEvent);
    }
    
    public void publishResetPasswordEvent(final String email, final Locale locale, final String appUrl ) {
        ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent(this, email,locale, appUrl);
        applicationEventPublisher.publishEvent(resetPasswordEvent);
    }
    
    public void publishUserVisitRequestEvent(final Visit visit) {
    	UserVisitRequestEvent userVisitRequestEvent = new UserVisitRequestEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitRequestEvent);
    }
    
    public void publishUserQuotationEvent(final OnlineQuote onlineQuote) {
    	UserQuotationEvent userQuotationEvent = new UserQuotationEvent(this,onlineQuote);
        applicationEventPublisher.publishEvent(userQuotationEvent);
    }
    
    public void publishUserVisitChangeDateEvent(final Visit visit) {
    	UserVisitChangeDateEvent userVisitChangeDateEvent = new UserVisitChangeDateEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitChangeDateEvent);
    }
    
    public void publishUserVisitAcceptedEvent(final Visit visit) {
    	UserVisitAcceptedEvent userVisitAcceptedEvent = new UserVisitAcceptedEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitAcceptedEvent);
    }
    
    public void publishUserVisitPostponedEvent(final Visit visit) {
    	UserVisitPostponedEvent userVisitPostponedEvent = new UserVisitPostponedEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitPostponedEvent);
    }
    
    public void publishUserVisitRejectedEvent(final Visit visit) {
    	UserVisitRejectedEvent userVisitRejectedEvent = new UserVisitRejectedEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitRejectedEvent);
    }
    
    public void publishUserVisitUnfulfilledEvent(final Visit visit) {
    	UserVisitUnfulfilledEvent userVisitUnfulfilledEvent = new UserVisitUnfulfilledEvent(this,visit);
        applicationEventPublisher.publishEvent(userVisitUnfulfilledEvent);
    }
    
    public void publishProviderVisitChangeDateEvent(final Visit visit) {
    	ProviderVisitChangeDateEvent providerVisitChangeDateEvent = new ProviderVisitChangeDateEvent(this,visit);
        applicationEventPublisher.publishEvent(providerVisitChangeDateEvent);
    }
    
    public void publishProviderVisitAcceptedEvent(final Visit visit) {
    	ProviderVisitAcceptedEvent providerVisitAcceptedEvent = new ProviderVisitAcceptedEvent(this,visit);
        applicationEventPublisher.publishEvent(providerVisitAcceptedEvent);
    }
    
    public void publishProviderVisitPostponedEvent(final Visit visit) {
    	ProviderVisitPostponedEvent providerVisitPostponedEvent = new ProviderVisitPostponedEvent(this,visit);
        applicationEventPublisher.publishEvent(providerVisitPostponedEvent);
    }
    
    public void publishProviderVisitRejectedEvent(final Visit visit) {
    	ProviderVisitRejectedEvent providerVisitRejectedEvent = new ProviderVisitRejectedEvent(this,visit);
        applicationEventPublisher.publishEvent(providerVisitRejectedEvent);
    }
    
    public void publishProviderVisitUnfulfilledEvent(final Visit visit) {
    	ProviderVisitUnfulfilledEvent providerVisitUnfulfilledEvent = new ProviderVisitUnfulfilledEvent(this,visit);
        applicationEventPublisher.publishEvent(providerVisitUnfulfilledEvent);
    }
}
