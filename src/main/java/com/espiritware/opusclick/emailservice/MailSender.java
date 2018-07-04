package com.espiritware.opusclick.emailservice;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	 public void sendEmail(EmailMessage emailMessage) throws MessagingException {
		System.out.println("Llega e-mail");
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(emailMessage.getFrom());
        helper.setTo(emailMessage.getTo());
        helper.setSubject(emailMessage.getSubject());
        helper.setText(emailMessage.getBody(), true);
        /*ClassPathResource file = new ClassPathResource("OpusClickLogo.png");
		helper.addInline("id101", file);*/
        mailSender.send(message);
        
        /*
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("set-your-recipient-email-here@gmail.com");
        helper.setText("<html><body>Here is a cat picture! <img src='cid:id101'/><body></html>", true);
        helper.setSubject("Hi");
        ClassPathResource file = new ClassPathResource("cat.jpg");
        helper.addInline("id101", file);
        sender.send(message);
        */
        
        /*final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(emailMessage.getFrom());
        email.setTo(emailMessage.getTo());
        email.setSubject(emailMessage.getSubject());
        email.setText(emailMessage.getBody());
		mailSender.send(email);*/
	 }
}
