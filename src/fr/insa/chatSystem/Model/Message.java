package fr.insa.chatSystem.Model;

import java.time.LocalDateTime;
import fr.insa.chatSystem.controller.MainController;

public class Message {
	private String sender;
	private String content;
	private LocalDateTime time;

	public Message(String content) {
		this.content = content;
		sender = MainController.username;
		time = java.time.LocalDateTime.now();
	}

	public String toString() {
		return "[" + time.toString() + "] " + sender + ": " + content.toString();
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