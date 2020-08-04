package br.com.segala.domotica.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import br.com.segala.domotica.components.Email;
import br.com.segala.domotica.components.Firebase;
import br.com.segala.domotica.enums.SensorEnum;
import br.com.segala.domotica.utils.DateUtil;

@Service
public class NotificationService {

	@Autowired
	private Environment env;
	
	@Autowired
	private Email email;
	
	@Autowired
	private Firebase push;
	
	public void pushNotification(String message) {
		push.pushNotification(message, env.getProperty("push.notification.list").split(";"));
	}
	
	public void sendEmail(String message) {
		email.sendSimpleMail(message, env.getProperty("email.notification.list").split(";"));
	}
	
	public void notify(SensorEnum sensor) {
		StringBuilder message = new StringBuilder();
		message.append(sensor.getDescription());
		message.append(" em ");
		message.append(DateUtil.format(new Date()));
		
		pushNotification(message.toString());

		sendEmail(message.toString());
	}
}
