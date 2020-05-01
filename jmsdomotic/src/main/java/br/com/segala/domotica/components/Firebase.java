package br.com.segala.domotica.components;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.segala.domotica.model.PushNotificationBody;

@Component
public class Firebase {

	@Autowired
	private Environment env;

	public void pushNotification(String message, String... toAdress) {
		if ( toAdress.length == 0 ) {
			return;
		}
		
		System.out.println("Pushing notification...");
		for (String to : toAdress) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				final String baseUrl = "https://fcm.googleapis.com/fcm/send";
				URI uri = new URI(baseUrl);
				PushNotificationBody body = new PushNotificationBody("Alarme de casa", message, to);
	
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "key=" + env.getProperty("firebase.server.key"));
				headers.set("Content-Type", "application/json");
	
				HttpEntity<PushNotificationBody> request = new HttpEntity<>(body, headers);
	
				ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
	
				System.out.println("Push notification: " + result.getStatusCodeValue());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
