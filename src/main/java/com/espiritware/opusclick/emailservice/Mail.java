package com.espiritware.opusclick.emailservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.espiritware.opusclick.event.GenericEvent;
import com.espiritware.opusclick.event.ProblemEvent;
import com.espiritware.opusclick.event.ProviderAcceptContractEvent;
import com.espiritware.opusclick.event.ProviderCancelWorkEvent;
import com.espiritware.opusclick.event.ProviderModifiesContractEvent;
import com.espiritware.opusclick.event.ProviderRegistrationEvent;
import com.espiritware.opusclick.event.ProviderRequestPaymentEvent;
import com.espiritware.opusclick.event.ProviderVisitAcceptedEvent;
import com.espiritware.opusclick.event.ProviderVisitChangeDateEvent;
import com.espiritware.opusclick.event.ProviderVisitPostponedEvent;
import com.espiritware.opusclick.event.ProviderVisitRejectedEvent;
import com.espiritware.opusclick.event.ProviderVisitUnfulfilledEvent;
import com.espiritware.opusclick.event.QuoteMadeEvent;
import com.espiritware.opusclick.event.ResetPasswordEvent;
import com.espiritware.opusclick.event.ReviewEvent;
import com.espiritware.opusclick.event.UserAuthorizesPaymentEvent;
import com.espiritware.opusclick.event.UserCancelWorkEvent;
import com.espiritware.opusclick.event.UserDenyPaymentEvent;
import com.espiritware.opusclick.event.UserMakesPaymentEvent;
import com.espiritware.opusclick.event.UserModifiesContractEvent;
import com.espiritware.opusclick.event.UserQuotationEvent;
import com.espiritware.opusclick.event.UserRegistrationEvent;
import com.espiritware.opusclick.event.UserVisitAcceptedEvent;
import com.espiritware.opusclick.event.UserVisitChangeDateEvent;
import com.espiritware.opusclick.event.UserVisitPostponedEvent;
import com.espiritware.opusclick.event.UserVisitRejectedEvent;
import com.espiritware.opusclick.event.UserVisitRequestEvent;
import com.espiritware.opusclick.event.UserVisitUnfulfilledEvent;
import com.espiritware.opusclick.model.Contract;
import com.espiritware.opusclick.model.OnlineQuote;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Visit;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.security.TokenService;

@Component
public class Mail implements ApplicationListener<GenericEvent>{

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	@Autowired 
	private TokenService tokenUtil;

	@Value("${app.hostname}")
	private String hostname;
	
	@Value("${support.phone}")
	private String supportPhone;
	
	@Value("${support.emailProblems}")
	private String emailProblems;
	
	@Value("${support.emailNotifications}")
	private String emailNotifications;
	
	@Autowired
    private Environment env;
	
