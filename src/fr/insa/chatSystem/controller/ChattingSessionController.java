package fr.insa.chatSystem.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.Message;
import fr.insa.chatSystem.model.UserID;
import fr.insa.chatSystem.controller.MainController.result;
import fr.insa.chatSystem.gui.ChatWindow;

public abstract class ChattingSessionController {
	// METHODES PUBLIQUES AVEC INTERFACE

	// Termine la session de chat avec l'utilisateur de nom "username"
	// Retourne INCORRECT_USERNAME si le nom n'est pas valide
	// Retourne SESSION_DOES_NOT_EXIST si la session de chat n'existe pas
	// Retourne SUCCESS si la session s'est bien fermee
	public static result closeSession(String username) {
				
		UserID id = DistributedDataController.getIDByName(username);
		if (id == null) {
			return result.INVALID_USERNAME;
		}
		ChattingSession session = getSessionByID(id);
		if (session == null) {
			return result.SESSION_DOES_NOT_EXIST;
		}
		try {
			session.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainController.NO_GUI_debugPrint("Removed "+username+" from active chat session list"+chatlist) ; 
		return result.SUCCESS;
	}

	// Envoie "message_content" a l'utilisateur de nom "username"
	// Retourne INCORRECT_USERNAME si le nom n'est pas valide
	// Retourne SESSION_DOES_NOT_EXIST si la session de chat n'existe pas
	// Retourne SUCCESS si le message a bien ete envoye
	public static result sendMessage(String username, String message_content) {
		UserID id = DistributedDataController.getIDByName(username);

		if (id == null) {
			return result.INVALID_USERNAME;
		}
		ChattingSession session = getSessionByID(id);
		if (session == null) {
			return result.SESSION_DOES_NOT_EXIST;
		}
		if (message_content.isBlank() || message_content.contains(DistributedDataController.getIllegalContent())) {
			return result.INVALID_CONTENT;
		}
		session.send(message_content);
		return result.SUCCESS;
	}
	
	
	// Cree une nouvelle session de chat avec l'utilisateur "other_user"
	// Retourne ALREADY_EXISTS si la session existe deja
	// Retourne INCORRECT_USERNAME si le nom d'utilisateur est incorect
	// Retourne SUCCESS si ca a marche
	public static result newChat(String other_user) {
		// First checking if username exists
		UserID id = DistributedDataController.getIDByName(other_user);
		if (!(id == null)) {
			ChattingSession session = getSessionByID(id);
			// Now checking if chatting session already exists
			if (session == null) {
				MainController.NO_GUI_debugPrint("Creating new chatting session . . . ");
				String threadname = "CS " + other_user;
				UserID user = DistributedDataController.getIDByName(other_user);
				session = new ChattingSession(user, threadname);
				chatlist.add(session);
				return result.SUCCESS;
			} else {
				return result.ALREADY_EXISTS;
			}

		} else {
			return result.INVALID_USERNAME;
		}
	}
	
	public static String getConversation(String username) {
		UserID id = DistributedDataController.getIDByName(username);
		ChattingSession session = getSessionByID(id);
		return session.getConversation();
	}

	// Retourne la liste des session de chat
	// Utile pour l'afficher
	public static List<ChattingSession> getChatList() {
		return chatlist;
	}

	// PRIVATE

	// Port used to chat
	static final private int CHAT_PORT = 3000;

	// List of all active chatting sessions
	static private List<ChattingSession> chatlist = new ArrayList<ChattingSession>(); // List of active chat sessions

	private static ChattingSession getSessionByID(UserID id) {
		for (ChattingSession session : chatlist) {
			if (session.getID().equals(id)) {
				return session;
			}

		}
		return null;
	}

	private static class ChattingSession extends Thread {

		private String conversation = "Conversation :" ; 
		private UserID other_user; // Other participant to the conversation
		
		private Socket socket = null;
		private PrintWriter output = null;
		BufferedReader input = null;

		// Constructor to be used when the agent's user wants the launch a new chatting
		// session
		public ChattingSession(UserID other_user, String threadname) {
			super(threadname);
			this.other_user = other_user;
			MainController.NO_GUI_debugPrint("Created new chatting session");
		}

		// Constructor to be used when the agent receives a new valid chat request from
		// the network
		public ChattingSession(UserID other_user, Socket socket, String threadname) {
			super(threadname);
			this.other_user = other_user;
			this.socket = socket;
			MainController.NO_GUI_debugPrint("Created new chatting session");
			try {
				this.output = new PrintWriter(this.socket.getOutputStream(), true);
				this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.start();
			MainController.NO_GUI_debugPrint("Started thread");
		}
		
		public String getConversation() {
			return conversation ; 
		}

		// Will send a dated message and begin the TCP connection if it is the first
		// message in the conversation (i.e. socket hasn't been instanciated yet)
		public void send(String M) {
			try {

				if (socket == null) {
					System.out.println("OTHER USER "+other_user+" CHAT LIST "+getChatList().toString()) ; 
					this.socket = new Socket(other_user.getAddress(), CHAT_PORT);
					MainController.NO_GUI_debugPrint("Created socket to chat with " + other_user.toString());
					this.output = new PrintWriter(this.socket.getOutputStream(), true);
					this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					this.start();
				}
				Message msg = new Message(M, MainController.username);
				MainController.NO_GUI_debugPrint("Sent " + msg.toString());
				//Adding message to conversation
				this.conversation += msg.toString() ; 
				if(ChatWindow.currentUser != null)
					if( !MainController.debug_mode && ChatWindow.currentUser.equals(this.other_user))
						ChatWindow.refreshMessages();
				this.output.println(msg);

			} catch (Exception e) {
				MainController
						.NO_GUI_debugPrint("Blimey! It appears " + other_user.toString() + " is busy or something");
				e.printStackTrace();
			}

		}

		public String toString() {
			return other_user.toString();
		}

		public UserID getID() {
			return this.other_user;
		}

		public void run() {

			// Closes sockets when the user closes the agent
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						DistributedDataController.notifyDisconnection(); // Notifying every user in the local network
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			String input_msg;
			boolean active = true;
			while (active) {
				try {
					MainController.NO_GUI_debugPrint("Listenning . . .");
					
					if(this.socket != null) {
							input_msg = this.input.readLine();
	
						if (input_msg == null) {
							active = false;
							MainController.NO_GUI_debugPrint("Disconnected");
							this.input.close();
							this.output.close();
							this.socket.close();
						} else {
							MainController.NO_GUI_debugPrint("Received: " + input_msg);
							this.conversation += input_msg+"\n" ;
							//MainController.debugPrint(input_msg);
							if(!MainController.debug_mode)
								if(ChatWindow.currentUser.equals(this.other_user))
									ChatWindow.refreshMessages();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static CSM_Deamon csm_deamon ; 
	public static void start_deamon() {
		MainController.NO_GUI_debugPrint("Starting deamon . . .");
		csm_deamon = new CSM_Deamon("CSM_Deamon");
		csm_deamon.start();
	}

	private static ServerSocket chat_socket_generator = null;
	static class CSM_Deamon extends Thread {
		
		
		CSM_Deamon(String name) {
			super(name);
			try {
				// Socket to receive incoming chat request
					if(chat_socket_generator != null)
						chat_socket_generator.close();
					chat_socket_generator = new ServerSocket(CHAT_PORT);
			} catch (Exception E) {
				E.printStackTrace();
			}
		}

		public void run() {

			// Closes sockets when the user closes the agent
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						// Shutting down sockets for each chatting session
						chat_socket_generator.close();
						for (ChattingSession session : chatlist) {
							session.socket.close();
							MainController.NO_GUI_debugPrint(
									"Chat socket connected to " + session.getID().toString() + " is shut down!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			// Listenning for incoming TCP connection request and gerating a new chatting
			// session if the source IP known
			try {
				Socket new_sock;
				MainController.NO_GUI_debugPrint("Listening on port " + CHAT_PORT);
				while (true) {
						new_sock = chat_socket_generator.accept();
						MainController
								.NO_GUI_debugPrint("Received chat request from " + new_sock.getInetAddress().toString());
						MainController.NO_GUI_debugPrint("Looking for username . . .");
						UserID usrid = DistributedDataController.getIDByIP(new_sock.getInetAddress());
						if (usrid == null) {
							MainController.NO_GUI_debugPrint(
									"No user with address " + new_sock.getInetAddress() + " has been found ! Ignoring");
						} else {
							MainController.NO_GUI_debugPrint(
									"Found user " + usrid.getName() + " for address " + new_sock.getInetAddress());
							chatlist.add(new ChattingSession(usrid, new_sock, "Chat Thread " + chatlist.size()));
						}
					}
			} catch (IOException E_sock) {
				//Ignore
			}
		}
	}
}
