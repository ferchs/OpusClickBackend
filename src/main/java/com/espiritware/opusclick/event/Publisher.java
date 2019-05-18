package com.espiritware.opusclick.event;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.espiritware.opusclick.model.Contract;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.ProviderQuote;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;

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
    
    public void publishReviewEvent(final Work work ) {
    	ReviewEvent reviewEvent = new ReviewEvent(this, work);
        applicationEventPublisher.publishEvent(reviewEvent);
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
    
    public void publishUserCancelWorkEvent(final Work work) {
    	UserCancelWorkEvent userCancelWorkEvent = new UserCancelWorkEvent(this,work);
        applicationEventPublisher.publishEvent(userCancelWorkEvent);
    }
    
    public void publishUserModifiesContractEvent(final Contract contract) {
    	UserModifiesContractEvent userModifiesContractEvent = new UserModifiesContractEvent(this,contract);
        applicationEventPublisher.publishEvent(userModifiesContractEvent);
    }
    
    public void publishUserMakesPaymentEvent(final Contract contract) {
    	UserMakesPaymentEvent userMakesPaymentEvent = new UserMakesPaymentEvent(this,contract);
        applicationEventPublisher.publishEvent(userMakesPaymentEvent);
    }
    
    public void publishUserAuthorizesPaymentEvent(final Contract contract) {
    	UserAuthorizesPaymentEvent userAuthorizesPaymentEvent = new UserAuthorizesPaymentEvent(this,contract);
        applicationEventPublisher.publishEvent(userAuthorizesPaymentEvent);
    }
    
    public void publishUserDenyPaymentEvent(final Contract contract) {
    	UserDenyPaymentEvent userDenyPaymentEvent = new UserDenyPaymentEvent(this,contract);
        applicationEventPublisher.publishEvent(userDenyPaymentEvent);
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
    
    public void publishProviderCancelWorkEvent(final Work work) {
    	ProviderCancelWorkEvent providerCancelWorkEvent = new ProviderCancelWorkEvent(this,work);
        applicationEventPublisher.publishEvent(providerCancelWorkEvent);
    }
    
    public void publishProviderQuoteMadeEvent(final ProviderQuote providerQuote) {
    	QuoteMadeEvent quoteMadeEvent = new QuoteMadeEvent(this,providerQuote);
        applicationEventPublisher.publishEvent(quoteMadeEvent);
    }
    
    public void publishProviderAcceptContractEvent(final Contract contract) {
    	ProviderAcceptContractEvent providerAcceptContractEvent = new ProviderAcceptContractEvent(this,contract);
        applicationEventPublisher.publishEvent(providerAcceptContractEvent);
    }
    
    public void publishProviderModifiesContractEvent(final Contract contract) {
    	ProviderModifiesContractEvent providerModifiesContractEvent = new ProviderModifiesContractEvent(this,contract);
        applicationEventPublisher.publishEvent(providerModifiesContractEvent);
    }
    
    public void publishProviderRequestPaymentEvent(final Contract contract) {
    	ProviderRequestPaymentEvent providerRequestPaymentEvent = new ProviderRequestPaymentEvent(this,contract);
        applicationEventPublisher.publishEvent(providerRequestPaymentEvent);
    }
    
    public void publishProviderFinalizedContractEvent(final Contract contract) {
    	ProviderFinalizedContractEvent providerFinalizedContractEvent = new ProviderFinalizedContractEvent(this,contract);
        applicationEventPublisher.publishEvent(providerFinalizedContractEvent);
    }
    
    public void publishProblemEvent(final Work work) {
    	ProblemEvent problemEvent = new ProblemEvent(this,work);
        applicationEventPublisher.publishEvent(problemEvent);
    }

}
