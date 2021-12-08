package fr.insa.chatSystem.model;
import java.util.ArrayList;
import java.util.List;

public class ChattingSession extends Thread {
	
	private UserID other_user ; //Other participant to the conversation
	
	private List<Message> message_list = new ArrayList<Message>() ;  //List of messages (history)
	
	public ChattingSession(UserID other_user, String threadname) {
		super(threadname);
		this.other_user = other_user ; 
	}
	
	private void debugPrint(String str) {
		System.out.println("["+Thread.currentThread().getName()+"] ChattingSession: "+str);
	}
	
	public void run() {
		
	}
	
}
