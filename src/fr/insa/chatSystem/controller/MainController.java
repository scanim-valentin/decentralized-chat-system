package fr.insa.chatSystem.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.controller.ChattingSessionController.ChattingSession;
import fr.insa.chatSystem.model.UserID;

public class MainController {

	static public List<UserID> userlist = new ArrayList<UserID>(); // List of users to fill with other UsersID

	static public String username = "" ;

	// Maximum number of simultaneous chatting sessions
	static final public int MAX_SESSIONS = 50;
	
	/*
	 * Checks if a username is valid, i.e.: - it is not a blank string - it does not
	 * contain a forbidden character - this username is available
	 */
	public static boolean isValid(String S) {

		if (S.isBlank() || S.contains(DistributedDataController.SEP))
			return false;

		for (UserID id : userlist) {
			if (id.getName().equals(S))
				return false;
		}

		return true;
	}
	
	// Changes name in user list
		static public void updateList(String oldname, String newname) {
			for (UserID id : userlist) {
				if (id.getName().equals(oldname)) {
					id.setName(newname);
				}
			}
		}

	// A simple method to wait a certain amount of time (ms)
	private static void wait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	//-------------------------------------------------------------------------------
	//PARTIE DEBUG - NE PAS SUPPRIMER
	
	static public Boolean debug_mode = false ; 
	
	// Command prompt
	static public String input = "";
	static final public String EXIT_IN = "exit";
	static final public String NEWNAME_IN = "newname";
	static final public String CHATWITH_IN = "newchat";
	static final public String USRLIST_IN = "userlist";
	
	// Changes name in user list
	//NE PAS SUPPRIMER
	//FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	static public void NO_GUI_updateList(String oldname, String newname) {
		for (UserID id : userlist) {
			if (id.getName().equals(oldname)) {
				id.setName(newname);
				NO_GUI_debugPrint("Updated name in user list (" + oldname + "->" + newname + ")");
			}
		}
	}
			
	// Waits for an input, checks the username validity and changes username if it
	// is valid
	// If it is valid, changes username attributes and asks the
	// DistributedDataManager to notify everyone on the network
	private static void NO_GUI_changeUsername() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name_input = "";
		try {
			// Reading data using readLine
			boolean valid = false;
			while (!valid) {
				name_input = reader.readLine();
				if (isValid(name_input)) {
					valid = true;
				} else {

				}
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
		// Notifying everyone on the local network
		if (username.isEmpty()) {
			username = name_input;
			DistributedDataController.notifyConnection();
		} else {
			DistributedDataController.notifyNewName(name_input);
			username = name_input;
		}
		NO_GUI_debugPrint("Set new name to " + username);
	}

	// Waits for an input, checks the username validity and starts a new chatting
	// session if it is valid
	//Waits for an input, checks the username validity and starts a new chatting session if it is valid
	//NE PAS SUPPRIMER
	//FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	private static void NO_GUI_getChatRequest() {
			BufferedReader reader = new BufferedReader(
		            new InputStreamReader(System.in));
			String name_input = "" ;
	        try {
		        // Reading data using readLine
		        boolean valid = false ; 
	        	while(!valid) {
	        		NO_GUI_debugPrint("Who do you want to chat with? User list:"+userlist.toString());
	        		name_input = reader.readLine();
	        		
	        		int i = 0;
					boolean in_list = false;
					NO_GUI_debugPrint("Looking for username . . .") ; 
					while(( i < userlist.size() ) &&  !in_list) {
						UserID usrid = userlist.get(i);
						if(usrid.getName().equals(name_input)) {
							NO_GUI_debugPrint("Found user at address "+usrid.getAddress().toString()+" for name "+name_input) ; 
							in_list = true ; 
							ChattingSessionController.chatlist.add(new ChattingSession(usrid,"Chat Thread "+ChattingSessionController.chatlist.size())) ;
							valid = true ; 
						}
						i++;
					}
					if(!in_list) {
						NO_GUI_debugPrint("No user with name "+name_input+" has been found ! Please try again") ; 
					}
		   
		        }
		    }catch(Exception E) {
		    	E.printStackTrace();
		    }
		}

		//Prints the remaning time every second
		//NE PAS SUPPRIMER
		//FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
		public static void NO_GUI_formatedDelay(int delay) {
			for(int i = 0 ; i < delay ; i++) {
				wait(1000);
				NO_GUI_debugPrint((delay-i)+" seconds remaining . . . ") ; 
			}
		}
		
		//NE PAS SUPPRIMER
		//FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
		public static void NO_GUI_agent() {
			
			debug_mode = true ; 
			
			NO_GUI_debugPrint("Starting DDM deamon . . .") ; 
			DistributedDataController.start_deamon();
			
			//Waiting to gather all of the user id
			NO_GUI_debugPrint("Waiting for local ID request answer");
			
			//Waits 3 seconds to gather all of the usernames from the local network 
			NO_GUI_formatedDelay(3); 
			
			//Asks the user to choose a valid username
			NO_GUI_changeUsername(); 
			
			// Enter data using BufferReader
			NO_GUI_debugPrint("Please enter a command.");
			boolean close = false ; 
			while(!close) {
				//Command prompt
		        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		        try {
		        	input = reader.readLine() ; 
		        	switch(input) {
		        		case EXIT_IN : 
		        			close = true ;
		        			NO_GUI_debugPrint("Closing agent . . .") ; 
		        			break;
		        	
		        		case NEWNAME_IN :
		        			NO_GUI_changeUsername();
		        			break;
		        			
		        		case USRLIST_IN : 
		        			NO_GUI_debugPrint("Userlist : "+userlist.toString());
		        			break;
		        			
		        		case CHATWITH_IN : 
		        			NO_GUI_getChatRequest(); 
		        			break; 
		        			
		        		default : 
		        			NO_GUI_debugPrint("Unidentified input. \n exit : close the agent\n newname : change username\n newchat : start chatting session\n userlist : see user list");
		        	}
		        }catch(Exception E) {
		        	E.printStackTrace() ; 
		        }
			}
			NO_GUI_debugPrint("Goodbye.") ; 
			System.exit(0); 
		}
		
		//UNE FONCTION POUR AFFICHER DU DEBUG AVEC DES DETAILS
		//NE PAS SUPPRIMER
		static public void NO_GUI_debugPrint(String str) {
				//dgram.split("\\" + SEP)
				if(debug_mode) {
					String[] info_file = Thread.currentThread().getStackTrace()[2].getFileName().split("\\.java") ; 
					System.out.println("[" + Thread.currentThread().getName() + "] "+info_file[0]+" : " + str);
					
				}
		}
		
		
}
