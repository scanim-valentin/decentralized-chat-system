package fr.insa.chatSystem.model;

import java.sql.Timestamp;

public class Message {

	private String sender;
	private String textMessage;
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public Message(String textMessage, String sender) {
		this.textMessage = textMessage;
		this.sender = sender;
	}

	public Message(String textMessage, String sender, Timestamp timestamp) {
		this.textMessage = textMessage;
		this.sender = sender;
		this.timestamp = timestamp;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public String toString() {
		return "[" + timestamp.toString() + "] " + sender + ": " + textMessage.toString() + "\n";
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