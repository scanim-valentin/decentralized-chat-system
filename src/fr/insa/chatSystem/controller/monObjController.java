package fr.insa.chatSystem.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.DistributedDataManager;
import fr.insa.chatSystem.model.UserID;

public class monObjController {
		
    	static public List<UserID> userlist = new ArrayList<UserID>() ; //List of users to fill with other UsersID
    	
    	//static public String pseudo = "" ; 
    	
    	
    	//Command prompt
    	static public String input = "" ; 
    	static final public String EXIT_IN = "exit" ; 
    	static final public String NEWNAME_IN = "newname" ; 
    	static final public String CHATWITH_IN = "newchat" ;
    	static final public String USRLIST_IN = "userlist" ;  
    	
    	//Maximum number of simultaneous chatting sessions
    	static final public int MAX_SESSIONS = 50 ;  
    	
    	static private void UserName(String str) {
    		setName
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
            		name_input = reader.readLine();
    	        	if(isValid(name_input)) {
    	        		valid = true ; 
    	        	} else {

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
            		//debugPrint("Who do you want to chat with? User list:"+userlist.toString());
            		name_input = reader.readLine();
            		
            		if(ChattingSessionManager.newChat(name_input)) {
            			//debugPrint("Chatting session created with user "+name_input) ; 
            			valid = true ; 
            		}
           
    	        	if (valid = false)
    	        		//debugPrint("Unknown"+userlist.toString()) ; 
    	   
    	        }
    	    }catch(Exception E) {
    	    	E.printStackTrace();
    	    }
    	}
    	
    	public static void gui() {
    		
    		DistributedDataManager.start_deamon();
    	
    		//Asks the user to choose a valid username
    		changeUsername(); 
    		
    		// Enter data using BufferReader
    		//debugPrint("Please enter a command.");
    		boolean close = false ; 
    		while(!close) {
    			//Command prompt
    	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	        try {
    	        	input = reader.readLine() ; 
    	        	switch(input) {
    	        		case EXIT_IN : 
    	        			close = true ;
    	        			//debugPrint("Closing agent . . .") ; 
    	        			break;
    	        	
    	        		case NEWNAME_IN :
    	        			changeUsername();
    	        			break;
    	        			
    	        		case USRLIST_IN : 
    	        			//debugPrint("Userlist : "+userlist.toString());
    	        			break;
    	        			
    	        		case CHATWITH_IN : 
    	        			getChatRequest(); 
    	        			break; 
    	        			
    	        		default : 
    	        			//debugPrint("Unidentified input. \n exit : close the agent\n newname : change username\n chat : start chatting session\n userlist : see user list");
    	        	}
    	        }catch(Exception E) {
    	        	E.printStackTrace() ; 
    	        }
    		}
    		//debugPrint("Goodbye.") ; 
    		System.exit(0); 
    	}
    }