	@Override
	public void onApplicationEvent(GenericEvent event) {
		if (event instanceof UserRegistrationEvent) {
			UserRegistrationEvent registrationEvent = (UserRegistrationEvent) event;
			createUserRegistrationEmailMessage(registrationEvent.getId(), registrationEvent.getEmail(),
					registrationEvent.getName(), registrationEvent.getAppUrl());
		} else if (event instanceof ResetPasswordEvent) {
			ResetPasswordEvent resetPasswordEvent = (ResetPasswordEvent) event;
			createResetPasswordEmail(resetPasswordEvent.getEmail(), resetPasswordEvent.getAppUrl());
		} else if (event instanceof ReviewEvent) {
			ReviewEvent reviewEvent = (ReviewEvent) event;
			createReviewEventNotificationEmail(reviewEvent.getWork());
		} else if (event instanceof UserVisitRequestEvent) {
			UserVisitRequestEvent userVisitRequestEvent = (UserVisitRequestEvent) event;
			createProviderVisitNotificationEmail(userVisitRequestEvent.getVisit());
			createUserVisitReminderEmail(userVisitRequestEvent.getVisit());
		} else if (event instanceof UserQuotationEvent) {
			UserQuotationEvent userQuotationEvent = (UserQuotationEvent) event;
			createProviderQuoteNotificationEmail(userQuotationEvent.getOnlineQuote());
			createUserQuoteReminderEmail(userQuotationEvent.getOnlineQuote());
		} else if (event instanceof UserVisitChangeDateEvent) {
			UserVisitChangeDateEvent userVisitChangeDateEvent = (UserVisitChangeDateEvent) event;
			createUserVisitChangeDateNotificationEmail(userVisitChangeDateEvent.getVisit());
		} else if (event instanceof UserVisitAcceptedEvent) {
			UserVisitAcceptedEvent userVisitAcceptedEvent = (UserVisitAcceptedEvent) event;
			createUserVisitAcceptedNotificationEmail(userVisitAcceptedEvent.getVisit());
			createUserVisitAcceptedReminderEmail(userVisitAcceptedEvent.getVisit());
		} else if (event instanceof UserVisitRejectedEvent) {
			UserVisitRejectedEvent userVisitRejectedEvent = (UserVisitRejectedEvent) event;
			createUserRejectedVisitNotificationEmail(userVisitRejectedEvent.getVisit());
		} else if (event instanceof UserVisitUnfulfilledEvent) {
			UserVisitUnfulfilledEvent userVisitUnfulfilledEvent = (UserVisitUnfulfilledEvent) event;
			createUserVisitUnfulfillNotificationEmail(userVisitUnfulfilledEvent.getVisit());
		} else if (event instanceof UserVisitPostponedEvent) {
			UserVisitPostponedEvent userVisitPostponedEvent = (UserVisitPostponedEvent) event;
			createUserVisitPostponedNotificationEmail(userVisitPostponedEvent.getVisit());
		} else if (event instanceof UserCancelWorkEvent) {
			UserCancelWorkEvent userCancelWorkEvent = (UserCancelWorkEvent) event;
			createUserCancelWorkNotificationEmail(userCancelWorkEvent.getWork());
		} else if (event instanceof UserModifiesContractEvent) {
			UserModifiesContractEvent userModifiesContractEvent = (UserModifiesContractEvent) event;
			createUserModifiesContractNotificationEmail(userModifiesContractEvent.getContract());
		}else if (event instanceof UserMakesPaymentEvent) {
			UserMakesPaymentEvent userMakesPaymentEvent = (UserMakesPaymentEvent) event;
			createUserMakesPaymentNotificationEmail(userMakesPaymentEvent.getContract());
			createUserMakesPaymentReminderEmail(userMakesPaymentEvent.getContract());
		}else if (event instanceof UserAuthorizesPaymentEvent) {
			UserAuthorizesPaymentEvent userAuthorizesPaymentEvent = (UserAuthorizesPaymentEvent) event;
			createUserAuthorizesPaymentNotificationEmail(userAuthorizesPaymentEvent.getContract());
		}else if (event instanceof UserDenyPaymentEvent) {
			UserDenyPaymentEvent userDenyPaymentEvent = (UserDenyPaymentEvent) event;
			createUserDenyPaymentNotificationEmail(userDenyPaymentEvent.getContract());
		}else if (event instanceof ProviderRegistrationEvent) {
			ProviderRegistrationEvent registrationEvent = (ProviderRegistrationEvent) event;
			createProviderRegistrationEmailMessage(registrationEvent.getId(), registrationEvent.getEmail(),
					registrationEvent.getName(), registrationEvent.getAppUrl());
		} else if (event instanceof ProviderVisitChangeDateEvent) {
			ProviderVisitChangeDateEvent providerVisitChangeDateEvent = (ProviderVisitChangeDateEvent) event;
			createProviderVisitChangeDateNotificationEmail(providerVisitChangeDateEvent.getVisit());
		} else if (event instanceof ProviderVisitAcceptedEvent) {
			ProviderVisitAcceptedEvent providerVisitAcceptedEvent = (ProviderVisitAcceptedEvent) event;
			createProviderVisitAcceptedNotificationEmail(providerVisitAcceptedEvent.getVisit());
			createProviderVisitReminderEmail(providerVisitAcceptedEvent.getVisit());
		} else if (event instanceof ProviderVisitRejectedEvent) {
			ProviderVisitRejectedEvent providerVisitRejectedEvent = (ProviderVisitRejectedEvent) event;
			createProviderRejectedVisitNotificationEmail(providerVisitRejectedEvent.getVisit());
		} else if (event instanceof ProviderVisitPostponedEvent) {
			ProviderVisitPostponedEvent providerVisitPostponedEvent = (ProviderVisitPostponedEvent) event;
			createProviderVisitPostponedNotificationEmail(providerVisitPostponedEvent.getVisit());
		} else if (event instanceof ProviderVisitUnfulfilledEvent) {
			ProviderVisitUnfulfilledEvent providerVisitUnfulfilledEvent = (ProviderVisitUnfulfilledEvent) event;
			createProviderVisitUnfulfilledNotificationEmail(providerVisitUnfulfilledEvent.getVisit());
		} else if (event instanceof ProviderCancelWorkEvent) {
			ProviderCancelWorkEvent providerCancelWorkEvent = (ProviderCancelWorkEvent) event;
			createProviderCancelWorkEventNotificationEmail(providerCancelWorkEvent.getWork());
		} else if (event instanceof QuoteMadeEvent) {
			QuoteMadeEvent quoteMadeEvent = (QuoteMadeEvent) event;
			createQuoteMadeNotificationEmail(quoteMadeEvent.getProviderQuote().getWork());
		}else if (event instanceof ProviderAcceptContractEvent) {
			ProviderAcceptContractEvent providerAcceptContractEvent = (ProviderAcceptContractEvent) event;
			createProviderAcceptContractEventNotificationEmail(providerAcceptContractEvent.getContract());
		}else if (event instanceof ProviderModifiesContractEvent) {
			ProviderModifiesContractEvent providerModifiesContractEvent = (ProviderModifiesContractEvent) event;
			createProviderModifiesContractNotificationEmail(providerModifiesContractEvent.getContract());
		}else if (event instanceof ProviderRequestPaymentEvent) {
			ProviderRequestPaymentEvent providerRequestPaymentEvent = (ProviderRequestPaymentEvent) event;
			createProviderRequestPaymentNotificationEmail(providerRequestPaymentEvent.getContract());
		}else if (event instanceof ProblemEvent) {
			ProblemEvent providerRequestPaymentEvent = (ProblemEvent) event;
			createProblemNotificationEmail(providerRequestPaymentEvent.getWork());
		}
	}
	
