package fr.insa.chatSystem.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainController {

	static public String username = "";

	public static enum result {
		SUCCESS, ALREADY_EXISTS, INVALID_USERNAME, SESSION_DOES_NOT_EXIST, INVALID_CONTENT;
	}

	// A simple method to wait a certain amount of time (ms)
	private static void wait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
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

	// Prints the remaning time every second
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

	// NE PAS SUPPRIMER
	// FONCTION DÉDIÉE AU DEBUG SANS INTERFACE GRAPHIQUE
	public static void NO_GUI_agent(String[] args) {

		debug_mode = true;
		NO_GUI_debugPrint("Args = " + args[0]);
		if (args[0].equals("ip")) {
			try {
				NO_GUI_debugPrint("Specified IP " + args[1]);
				addr = InetAddress.getByName(args[1]);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		NO_GUI_debugPrint("Starting DDC deamon . . .");
		DistributedDataController.start_deamon();

		// Waiting to gather all of the user id
		NO_GUI_debugPrint("Waiting for local ID request answer");

		// Waits 3 seconds to gather all of the usernames from the local network
		NO_GUI_formatedDelay(3);

		// Asks the user to choose a valid username
		while (username == "") {
			NO_GUI_debugPrint("Please enter a valid username.");
			NO_GUI_changeUsername();
		}

		NO_GUI_debugPrint("Starting CSC deamon . . .");
		ChattingSessionController.start_deamon();

		boolean close = false;
		String help = "\n help : display the command list \n exit : close the agent\n newname : change username\n newchat : start chatting session \n send : send a message in a conversation \n endchat : end a conversation \n chatlist : see active conversation list \n userlist : see user list";
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
