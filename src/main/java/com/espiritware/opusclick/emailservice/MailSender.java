package com.espiritware.opusclick.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

	@Autowired
	private JavaMailSender mailSender;
	
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	 public void sendEmail(EmailMessage emailMessage) {
		System.out.println("Llega e-mail");
		final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(emailMessage.getFrom());
        email.setTo(emailMessage.getTo());
        email.setSubject(emailMessage.getSubject());
        email.setText(emailMessage.getBody());
		mailSender.send(email);
	 }
}