	@Transactional
	private void createUserRegistrationEmailMessage(int id, String to, String name, String appUrl) {
		String subject = "¡Te damos la bienvenida a OpusClick!";
		StringBuilder body = new StringBuilder();
		final String confirmationUrl = appUrl + "?verifyCode="+ tokenUtil.createVerificationEmailToken(id,to) +"&type=user";
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">¡Bienvenido a OpusClick!</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Estimad@ "+name+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">¡Felicitaciones! Acabas de crear una cuenta en OpusClick,\n" + 
				"       la red en línea más confiable para contratar los mejores expertos para tu hogar.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Te recordamos que en OpusClick tú tienes el poder de elegir \n" + 
				"        el experto más adecuado para resolver tu necesidad, solo ingresa y conoce todos nuestros expertos.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Para comenzar a disfrutar de la experiencia OpusClick, \n" + 
				"        solo debes <strong>VERIFICAR</strong> tu cuenta haciendo click en el enlace que verás a continuación: </p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+confirmationUrl+"\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VERIFICAR</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");

		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderRegistrationEmailMessage(int id, String to, String name, String appUrl) {
		String subject = "¡Te damos la bienvenida a OpusClick!";
		final String confirmationUrl = appUrl + "?verifyCode="+ tokenUtil.createVerificationEmailToken(id,to)+"&type=provider";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">¡Bienvenido a OpusClick!</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Estimad@ "+name+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">¡Felicitaciones! Acabas de crear una cuenta en OpusClick, \n" + 
				"        la red en línea más confiable para las reparaciones y mantenimiento del hogar, aquí se reúnen solo los mejores expertos de la ciudad.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Te recordamos que en OpusClick te conectamos con las personas que requieren \n" + 
				"        de tus servicios como experto, solo ingresa, completa tu perfil y empieza a mejorar tus ingresos.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Para comenzar a disfrutar de la experiencia OpusClick, \n" + 
				"        solo debes <strong>VERIFICAR</strong> tu cuenta haciendo click en el enlace que verás a continuación:</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+confirmationUrl+"\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VERIFICAR</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createResetPasswordEmail(String to, String appUrl) {
		String subject = "Restablecer Contraseña OpusClick";
		final String confirmationUrl = appUrl + "?email="+to+"&verifyCode="+ tokenUtil.createResetPasswordToken(to);
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Restablecer Contraseña</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola Fernando,</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recibimos una solicitud para restablecer tu contraseña OpusClick. \n" + 
				"        Haz click en el botón <strong>Restablecer</strong> elegir una nueva:</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+confirmationUrl+"\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #222222;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">RESTABLECER</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), to, subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}	
	
	private void createProviderVisitNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getUser().getAccount().getName()+" te ha solicitado una visita!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Solicitud de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+
				" "+visit.getWork().getUser().getAccount().getLastname()+"</strong> se ha interesado en tu trabajo y ha solicitado que lo visites, \n" + 
				"        por favor revisa toda la información y confirma tu asistencia para la hora y fecha indicada, si no puedes asistir puedes proponer una nueva fecha \n" + 
				"        de visita o por el contrario cancelarla.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda que tienes un plazo máximo de <strong>2 días</strong> para responder esta solicitud.</p>\n" +
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/visitas/nuevas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER SOLICITUD</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
		emailMessage.setBody("Evento: "+"Solicitud de visita"
		+"Experto: "+ visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"\n"
		+"Contacto: "+visit.getWork().getProvider().getPhone()+"\n");
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderQuoteNotificationEmail(OnlineQuote onlineQuote) {
		String subject = "¡"+onlineQuote.getWork().getUser().getAccount().getName()+" te ha solicitado una cotización!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Solicitud de cotización</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+onlineQuote.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+onlineQuote.getWork().getUser().getAccount().getName()+
				" "+onlineQuote.getWork().getUser().getAccount().getLastname()+"</strong> te ha elegido entre todos los expertos para que realices una cotización, \n" + 
				"        por favor revisa toda la información y hazle una oferta sincera para realizar este trabajo.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda que tienes un plazo máximo de <strong>2 días</strong> para realizar esta cotización.</p>\n" +
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/negociaciones/en_proceso\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER SOLICITUD</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), onlineQuote.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
		emailMessage.setBody("Evento: "+"Solicitud de cotizacion"
		+"Experto: "+ onlineQuote.getWork().getProvider().getAccount().getName()+" "+onlineQuote.getWork().getUser().getAccount().getLastname()+"\n"
		+"Contacto: "+onlineQuote.getWork().getProvider().getPhone()+"\n");
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitReminderEmail(Visit visit) {
		String subject ="Visita en espera de ser aceptada";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recordatorio de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Haz solicitado la visita de código <strong>"+visit.getCode()+
				"</strong> al experto <strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong>, recuerda que \n" + 
				"        debes esperar a que "+visit.getWork().getProvider().getAccount().getName()+" acepte esta solicitud. Una vez aceptada, serás notificado por este medio.</p>\n" + 
				"        <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El número de PIN de seguridad para esta visita es el: <strong>"+visit.getSecurityPin()+"</strong>.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes algún inconveniente, puedes proponer una nueva fecha \n" + 
				"        de visita con <strong>12 horas de anticipación</strong> o por el contrario cancelarla en cualquier momento.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/pendientes\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserQuoteReminderEmail(OnlineQuote onlineQuote){
		String subject ="Solicitud de cotización en espera de ser contestada";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Solicitud de cotización</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+onlineQuote.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Haz solicitado la cotización de código <strong>"+onlineQuote.getQuotationNumber()+
				"</strong> al experto <strong>"+onlineQuote.getWork().getProvider().getAccount().getName()+" "+onlineQuote.getWork().getProvider().getAccount().getLastname()+"</strong>, recuerda que \n" + 
				"        debes esperar a que "+onlineQuote.getWork().getProvider().getAccount().getName()+" elabore la cotización en un plazo máximo de 2 días. Una vez elaborada, serás notificado por este medio.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/negociaciones/en_proceso\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER SOLICITUD</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), onlineQuote.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserRejectedVisitNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getUser().getAccount().getName()+" ha cancelado la visita!";
		StringBuilder body = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currDateString = dateFormat.format(visit.getDate());
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita Cancelada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"</strong> ha cancelado la visita "
						+ "<strong>"+visit.getCode()+"</strong> programada para el día <strong>"+currDateString+"</strong> \n" + 
				"        referente a:</p>\n" + 
				"         <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><i>"+visit.getDescription()+"</i>.</p>\n" + 
				"         <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda estar atento a futuras solicitudes.</p>\n" + 
				"        <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"        <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitPostponedNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getUser().getAccount().getName()+" ha postergado su visita!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita Postergada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"</strong> ha "
						+ "decidido posponer su visita, \n" + 
				"        por lo cual te propone dos nuevas fechas para realizarla, por favor revísalas y elige la que mejor se ajuste a tu disponibilidad.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/visitas/nuevas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color:#294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
		emailMessage.setBody("Evento: "+"Visita postergada"
				+"Experto: "+ visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"\n"
				+"Contacto: "+visit.getWork().getProvider().getPhone()+"\n");
				jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitAcceptedNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getUser().getAccount().getName()+" aceptó la fecha de visita!";
		StringBuilder body = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currDateString = dateFormat.format(visit.getDate());
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita aceptada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+
				"</strong> ha revisado tu propuesta y acepta que lo visites el día:\n" + 
				"         <strong>"+currDateString+"</strong> entre las <strong>"+visit.getLowerLimit()+"</strong> y las <strong>"+visit.getUpperLimit()+"</strong>, por favor revisa toda la información para que estés listo para el día de la visita.</p>\n" + 
				"         <strong style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Es importante que al momento de llegar al sitio del servicio, debes \n" + 
				"           identificarte con el PIN de seguridad asignado para esta visita. De lo contrario, el cliente no permitirá el ingreso a la residencia.</strong>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El número PIN para esta visita es el: <strong>"+visit.getSecurityPin()+"</strong></p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda que cumplir con cada visita aumentará tu reputación en la plataforma.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/visitas/aceptadas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitAcceptedReminderEmail(Visit visit) {
		String subject ="Visita aceptada";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recordatorio de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Haz aceptado la visita de código <strong>"+visit.getCode()+
				"</strong> del experto <strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong>, recuerda \n" + 
				"   	estar en casa el día de la visita.</p>\n" + 
				"      <strong style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Por tu seguridad debes confirmar con el experto en número PIN\n" + 
				"          asignado antes del ingreso a tu residencia.</strong>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El número de PIN de seguridad para esta visita es el: <strong>"+visit.getSecurityPin()+"</strong>.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes algún inconveniente, puedes proponer una nueva fecha \n" + 
				"        de visita con <strong>12 horas de anticipación</strong> o por el contrario cancelarla en cualquier momento.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/pendientes\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderVisitAcceptedNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getProvider().getAccount().getName()+" ha aceptado la visita!";
		StringBuilder body = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currDateString = dateFormat.format(visit.getDate());
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita Aceptada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Se ha confirmado la visita de <strong>"+visit.getWork().getProvider().getAccount().getName()+"\n" +
				" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong>"+
				"        para el día <strong>"+currDateString+"</strong> entre las <strong>"+visit.getLowerLimit()+"</strong> y las <strong>"+visit.getUpperLimit()+"</strong> horas.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda estar en casa estar el día de la visita \n" + 
				"        y ten en cuenta que podrás posponerla <strong>hasta 12 horas</strong> antes de la fecha programada.</p>\n" + 
				"      <strong style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Por tu seguridad debes confirmar con el experto en número PIN\n" + 
				"          asignado antes del ingreso a tu residencia.</strong>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El número PIN para esta visita es el: <strong>"+visit.getSecurityPin()+"</strong></p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Muchas gracias por confiar en nosotros.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/visitas/aceptadas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderVisitReminderEmail(Visit visit) {
		String subject ="Visita Aceptada";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recordatorio de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Haz aceptado la visita de código <strong>"+visit.getCode()+
				"</strong> del cliente <strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"</strong>, recuerda que \n" + 
				"       cumplir las visitas aumenta tu reputación en la plataforma, permitiendo aumentar tus ingresos.</p>\n" + 
				"      <strong style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Es importante que al momento de llegar al sitio del servicio, debes "
				+ "identificarte con el PIN de seguridad asignado para esta visita. De lo contrario, el cliente no permitirá el ingreso a la residencia.</strong>\n" +
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El número de PIN de seguridad para esta visita es el: <strong>"+visit.getSecurityPin()+"</strong>.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes algún inconveniente, puedes proponer una nueva fecha \n" + 
				"        de visita con <strong>12 horas de anticipación</strong> o por el contrario cancelarla en cualquier momento.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_expertos/visitas/aceptadas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserCancelWorkNotificationEmail(Work work) {
		String subject = "¡La negociación ha finalizado! :(";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Negociación Finalizada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+work.getUser().getAccount().getName()+" "+work.getUser().getAccount().getLastname()+"</strong> ha finalizado la negociación "
						+ "<strong>"+work.getWorkNumber()+"</strong></p>" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Lastimosamente no has llegado a un acuerdo con "+work.getUser().getAccount().getName()+", recuerda estar atento a futuras solicitudes.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserModifiesContractNotificationEmail(Contract contract) {
		String subject = "¡Tienes un nuevo contrato!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Contrato modificado</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+contract.getWork().getUser().getAccount().getName()+" "+contract.getWork().getUser().getAccount().getLastname()+
				"</strong> ha definido un contrato para la cotización "+contract.getWork().getProviderQuote().getNumber()+", por favor revisa todos los términos en detalle."+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si estás de acuerdo puedes aceptar el contrato para iniciar el trabajo, de lo contrario puedes modificar o añadir algunas condiciones, sujetas a aceptación por parte del cliente.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/negociaciones/en_proceso\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserMakesPaymentNotificationEmail(Contract contract) {
		String subject = "¡Felicitaciones! Has sido contratado";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Nuevo Contrato</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Has sido elegido por <strong>"+contract.getWork().getUser().getAccount().getName()+" "+contract.getWork().getUser().getAccount().getLastname()+
				"</strong> para ejecutar el contrato <strong>"+contract.getContractNumber()+"</strong>;"+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">El tiempo de entrega inicia a partir del siguiente día hábil a este comunicado o según como se haya definido en el contrato.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerde que de su calidad y cumplimiento en el trabajo dependerá su reputación en la web para futuras contrataciones.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/negociaciones/concretadas\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
		emailMessage.setBody("Evento: "+"Pago a experto. Iniciar trabajo"
				+"Experto: "+ contract.getWork().getProvider().getAccount().getName()+" "+contract.getWork().getUser().getAccount().getLastname()+"\n"
				+"Contacto: "+contract.getWork().getProvider().getPhone()+"\n");
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserMakesPaymentReminderEmail(Contract contract) {
		String subject = "¡Se ha confirmado tu pago!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Confirmación de pago</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola <strong>"+contract.getWork().getUser().getAccount().getName()+"</strong>,</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Le informamos que el pago realizado en OpusClick para el contrato "+contract.getContractNumber()+" ha sido aprobado.El trabajo entrará en ejecución a partir del siguiente día hábil."+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Queremos agradecerte por hacer parte de esta nueva manera de contratar servicios.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/negociaciones/concretadas\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserAuthorizesPaymentNotificationEmail(Contract contract){
		String subject = "¡Tu pago ha sido aprobado!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Autorización de pago</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+contract.getWork().getUser().getAccount().getName()+" "+contract.getWork().getUser().getAccount().getLastname()+
				"</strong> ha aprobado el pago para el contrato <strong>"+contract.getContractNumber()+"</strong>;"+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">OpusClick procederá a realizar el respectivo desembolso que se verá reflejado en tu cuenta bancaria de acuerdo con los plazos establecidos en nuestra política de pagos.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">¡Muchas gracias por la calidad y cumplimiento de su trabajo!</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/negociaciones/concretadas\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserDenyPaymentNotificationEmail(Contract contract) {
		String subject = "¡Tu pago ha sido denegado!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Denegación de pago</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+contract.getWork().getUser().getAccount().getName()+" "+contract.getWork().getUser().getAccount().getLastname()+
				"</strong> ha denegado el pago para el contrato <strong>"+contract.getId()+"</strong>;"+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">OpusClick procederá a realizar un seguimiento del caso para resolver este inconveniente.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/negociaciones/concretadas\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserWorkRejectedNotificationEmail(Work work) {
		String subject = "¡La negociación ha finalizado! :(";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Negociación Finalizada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+work.getUser().getAccount().getName()+" "+work.getUser().getAccount().getLastname()+"</strong> ha finalizado la negociación "
						+ "<strong>"+work.getWorkNumber()+"</strong></p>" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Lastimosamente no has llegado a un acuerdo con "+work.getUser().getAccount().getName()+", recuerda estar atento a futuras solicitudes.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderRejectedVisitNotificationEmail(Visit visit) {
		String subject = "¡"+visit.getWork().getProvider().getAccount().getName()+" ha cancelado la visita!";
		StringBuilder body = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currDateString = dateFormat.format(visit.getDate());
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita Cancelada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong> ha cancelado la visita "
						+ "<strong>"+visit.getCode()+"</strong> programada para el día <strong>"+currDateString+"</strong> \n" + 
				"        referente a:</p>\n" + 
				"         <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><i>"+visit.getDescription()+"</i>.</p>\n" + 
				"         <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Esto ocurre cuando por alguna razón de fuerza mayor "
				+ 			"el experto no puede realizar la visita.</p>\n" + 
				"        <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si lo deseas, puedes eligir a otro experto y solicitar una nueva visita.</p>\n" + 
				"        <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderVisitPostponedNotificationEmail(Visit visit) {
		String subject = "El experto propone visitarte en otra fecha";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Visita Pospuesta</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong> "
				+ "ya no puede visitarte en la fecha acordada, sin embargo, ha propuesto dos fechas alternativas para realizar la visita, por favor revísalas y elige la que mejor se ajuste a tu tiempo.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si no es posible recibir la visita del experto en ninguna de las fechas propuestas por él,"
				+ " recuerda que puedes proponerle otras nuevas fechas para realizar la visita o por el contrario cancelarla.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/pendientes\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color:#008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderVisitChangeDateNotificationEmail(Visit visit) {
		String subject = "El experto propone visitarte en otra fecha";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Propuesta de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+"</strong> "
				+ "no puede visitarte en la fechas que le haz solicitado, sin embargo, ha propuesto dos fechas alternativas para realizar la visita, por favor revísalas y elige la que mejor se ajuste a tu tiempo.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si no es posible recibir la visita del experto en ninguna de las fechas propuestas por él,"
				+ " recuerda que puedes proponerle otras nuevas fechas para realizar la visita o por el contrario cancelarla.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/nuevas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color:#008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitChangeDateNotificationEmail(Visit visit) {
		String subject = visit.getWork().getUser().getAccount().getName()+" desea que lo visites en otra fecha";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Propuesta de visita</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"</strong> "
				+ "no puede recibir la visita en la fechas que le has propuesto, sin embargo, ha dispuesto dos fechas alternativas para que puedas realizar la visita, por favor revísalas y elige la que mejor se ajuste a tu tiempo.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si no es posible recibir la visita del experto en ninguna de las fechas propuestas por él,"
				+ " recuerda que puedes proponerle otras nuevas fechas para realizar la visita o por el contrario cancelarla.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/nuevas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color:#294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER VISITA</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderVisitUnfulfilledNotificationEmail(Visit visit) {
		String subject = visit.getWork().getUser().getAccount().getName()+" ha reportado un incumplimiento";
		StringBuilder body = new StringBuilder();
		int begin=visit.getBreachDescription().indexOf(" &Cliente: ");
		int end=visit.getBreachDescription().indexOf('@');
		String reason="";
		if(begin>end) {
			reason=visit.getBreachDescription().substring(begin+10, visit.getBreachDescription().lastIndexOf('@'));
		}else {
			reason=visit.getBreachDescription().substring(begin+10, visit.getBreachDescription().indexOf('@'));
		}
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Reporte de incumplimiento</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getUser().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+
				"</strong> ha reportado el incumplimiento de la visita <strong>"+visit.getCode()+"</strong> referente a:</p>\n" + 
				"      <i style=\"font-family: 'Open Sans', sans-serif; color: #202020\">"+visit.getDescription()+"</i>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">La causa por la cual se reportó el incumplimiento fué:</p>\n" + 
				"      <i style=\"font-family: 'Open Sans', sans-serif; color: #202020\">"+reason+"</i>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Ten en cuenta que el hecho de incumplir reiteradamente las visitas puede causar la salida de tu perfil en la plataforma.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si no has incumplido la visita, puedes hacer clic en el botón que aparece a continuación, y reportar un incumplimiento.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_experto/visitas/incumplidas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #294664;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">REPORTAR INCUMPLIMIENTO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createUserVisitUnfulfillNotificationEmail(Visit visit) {
		String subject = visit.getWork().getProvider().getAccount().getName()+" ha reportado un incumplimiento";
		StringBuilder body = new StringBuilder();
		int begin=visit.getBreachDescription().indexOf(" &Experto: ");
		int end=visit.getBreachDescription().indexOf('@');
		String reason="";
		if(begin>end) {
			reason=visit.getBreachDescription().substring(begin+10, visit.getBreachDescription().lastIndexOf('@'));
		}else {
			reason=visit.getBreachDescription().substring(begin+10, visit.getBreachDescription().indexOf('@'));
		}
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Reporte de incumplimiento</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+visit.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getProvider().getAccount().getLastname()+
				"</strong> ha reportado el incumplimiento de la visita <strong>"+visit.getCode()+"</strong> referente a:</p>\n" + 
				"      <i style=\"font-family: 'Open Sans', sans-serif; color: #202020\">"+visit.getDescription()+"</i>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">La causa por la cual se reportó el incumplimiento fué:</p>\n" + 
				"      <i style=\"font-family: 'Open Sans', sans-serif; color: #202020\">"+reason+"</i>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Ten en cuenta que si vas a solicitar otra visita, debes realizar el pago por esta.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si no has incumplido la visita, puedes hacer clic en el botón que aparece a continuación, y reportar un incumplimiento.</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/visitas/incumplidas\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">REPORTAR INCUMPLIMIENTO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), visit.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
		emailMessage.setBody("Evento: "+"Incumplimiento de experto"
				+"Experto: "+ visit.getWork().getProvider().getAccount().getName()+" "+visit.getWork().getUser().getAccount().getLastname()+"\n"
				+"Contacto: "+visit.getWork().getProvider().getPhone()+"\n");
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderCancelWorkEventNotificationEmail(Work work) {
		String subject = "¡La negociación ha finalizado! :(";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Negociación Finalizada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+work.getProvider().getAccount().getName()+" "+work.getProvider().getAccount().getLastname()+"</strong> ha finalizado la negociación "
						+ "<strong>"+work.getWorkNumber()+"</strong></p>" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Lastimosamente no has llegado a un acuerdo con "+work.getProvider().getAccount().getName()+", te recomendamos contactar a otro experto que pueda cumplir con tus requerimientos.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderWorkRejectedNotificationEmail(Work work) {
		String subject = "¡La negociación ha finalizado! :(";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Negociación Finalizada</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+work.getProvider().getAccount().getName()+" "+work.getProvider().getAccount().getLastname()+"</strong> ha terminado la negociación "
						+ "<strong>"+work.getWorkNumber()+"</strong></p>" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Lastimosamente no has llegado a un acuerdo con "+work.getProvider().getAccount().getName()+", te recomendamos contactar a otro experto que pueda cumplir con tus requerimientos.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createQuoteMadeNotificationEmail(Work work) {
		String subject = "!"+work.getProvider().getAccount().getName()+" ha realizado una cotización!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Cotización</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\"><strong>"+work.getProvider().getAccount().getName()+" "+work.getProvider().getAccount().getLastname()+"</strong> "
						+ "ha dado respuesta a la solicitud de cotización que realizaste, por favor ingresa a la plataforma y revisa su propuesta en detalle."+
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda que puedes definir el contrato para iniciar este trabajo o por el contrario rechazar la propuesta.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"	<a href=\""+hostname+"/dashboard_usuario/negociaciones/en_proceso\" style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER COTIZACIÓN</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderAcceptContractEventNotificationEmail(Contract contract) {
		String subject = "¡Contrato aceptado!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Contrato Aceptado</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Le informamos que el contrato "+contract.getContractNumber()+" ha sido aprobado por <strong>"+contract.getWork().getProvider().getAccount().getName()
				+" "+contract.getWork().getProvider().getAccount().getLastname()+ "</strong> experto en <strong>"
				+contract.getWork().getProvider().getProfession().getName()+"</strong>"+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Ingresa a la plataforma para que puedas realizar el pago y comenzar con el trabajo propuesto.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/negociaciones/en_proceso\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	
	private void createProviderModifiesContractNotificationEmail(Contract contract) {
		String subject = "!"+contract.getWork().getProvider().getAccount().getName()+" ha modificado el contrato!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Modificación de contrato</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Le informamos que el contrato "+contract.getId()+" ha sido modificado por "
				+contract.getWork().getProvider().getAccount().getName()+" "+contract.getWork().getProvider().getAccount().getLastname()+" experto en "+contract.getWork().getProvider().getProfession().getName()+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Por favor revise las condiciones que fueron modificadas, si está de acuerdo puede aceptar, de lo contrario puede volver a modificar o añadir información en el contrato, sujeta a aprobación por parte del experto.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerde que nos basaremos exclusivamente en el contrato ante futuras reclamaciones.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/negociaciones/en_proceso\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createProviderRequestPaymentNotificationEmail(Contract contract) {
		String subject = "Autorización de pago";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Autorización de pago</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+contract.getWork().getUser().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Tienes una autorización de pago pendiente para el contrato "+contract.getContractNumber()+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si el experto culminó de manera correcta el trabajo, autorízanos para proceder con el pago, de lo contrario indícanos las razones de tu inconformidad para que el experto se encargue.</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Si tienes alguna pregunta no dudes en contactarnos "+supportPhone+".</p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  <div style=\"display: flex; margin: 0 auto; text-align: justify;\">\n" + 
				"<a href=\""+hostname+"/dashboard_usuario/negociaciones/concretadas\" "+"style=\"font-family: 'Open Sans', sans-serif;\n" + 
				"    display: flex; margin: 0 auto;\n" + 
				"    background-color: #008d98;\n" + 
				"    border: none;\n" + 
				"    color: white;\n" + 
				"    padding: 15px 32px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\">VER CONTRATO</a>\n" + 
				"  </div>\n" + 
				"  <div style=\"background-color: #fafafa; height:100px;\"></div>\n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), contract.getWork().getUser().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
	
	private void createReviewEventNotificationEmail(Work work){
		String subject = "¡Te han calificado!";
		StringBuilder body = new StringBuilder();
		body.append("<!DOCTYPE HTML>\n" + 
				"<html>\n" + 
				"  <head>\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\">\n" + 
				"  </head>\n" + 
				"  <body style=\"background-color: #ffffff; padding-left: 30px; padding-right: 30px;\">\n" + 
				"    <div style=\"background-color: #fafafa;\">\n" + 
				"      <div style=\"background-color: #fafafa; margin:auto;\">\n" + 
				"        <img style=\"display: table; margin: 0 auto; background-color: #fafafa;\" src=\"https://s3.amazonaws.com/opusclick.com/assets/OpusClickLogo.png\">\n" + 
				"      </div>\n" + 
				"    <div style=\"display: table; margin: 0 auto; color: #202020\" >\n" + 
				"      <h1 style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Nueva calificación</h1>\n" + 
				"    </div>\n" + 
				"    <div style=\"display: inline-block; margin: 0 auto; text-align: justify; padding-left: 30px; padding-right: 30px;\" >\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Hola "+work.getProvider().getAccount().getName()+",</p>\n" + 
				"      <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Has recibido una nueva calificación por <strong>"+work.getUser().getAccount().getName()+" "+work.getUser().getAccount().getLastname()+
				"</strong> relativa al trabajo realizado en el contrato <strong>"+work.getContract().getContractNumber()+"</strong>:"+
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Nivel de satisfacción con el trabajo que realizaste: <strong>"+work.getReview().getSatisfactionLevel()+"%"+"</strong></p>\n" + 
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Recuerda que tu reputación en la plataforma es resultado del promedio de todas las calificaciones recibidas, procura mantener una muy buena calificación para que te sigan contratando.</p>\n" + 				
				"       <p style=\"font-family: 'Open Sans', sans-serif; color: #202020\">Equipo OpusClick.</p>\n" + 
				"      <br> \n" + 
				"  </div>\n" + 
				"  </body>\n" + 
				"</html>");
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), work.getProvider().getAccount().getEmail(), subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
		emailMessage.setTo(emailNotifications);
	}
	
	private void createProblemNotificationEmail(Work work){
		String subject = new String();
		StringBuilder body = new StringBuilder();
		body.append("<p>IdNegociación : "+work.getWorkNumber()+"</p>\n");
		body.append("<p>IdUsuario : "+work.getUser().getId()+"</p>\n");
		body.append("<p>Contacto : "+work.getUser().getPhone()+"</p>\n");
		body.append("<p>IdExperto : "+work.getProvider().getId()+"</p>\n");
		body.append("<p>Contacto : "+work.getProvider().getPhone()+"</p>\n");
		if(work.getState().equals(State.PARTIALLY_FINISHED)) {
			subject = "Experto dice que ha resuelto un inconveniente "+ work.getWorkNumber();
		}else if(work.getState().equals(State.CANCELLED_BY_USER)) {
			subject = "Usuario ha cancelado negociación "+ work.getWorkNumber();
			body.append("<p>Causa : "+work.getComment()+"</p>\n");
		}else if(work.getState().equals(State.CANCELLED_BY_PROVIDER)) {
			subject = "Experto ha cancelado negociación "+ work.getWorkNumber();
			body.append("<p>Causa : "+work.getComment()+"</p>\n");
		}else if(work.getState().equals(State.DENIED)) {
			subject = "Se ha denegado un pago "+ work.getWorkNumber();
		}
		
		EmailMessage emailMessage = new EmailMessage(env.getProperty("support.email"), emailProblems, subject, body.toString());
		jmsTemplate.convertAndSend("mailbox",emailMessage);
	}
}
