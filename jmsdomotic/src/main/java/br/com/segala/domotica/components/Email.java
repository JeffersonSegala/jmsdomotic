package br.com.segala.domotica.components;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Email {
	
	@Autowired
	private Environment env;
	
	public void sendSimpleMail(String message, String... toAdress) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(env.getProperty("email.smtp.host"));
			email.addTo(toAdress);
			email.setFrom(env.getProperty("email.from.email"), env.getProperty("email.from.name"));
			email.setSubject(env.getProperty("email.subject"));
			email.setMsg(message);
			email.setAuthentication(env.getProperty("email.auth.user"), env.getProperty("email.auth.pass"));
			email.setSmtpPort(env.getProperty("email.smtp.port", Integer.class));
			email.setSSLOnConnect(true);
			email.setStartTLSEnabled(true);
			email.send();	
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}
