package br.com.segala.domotica.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.segala.domotica.components.SerialListner;
import br.com.segala.domotica.enums.SensorEnum;
import br.com.segala.domotica.model.RadioSignal;
import br.com.segala.domotica.utils.StringUtil;

@Service
public class SensorService {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private SerialListner serialListner;
	
	private String lastInput;
	private Long lastNotification = 0l;
	private boolean isArmed = false;
	private boolean isWithSound = false;
	
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
    
    private List<SensorEnum> soundList = new ArrayList<SensorEnum>() {
		private static final long serialVersionUID = 2620548947011846150L;
	{ 
    	add(SensorEnum.CONTROLE_2_S);
    }};
	
	public void sensorTriggered (String input) {
		if (isSpam(input)) {
			return;
		} else {
			lastInput = input;
			lastNotification = System.currentTimeMillis();
		}

		RadioSignal rs = StringUtil.jsonToObject(input, RadioSignal.class);
		SensorEnum sensor= SensorEnum.sensor(rs.getData());
		
		if (isArming(rs)) {
			isArmed = true;
			notificationService.pushNotification(buildMessage());
			
		} else if (isTogglingSound(rs)) {	
			isWithSound = !isWithSound;
			notificationService.pushNotification(buildMessage());
			
		} else if (isDisarming(rs)) {
			isArmed = false;
			isWithSound = false;
			serialListner.write(2);
			notificationService.pushNotification(buildMessage());
			
		} else if (isArmed) {
			if (isWithSound) {
				serialListner.write(1);
			}
			notificationService.notify(sensor);
		}
		
	}
	
	private String buildMessage() {
		StringBuilder message = new StringBuilder();
		message.append("Alarme ");
		if (isArmed) {
			message.append("ativado ");
			message.append(isWithSound ? "com " : "sem ");
			message.append("som.");
		} else {
			message.append("desativado ");
		}
		
		return message.toString();
	}

	private boolean isDisarming(RadioSignal rs) {
		return disarmList.contains(SensorEnum.sensor(rs.getData()));
	}

	private boolean isArming(RadioSignal rs) {
		return armList.contains(SensorEnum.sensor(rs.getData()));
	}
	
	private boolean isTogglingSound(RadioSignal rs) {
		return soundList.contains(SensorEnum.sensor(rs.getData()));
	}

	private boolean isSpam(String input) {
		return input.equals(lastInput) && System.currentTimeMillis() - lastNotification < 10000;
	}

}
