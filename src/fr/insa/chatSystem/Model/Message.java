package fr.insa.chatSystem.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fr.insa.chatSystem.controller.MainController;

public class Message {
	private String sender;
	private String textMessage;
	private LocalDateTime time = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
	String timeText = time.format(formatter);

	public Message(String textMessage, String sender) {
		this.textMessage = textMessage;
		sender = MainController.username;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public String toString() {
		return "[" + time + "] " + sender + ": " + textMessage.toString();
	}

	public String getSender() {
		return this.sender;
	}

	public String getText() {
		return this.textMessage;
	}

	public LocalDateTime getTime() {
		return time;
	}

}