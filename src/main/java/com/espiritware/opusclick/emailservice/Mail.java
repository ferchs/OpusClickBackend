package com.espiritware.opusclick.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.espiritware.opusclick.event.GenericEvent;
import com.espiritware.opusclick.event.ProviderRegistrationEvent;
import com.espiritware.opusclick.event.ResetPasswordEvent;
import com.espiritware.opusclick.event.UserRegistrationEvent;
import com.espiritware.opusclick.security.TokenService;

@Component
public class Mail implements ApplicationListener<GenericEvent>{

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	@Autowired 
	private TokenService tokenUtil;

	
	@Autowired
    private Environment env;
	
	@Override
	public void onApplicationEvent(GenericEvent event) {
		System.out.println("Se ha recibido evento...");
		if (event instanceof UserRegistrationEvent) {
			UserRegistrationEvent registrationEvent = (UserRegistrationEvent) event;
			createUserRegistrationEmailMessage(registrationEvent.getId(),registrationEvent.getEmail(), registrationEvent.getAppUrl());
		} else if (event instanceof ProviderRegistrationEvent) {
			ProviderRegistrationEvent registrationEvent = (ProviderRegistrationEvent) event;
			createProviderRegistrationEmailMessage(registrationEvent.getId(), registrationEvent.getEmail(), registrationEvent.getAppUrl());
		} else if (event instanceof ResetPasswordEvent) {
			ResetPasswordEvent resetPasswordEvent = (ResetPasswordEvent) event;
			createResetPasswordEmail(resetPasswordEvent.getEmail(), resetPasswordEvent.getAppUrl());
		}
	}
	
	private void createUserRegistrationEmailMessage(int id, String to, String appUrl) {
		String subject = "Confirmación de registro";
		final String message = "Gracias por utilizar OpusClick. Por favor haga click en el enlace que aparece a continuación para confirmar su cuenta.";
		final String confirmationUrl = appUrl + "?verifyCode="+ tokenUtil.createVerificationEmailToken(id,to) +"&type=user";
		String body = message + " \r\n" + confirmationUrl;
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body);
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderRegistrationEmailMessage(int id, String to, String appUrl) {
		String subject = "Confirmación de registro";
		final String message = "Gracias por utilizar OpusClick. Por favor haga click en el enlace que aparece a continuación para confirmar su cuenta.";
		final String confirmationUrl = appUrl + "?verifyCode="+ tokenUtil.createVerificationEmailToken(id,to)+"&type=provider";
		String body = message + " \r\n" + confirmationUrl;
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body);
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createResetPasswordEmail(String to, String appUrl) {
		String subject = "Restablecer Contraseña";
		final String message = "Haz click en el enlace que aparece a continuación para reestablecer tu contraseña.";
		final String confirmationUrl = appUrl + "?email="+to+"&verifyCode="+ tokenUtil.createResetPasswordToken(to);
		String body = message + " \r\n" + confirmationUrl;
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body);
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}	
	
}
