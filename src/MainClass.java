import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainClass {
	
	static public List<UserID> userlist = new ArrayList<UserID>() ; //List of users to fill with other UsersID
	
	static public String username = "" ; 
	
	//Command prompt
	static public String input = "" ; 
	static final public String EXIT_IN = "exit" ; 
	static final public String NEWNAME_IN = "newname" ; 
	static final public String CHATWITH_IN = "newchat" ;
	
	
	
	static private void debugPrint(String str) {
		System.out.println("MainClass: "+str);
	}
	
	/*Checks if a username is valid, i.e.: 
	* - it is not a blank string 
	* - it does not contain a forbidden character
	* - this username is available 
	*/ 
	static private boolean isValid(String S) {
		
		if(S.isBlank() || S.contains(DistributedDataManager.SEP))
			return false ; 
		
		for(UserID id: userlist) {
			if(id.getName().equals(S)) 
				return false ; 
		}
		
		return true ; 
	}
	
	private static void wait(int ms) {
		try {
			Thread.sleep(ms);
		}catch(InterruptedException ex){
		    Thread.currentThread().interrupt();
		}
	}
	
	//Waits for an input, checks the username validity and changes username if it is valid
	//If it is valid, changes username attributes and asks the DistributedDataManager to notify everyone on the network
	private static void changeUsername() {
		BufferedReader reader = new BufferedReader(
	            new InputStreamReader(System.in));
		String name_input = "" ;
        try {
	        // Reading data using readLine
	        boolean valid = false ; 
        	while(!valid) {
        		debugPrint("Enter a valid username: ");
        		name_input = reader.readLine();
	        	if(isValid(name_input)) {
	        		valid = true ; 
	        	} else {
	        		debugPrint("Invalid input! Empty string or contains forbidden character "+DistributedDataManager.SEP) ; 
	        	}
	        }
	    }catch(Exception E) {
	    	E.printStackTrace();
	    }
        //Notifying everyone on the local network 
        if(username.isEmpty()) {
        	username = name_input ;
        	DistributedDataManager.notifyConnection();
        }else {
        	DistributedDataManager.notifyNewName(name_input);
        	username = name_input ; 
        }
        
        //notify everyone... TODO
        debugPrint("Set new name to "+username);
	}
	
	//Prints the remaning time every second
	private static void formatedDelay(int delay) {
		for(int i = 0 ; i < delay ; i++) {
			wait(1000);
			debugPrint((delay-i)+" seconds remaining . . . ") ; 
		}
	}
	
	public static void main(String[] s) {
		
		debugPrint("Starting DDM deamon . . .") ; 
		DistributedDataManager.start_deamon();
		
		//Waiting to gather all of the user id
		debugPrint("Waiting for local ID request answer");
		
		//Waits 5 seconds to gather all of the usernames from the local network 
		formatedDelay(5); 
		
		//Asks the user to choose a valid username
		changeUsername(); 
		
		// Enter data using BufferReader
		debugPrint("Please enter a command.");
		boolean close = false ; 
		while(!close) {
			//Command prompt
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        try {
	        	input = reader.readLine() ; 
	        	switch(input) {
	        		case EXIT_IN : 
	        			close = true ;
	        			debugPrint("Closing agent . . .") ; 
	        			break;
	        		case NEWNAME_IN :
	        			changeUsername();
	        			break;
	        		default : 
	        			debugPrint("Unidentified input. \n exit : close the agent\n newname : change username\n chat : start chatting session");
	        	}
	        }catch(Exception E) {
	        	E.printStackTrace() ; 
	        }
		}
		debugPrint("Goodbye.") ; 
		System.exit(0); 
	}
}
