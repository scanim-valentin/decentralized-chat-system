package fr.insa.chatSystem.model;

import java.util.Date;

import fr.insa.chatSystem.controller.MainController;

public class Message {
	public String sender;
	public Date time;
	public String content;

	public Message(String content) {
		this.content = content;
		sender = MainController.username ; 
		time = new Date();  
	}

	public String toString() {
		return "[" + time.toString() + "] " + sender + ": " + content.toString();
	}

}
