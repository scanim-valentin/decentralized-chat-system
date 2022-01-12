package fr.insa.chatSystem.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fr.insa.chatSystem.controller.MainController;

public class Message {
	private String sender;
	private String content;
	private LocalDateTime time = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
	String timeText = time.format(formatter);

	public Message(String content) {
		this.content = content;
		sender = MainController.username;
	}

	public String toString() {
		return "[" + timeText + "] " + sender + ": " + content.toString();
	}

	public String getSender() {
		return this.sender;
	}

	public String getText() {
		return this.content;
	}

	public LocalDateTime getTime() {
		return time;
	}

}