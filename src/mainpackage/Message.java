package mainpackage;
import java.util.Date ; 

public class Message {
	public String sender ; 
	public Date time ; 
	public String content ; 
	
	
	public Message(String username, Date time, String content) {
		this.username = username ; 
		this.time = time ; 
		this.content = content ; 
	}
	
	public String toString() {
		return "["+Date.toString+"] "+sender+": "+content.toString();
	}
}
