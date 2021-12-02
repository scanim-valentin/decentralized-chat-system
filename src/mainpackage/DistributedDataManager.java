package mainpackage;

import java.util.List ;
import java.net.* ; 
import java.io.* ; 
import javax.swing.* ; 

public abstract class DistributedDataManager {
	
	public List<UserID> retrieveUserList() {
		List<UserID> userlist = null;
		boolean done = false ; 
		UDPBroadcast_IDRequest() ; 
		while(!done) {
			if() {
				//do something
			}
		}
		return userlist ; 
	}
	
	private void UDPBroadcast_IDRequest() {
		
	}
	
	private void UDPBroadcast_NotifyConnection(String Name) {
		
	}
	
	private void UDPBroadcast_NotifyDisconnection(String Name) {
		
	}

	
	
}
