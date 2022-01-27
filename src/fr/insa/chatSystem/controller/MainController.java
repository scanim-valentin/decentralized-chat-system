package fr.insa.chatSystem.controller;

import fr.insa.chatSystem.gui.ConnectWindow;
import fr.insa.chatSystem.model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainController {

	static public String username = "";
	static public String unique_id = "0" ;
	public static enum result {
		SUCCESS, ALREADY_EXISTS, INVALID_USERNAME, SESSION_DOES_NOT_EXIST, INVALID_CONTENT, INVALID_DB_AUTH
	}

	// A simple method to wait a certain amount of time (ms)
	public static void wait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	//INSCRIPTION A LA BASE DE DONNEE
	public static result useDatabaseSignUp(String db_URL, String db_name, String db_password, String Name, String Password){

		result R = DistributedDataController.isValid(Name);
		try {
			//Nom d'utilisateur unique et correct
			if (R == result.SUCCESS) {
				//Connection à la BDD
				RemoteDatabaseController.initializeConnection(db_URL,db_name,db_password);
				//On s'inscrit dans la BDD avec le pseudo et le MDP choisit
				RemoteDatabaseController.signUp(Name,Password) ;

				// On notifie tout le monde sur le réseau local
				MainController.username = Name;
				DistributedDataController.notifyConnection();
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
		return R;
	}

	public static result useDatabaseSignIn(String db_URL, String db_name, String db_password, String Name, String DB_ID, String Password){
		result R = DistributedDataController.isValid(Name);
		try {
			//Nom d'utilisateur unique et correct
			if (R == result.SUCCESS) {
				//Connection à la BDD
				RemoteDatabaseController.initializeConnection(db_URL,db_name,db_password);

				//On vérifie l'existence de l'utilisateur dans la BDD avec l'ID unique et le MDP
				if(!RemoteDatabaseController.AuthCheck(DB_ID,Password)){
					return result.INVALID_DB_AUTH ;
				}
				RemoteDatabaseController.setDB_ID(DB_ID);
				// On notifie tout le monde sur le réseau local
				MainController.username = Name;
				DistributedDataController.notifyConnection();
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
		return R;
	}

	// -------------------------------------------------------------------------------
	// PARTIE DEBUG - NE PAS SUPPRIMER

	static public Boolean debug_mode = false;
	static public InetAddress addr = null; // When testing on a single computer (with virtual interfaces), this field is
											// the local address in use by this agent

	// Command prompt
	static public String input = "";
	static final public String EXIT_IN = "exit";
	static final public String NEWNAME_IN = "newname";
	static final public String CHATWITH_IN = "newchat";
	static final public String USRLIST_IN = "userlist";
	static final public String CHATLIST_IN = "chatlist";
	static final public String SENDTO_IN = "send";
	static final public String ENDCHAT_IN = "endchat";
	static final public String HELP_IN = "help";
	static final public String DBCONNECT_IN = "dbconnect";
	static final public String DBCLOSE_IN = "dbclose";
	static final public String DBGETID_IN = "dbgetid";
	static final public String DBTEST_IN = "dbtest";

	// Waits for an input, checks the username validity and changes username if it
	// is valid
	// If it is valid, changes username attributes and asks the
	// DistributedDataManager to notify everyone on the network
	private static void NO_GUI_changeUsername() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name_input = "";
		try {
			// Reading data using readLine
			name_input = reader.readLine();
			result R = DistributedDataController.setUsername(name_input);
			switch (R) {
				case INVALID_CONTENT:
					MainController.NO_GUI_debugPrint(
							"Username contains illegal character " + DistributedDataController.getIllegalContent());
					break;

				case ALREADY_EXISTS:
					MainController.NO_GUI_debugPrint("Username already exists in userlist!");
					break;

				default:
					MainController.NO_GUI_debugPrint("Successfully changed username to" + name_input);
					break;
			}
		} catch (Exception E) {
			E.printStackTrace();
		}

	}

	// Waits for an input, checks the username validity and starts a new chatting
	// session if it is valid
	// NE PAS SUPPRIMER
	// FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	private static void NO_GUI_getChatRequest() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name_input = "";
		try {
			// Reading data using readLine
			NO_GUI_debugPrint(
					"Who do you want to chat with? User list:" + DistributedDataController.getUserList().toString());
			name_input = reader.readLine();
			result R = ChattingSessionController.newChat(name_input);
			switch (R) {
			case ALREADY_EXISTS:
				MainController.NO_GUI_debugPrint("Session with " + name_input + " already exists!");
				break;
			case INVALID_USERNAME:
				MainController.NO_GUI_debugPrint("No user with name " + name_input + " has been found!");
				break;
			default:
				MainController.NO_GUI_debugPrint("Created chat session with " + name_input);
				break;
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	// Prints the remaining time every second
	// NE PAS SUPPRIMER
	// FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	public static void NO_GUI_formatedDelay(int delay) {
		for (int i = 0; i < delay; i++) {
			wait(1000);
			NO_GUI_debugPrint((delay - i) + " seconds remaining . . . ");
		}
	}

	// Wait for a name and send a message if name is valid
	public static void NO_GUI_send() {
		NO_GUI_debugPrint("To who? " + ChattingSessionController.getChatList().toString());
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name_input = "";
		try {
			name_input = reader.readLine();
			NO_GUI_debugPrint("Enter message to send to " + name_input);
			String message_content = reader.readLine();
			result R = ChattingSessionController.sendMessage(name_input, message_content);
			switch (R) {

			case SESSION_DOES_NOT_EXIST:
				MainController.NO_GUI_debugPrint("Session with " + name_input + " does not exist exists!");
				break;

			case INVALID_USERNAME:
				MainController.NO_GUI_debugPrint("No user with name " + name_input + " has been found!");
				break;

			case INVALID_CONTENT:
				MainController.NO_GUI_debugPrint(
						"Message contains illegal character " + DistributedDataController.getIllegalContent());
				break;

			default:
				MainController.NO_GUI_debugPrint("Message sent to user " + name_input);
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void NO_GUI_getDBAuth() {
		NO_GUI_debugPrint("\nPre completed database info.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String url_input = reader.readLine();
			NO_GUI_debugPrint("Enter database name:");
			String name_input  = reader.readLine();
			NO_GUI_debugPrint("Enter password:");
			String password_input = reader.readLine();
			RemoteDatabaseController.initializeConnection(url_input, name_input, password_input);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void NO_GUI_test_history() {
		List<Message> messages = new ArrayList<Message>();
		for (int i = 0; i < 50; i++) {
			messages.add(new Message("Message numéro " + i, "USER_1")) ;
		}
		MainController.NO_GUI_debugPrint("TEST ENVOI DE 50 MESSAGES A LA BDD");
		RemoteDatabaseController.addHistory("USER_1", messages);
		MainController.NO_GUI_debugPrint("TEST EFFECTUE, TEST LECTURE DE L'HISTORIQUE");
		NO_GUI_debugPrint(RemoteDatabaseController.getHistory("USER_1")+"");
	}

	// NE PAS SUPPRIMER
	// FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	public static void NO_GUI_agent(String[] args) {

		debug_mode = true ;

		NO_GUI_debugPrint("Starting DDC deamon . . .");
		DistributedDataController.start_deamon();

		// Waiting to gather all of the user id
		NO_GUI_debugPrint("Waiting for local ID request answer");

		// Waits 3 seconds to gather all of the usernames from the local network
		NO_GUI_formatedDelay(3);



		NO_GUI_debugPrint("Args = " + args[0]);
		if (args[0].equals("sign")) {
			if(args[1].equals("up")) {
				NO_GUI_debugPrint("Signing up with name " + args[2] + " and password " + args[3]);
				result R = useDatabaseSignUp("", "", "", args[2], args[3]);
				switch (R) {
					case INVALID_CONTENT:
						MainController.NO_GUI_debugPrint(
								"Username contains illegal character " + DistributedDataController.getIllegalContent());
						System.exit(1);
						break;

					case ALREADY_EXISTS:
						MainController.NO_GUI_debugPrint("Username already exists in userlist!");
						System.exit(1);
						break;

					default:
						MainController.NO_GUI_debugPrint("Successfully signed up with username " + args[2]+" and unique database ID "+RemoteDatabaseController.getDB_ID());
						break;

				}
			}
			if(args[1].equals("in")) {
				NO_GUI_debugPrint("Signing in with unique DB ID " + args[3] + " and password " + args[4]);
				result R = useDatabaseSignIn("", "", "", args[2], args[3], args[4]);
				switch (R) {
					case INVALID_CONTENT:
						MainController.NO_GUI_debugPrint(
								"Username contains illegal character " + DistributedDataController.getIllegalContent());
						System.exit(1);
						break;

					case ALREADY_EXISTS:
						MainController.NO_GUI_debugPrint("Username already exists in userlist!");
						System.exit(1);
						break;

					case INVALID_DB_AUTH:
						MainController.NO_GUI_debugPrint("Database authentication failed!");
						System.exit(1);
						break;

					default:
						MainController.NO_GUI_debugPrint("Successfully signed in with username " + args[3]+" and unique database ID "+RemoteDatabaseController.getDB_ID());
						break;

				}
			}
		}else{
			// Asks the user to choose a valid username
			while (username == "") {
				NO_GUI_debugPrint("Please enter a valid username.");
				NO_GUI_changeUsername();
			}
		}

		NO_GUI_debugPrint("Starting CSC deamon . . .");
		ChattingSessionController.start_deamon();

		boolean close = false;
		String help = "\n help : display the command list \n exit : close the agent\n newname : change username\n newchat : start chatting session \n send : send a message in a conversation \n endchat : end a conversation \n chatlist : see active conversation list \n userlist : see user list \n dbconnect : connect to remote history database";
		while (!close) {
			// Command prompt
			// Enter data using BufferReader
			NO_GUI_debugPrint("Please enter a command.");

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				input = reader.readLine();
				switch (input) {
				case HELP_IN:
					NO_GUI_debugPrint(help);
					break;

				case EXIT_IN:
					close = true;
					NO_GUI_debugPrint("Closing agent . . .");
					break;

				case NEWNAME_IN:
					NO_GUI_changeUsername();
					break;

				case USRLIST_IN:
					NO_GUI_debugPrint("Userlist : " + DistributedDataController.getUserList().toString());
					break;

				case CHATWITH_IN:
					NO_GUI_getChatRequest();
					break;

				case CHATLIST_IN:
					NO_GUI_debugPrint("Chat list : " + ChattingSessionController.getChatList().toString());
					break;

				case SENDTO_IN:
					NO_GUI_send();
					break;

				case DBCONNECT_IN:
					NO_GUI_getDBAuth();
					break;

				case DBGETID_IN:
					//NO_GUI_getIDfromDB();
					break;

					case DBTEST_IN:
						NO_GUI_test_history();
						break;

				default:
					NO_GUI_debugPrint("Unidentified input." + help);
				}
			} catch (Exception E) {
				E.printStackTrace();
			}
		}
		NO_GUI_debugPrint("Goodbye.");
		System.exit(0);
	}
	
	static public void debug(String[] args) {
		debug_mode = true ; 
		if (args[0].equals("gui_debug")) {
			DistributedDataController.start_deamon();
			// Ouvrir la premiere page
			new ConnectWindow(username);
		}else
			MainController.NO_GUI_agent(args);
	}
	
	// UNE FONCTION POUR AFFICHER DU DEBUG AVEC DES DETAILS
	// NE PAS SUPPRIMER
	static public void NO_GUI_debugPrint(String str) {
		// dgram.split("\\" + SEP)
		if (debug_mode) {
			String[] info_file = Thread.currentThread().getStackTrace()[2].getFileName().split("\\.java");
			System.out.println("[" + Thread.currentThread().getName() + "] " + info_file[0] + " : " + str);

		}
	}
	

}
