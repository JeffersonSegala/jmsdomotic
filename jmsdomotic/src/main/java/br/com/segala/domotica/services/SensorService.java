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
	private boolean isWithSound = true;
	
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
		SensorEnum sensor = SensorEnum.sensor(rs.getData());
		if (isUnknownSensor(sensor)) {
			return;
		}
		
		if (isArming(sensor)) {
			isArmed = true;
			notificationService.pushNotification(buildMessage());
			
		} else if (isTogglingSound(sensor)) {	
			isWithSound = !isWithSound;
			notificationService.pushNotification(buildMessage());
			
		} else if (isDisarming(sensor)) {
			isArmed = false;
			isWithSound = false;
			deactivateAudubleAlarm();
			notificationService.pushNotification(buildMessage());
			
		} else if (isArmed) {
			if (isWithSound) {
				activateAdibleAlarm();
			}
			notificationService.notify(sensor);
		}
		
	}

	private boolean isUnknownSensor(SensorEnum sensor) {
		return sensor == null;
	}

	private void deactivateAudubleAlarm() {
		serialListner.write(2);
	}

	private void activateAdibleAlarm() {
		serialListner.write(1);
	}
	
	private String buildMessage() {
		StringBuilder message = new StringBuilder();
		message.append("Alarme ");
		if (isArmed) {
			message.append(isWithSound ? "peew peew peew " : "silencioso ");
			message.append("ativado ");
		} else {
			message.append("desativado ");
		}
		
		return message.toString();
	}

	private boolean isDisarming(SensorEnum sensor) {
		return disarmList.contains(sensor);
	}

	private boolean isArming(SensorEnum sensor) {
		return armList.contains(sensor);
	}
	
	private boolean isTogglingSound(SensorEnum sensor) {
		return soundList.contains(sensor);
	}

	private boolean isSpam(String input) {
		return input.equals(lastInput) && System.currentTimeMillis() - lastNotification < 10000;
	}

}
