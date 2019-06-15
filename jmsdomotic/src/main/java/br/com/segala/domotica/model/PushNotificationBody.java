package br.com.segala.domotica.model;

public class PushNotificationBody {

	private Notification notification;
	private String to;

	public PushNotificationBody(String title, String body, String to) {
		this.notification = new Notification(title, body);
		this.to = to;
	}
	
	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
