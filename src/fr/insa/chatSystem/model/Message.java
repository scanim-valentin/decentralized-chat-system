package fr.insa.chatSystem.model;

import java.util.Date;
import fr.insa.chatSystem.controller.MainController;


public class Message {
	public String sender;
	public Date time;
	public String content;
	private UserID Usersender;
    private String text;
    
	public Message(String content) {
		this.content = content;
		sender = MainController.username ; 
		time = new Date();  
	}

	public String toString() {
		return "[" + time.toString() + "] " + Usersender + ": " + content.toString();
	}
    
    public void Messages(UserID Usersender, String text){
        this.Usersender = Usersender;
        this.text = text;
    }
    
    public UserID getSender(){ 
    	return this.Usersender; 
    	}
    
    public String getText(){ 
    	return this.text; 
    	}
}