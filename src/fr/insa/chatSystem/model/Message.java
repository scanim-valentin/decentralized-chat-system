package fr.insa.chatSystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fr.insa.chatSystem.controller.MainController;

public class Message {
	private String sender;
	private String textMessage;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public Message(String textMessage, String sender) {
		this.textMessage = textMessage;
		sender = MainController.username;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public String toString() {
		return "[" + timestamp.toString() + "] " + sender + ": " + textMessage.toString();
	}

	public String getSender() {
		return this.sender;
	}

	public String getText() {
		return this.textMessage;
	}

	public Timestamp getTime() {
		return timestamp;
	}

}