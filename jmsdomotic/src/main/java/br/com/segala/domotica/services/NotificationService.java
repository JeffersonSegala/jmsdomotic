package br.com.segala.domotica.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import br.com.segala.domotica.components.Email;
import br.com.segala.domotica.components.Firebase;
import br.com.segala.domotica.enums.SensorEnum;
import br.com.segala.domotica.model.RadioSignal;
import br.com.segala.domotica.utils.DateUtil;
import br.com.segala.domotica.utils.StringUtil;

@Service
public class NotificationService {

	@Autowired
	private Environment env;
	
	@Autowired
	private Email email;
	
	@Autowired
	private Firebase push;
	
	private String lastInput;
	private Long lastNotification = 0l;
	private boolean isArmed = false;
	
	private List<SensorEnum> armList = new ArrayList<SensorEnum>() {
		private static final long serialVersionUID = -8947393649362123297L;
	{ 
        add(SensorEnum.CONTROLE_1_A); 
        add(SensorEnum.CONTROLE_2_A); 
    }};
    
    private List<SensorEnum> disarmList = new ArrayList<SensorEnum>() {
		private static final long serialVersionUID = 2620548947011846150L;
	{ 
    	add(SensorEnum.CONTROLE_1_D); 
    	add(SensorEnum.CONTROLE_2_D); 
    }}; 
	
	public void sensorTriggered (String input) {
		if (isSpam(input)) {
			return;
		}

		lastInput = input;
		lastNotification = System.currentTimeMillis();

		RadioSignal rs = StringUtil.jsonToObject(input, RadioSignal.class);
		
		if (arming(rs)) {
			isArmed = true;
			push.pushNotification("Alarme ativado", env.getProperty("push.notification.list").split(";"));
		} else if (disarming(rs)) {
			isArmed = false;
			push.pushNotification("Alarme desativado", env.getProperty("push.notification.list").split(";"));
		} else if (isArmed) {
			notify(rs);
		}
		
	}

	private boolean disarming(RadioSignal rs) {
		return disarmList.contains(SensorEnum.sensor(rs.getReceived()));
	}

	private boolean arming(RadioSignal rs) {
		return armList.contains(SensorEnum.sensor(rs.getReceived()));
	}

	private boolean isSpam(String input) {
		return input.equals(lastInput) && System.currentTimeMillis() - lastNotification < 10000;
	}

	private void notify(RadioSignal rs) {
		StringBuilder message = new StringBuilder();
		message.append(SensorEnum.sensor(rs.getReceived()).getDescription());
		message.append(" em ");
		message.append(DateUtil.format(new Date()));
		
		System.out.println("Sending email...");
		email.sendSimpleMail(message.toString(), env.getProperty("email.notification.list").split(";"));
		
		System.out.println("Pushing notification...");
		push.pushNotification(message.toString(), env.getProperty("push.notification.list").split(";"));
	}
}
