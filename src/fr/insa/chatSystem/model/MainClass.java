package fr.insa.chatSystem.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.ChattingSessionManager.*;

public class MainClass {
	
	static public List<UserID> userlist = new ArrayList<UserID>() ; //List of users to fill with other UsersID
	
	static public String username = "" ; 
	
	
	//Command prompt
	static public String input = "" ; 
	static final public String EXIT_IN = "exit" ; 
	static final public String NEWNAME_IN = "newname" ; 
	static final public String CHATWITH_IN = "newchat" ;
	static final public String USRLIST_IN = "userlist" ;  
	
	//Maximum number of simultaneous chatting sessions
	static final public int MAX_SESSIONS = 50 ;  
	
	static private void debugPrint(String str) {
		System.out.println("["+Thread.currentThread().getName()+"] MainClass: "+str);
	}
	
	/*Checks if a username is valid, i.e.: 
	* - it is not a blank string 
	* - it does not contain a forbidden character
	* - this username is available 
	*/ 
	public static boolean isValid(String S) {
		
		if(S.isBlank() || S.contains(DistributedDataManager.SEP))
			return false ; 
		
		for(UserID id: userlist) {
			if(id.getName().equals(S)) 
				return false ; 
		}
		
		return true ; 
	}
	
	//Changes name in user list
	static public void updateList(String oldname, String newname) {
		for(UserID id: userlist) {
			if(id.getName().equals(oldname)) { 
				 id.setName(newname);
				 debugPrint("Updated name in user list ("+oldname+"->"+newname+")") ; 
			}
		}
	}
	
	//A simple method to wait a certain amount of time (ms)
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
        debugPrint("Set new name to "+username);
	}
	
	//Waits for an input, checks the username validity and starts a new chatting session if it is valid
	private static void getChatRequest() {
		BufferedReader reader = new BufferedReader(
	            new InputStreamReader(System.in));
		String name_input = "" ;
        try {
	        // Reading data using readLine
	        boolean valid = false ; 
        	while(!valid) {
        		debugPrint("Who do you want to chat with? User list:"+userlist.toString());
        		name_input = reader.readLine();
        		
        		int i = 0;
				boolean in_list = false;
				debugPrint("Looking for username . . .") ; 
				while(( i < MainClass.userlist.size() ) &&  !in_list) {
					UserID usrid = MainClass.userlist.get(i);
					if(usrid.getName().equals(name_input)) {
						debugPrint("Found user at address "+usrid.getAddress().toString()+" for name "+name_input) ; 
						in_list = true ; 
						ChattingSessionManager.chatlist.add(new ChattingSession(usrid,"Chat Thread "+ChattingSessionManager.chatlist.size())) ;
						valid = true ; 
					}
					i++;
				}
				if(!in_list) {
					debugPrint("No user with name "+name_input+" has been found ! Please try again") ; 
				}
	   
	        }
	    }catch(Exception E) {
	    	E.printStackTrace();
	    }
	}
	
	//Prints the remaning time every second
	public static void formatedDelay(int delay) {
		for(int i = 0 ; i < delay ; i++) {
			wait(1000);
			debugPrint((delay-i)+" seconds remaining . . . ") ; 
		}
	}
	
	public static void nogui() {
		
		debugPrint("Starting DDM deamon . . .") ; 
		DistributedDataManager.start_deamon();
		
		//Waiting to gather all of the user id
		debugPrint("Waiting for local ID request answer");
		
		//Waits 3 seconds to gather all of the usernames from the local network 
		formatedDelay(3); 
		
		//Asks the user to choose a valid username
		changeUsername(); 
		
		debugPrint("Starting CSM deamon . . .") ; 
		ChattingSessionManager.start_deamon();

		
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
	        			
	        		case USRLIST_IN : 
	        			debugPrint("Userlist : "+userlist.toString());
	        			break;
	        			
	        		case CHATWITH_IN : 
	        			getChatRequest(); 
	        			break; 
	        			
	        		default : 
	        			debugPrint("Unidentified input. \n exit : close the agent\n newname : change username\n newchat : start chatting session\n userlist : see user list");
	        	}
	        }catch(Exception E) {
	        	E.printStackTrace() ; 
	        }
		}
		debugPrint("Goodbye.") ; 
		System.exit(0); 
	}
}

