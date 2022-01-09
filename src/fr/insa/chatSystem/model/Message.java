package fr.insa.chatSystem.model;

import java.time.LocalDateTime;
import java.util.Date;
import fr.insa.chatSystem.controller.MainController;

public class Message {
	public String sender;
	public String content;
	public Date time;    
    private LocalDateTime sendTime;

	public Message(String content) {
		this.content = content;
		sender = MainController.username;
		time = new Date();
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
	
    public LocalDateTime getTimeStamp(){
        return this.sendTime;
    }
}