import java.util.Date ; 

public class Message {
	public String sender ; 
	public Date time ; 
	public String content ; 
	
	
	public Message(String pseudo, Date time, String content) {
		this.sender = pseudo ; 
		this.time = time ; 
		this.content = content ; 
	}
	
	public String toString() {
		return "["+time.toString()+"] "+sender+": "+content.toString();
	}
}
