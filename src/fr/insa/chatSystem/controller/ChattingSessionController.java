package fr.insa.chatSystem.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.insa.chatSystem.model.Message;
import fr.insa.chatSystem.model.UserID;
import fr.insa.chatSystem.controller.MainController ;
import fr.insa.chatSystem.controller.MainController.result; 

public abstract class ChattingSessionController {
	//METHODES PUBLIQUES AVEC INTERFACE 
	
	//Termine la session de chat avec l'utilisateur de nom "username"
	//Retourne INCORRECT_USERNAME si le nom n'est pas valide
	//Retourne SESSION_DOES_NOT_EXIST si la session de chat n'existe pas
	//Retourne SUCCESS si la session s'est bien fermee
	public static result closeSession(String username) {
		UserID id = DistributedDataController.getIDByName(username) ; 
		if(id == null) {
			return result.INVALID_USERNAME ;
		}
		ChattingSession session = getSessionByID(id) ; 
		if(session == null) {
			return result.SESSION_DOES_NOT_EXIST ; 
		}
		try {
			session.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.SUCCESS ; 
	}
	
	//Envoie "message_content" a l'utilisateur de nom "username"
	//Retourne INCORRECT_USERNAME si le nom n'est pas valide
	//Retourne SESSION_DOES_NOT_EXIST si la session de chat n'existe pas
	//Retourne SUCCESS si le message a bien ete envoye
	public static result sendMessage(String username, String message_content){
		UserID id = DistributedDataController.getIDByName(username) ; 
		if(id == null) {
			return result.INVALID_USERNAME ;
		}
		ChattingSession session = getSessionByID(id) ; 
		if(session == null) {
			return result.SESSION_DOES_NOT_EXIST ; 
		}
		if(message_content.isBlank() || message_content.contains(DistributedDataController.getIllegalContent())) {
			return result.INVALID_CONTENT ;
		}
		session.sendMessage(message_content);
		return result.SUCCESS ; 
	}
	
	//Cree une nouvelle session de chat avec l'utilisateur "other_user"
	//Retourne ALREADY_EXISTS si la session existe deja
	//Retourne INCORRECT_USERNAME si le nom d'utilisateur est incorect
	//Retourne SUCCESS si ca a marche
	public static result newChat(String username){
		//First checking if username exists
		UserID id = DistributedDataController.getIDByName(username) ; 
		if(!(id == null)) {
			ChattingSession session = getSessionByID(id) ;
			//Now checking if chatting session already exists
			if(session == null) {
				String threadname = "CS "+username ;
				UserID user = DistributedDataController.getIDByName(username) ; 
				session = new ChattingSession(user, threadname) ; 
				chatlist.add(session);
				return result.SUCCESS ; 
			}else {
				return result.ALREADY_EXISTS ;
			}
			
		}else{
			return result.INVALID_USERNAME ; 
		}
	}
	
	
	//Retourne la liste des session de chat
	//Utile pour l'afficher
	public static List<ChattingSession> getChatList() {
		return chatlist ; 
	}
	
	
	//PRIVATE
	
	//Port used to chat
	static final private int CHAT_PORT = 3000 ;
	
	//List of all active chatting sessions
	static private List<ChattingSession> chatlist = new ArrayList<ChattingSession>(); // List of active chat sessions
	
	private static ChattingSession getSessionByID(UserID id) {
		for (ChattingSession session : chatlist) {
			if(session.getID().equals(id)) {
				return session ; 
			}
				
		}
		return null ; 
	}
	
	/*
	//Returns chattingsession if session already exists, throws an exception otherwise
	private static ChattingSession getSessionByID(UserID id) throws IDNotFoundInChatListException {
		for (ChattingSession session : chatlist) {
			if(session.getID().equals(id)) {
				MainController.NO_GUI_debugPrint("Found chatting session with "+id.toString()) ; 
				return session ; 
			}
				
		}
		MainController.NO_GUI_debugPrint("No session with "+id.toString()+" has been found !") ; 
		throw new IDNotFoundInChatListException("ID not found in chat list!" );
	}*/

	
	private static class ChattingSession extends Thread {

					
		private UserID other_user; // Other participant to the conversation
		private List<Message> message_list = new ArrayList<Message>(); // List of messages (history)

		private Socket socket = null;
		
		//Constructor to be used when the agent's user wants the launch a new chatting session
		public ChattingSession(UserID other_user, String threadname) {
			super(threadname);
			this.other_user = other_user;
			MainController.NO_GUI_debugPrint ("Created new chatting session");
		}
		
		//Constructor to be used when the agent receives a new valid chat request from the network
		public ChattingSession(UserID other_user, Socket socket, String threadname) {
			super(threadname);
			this.other_user = other_user;
			this.socket = socket;
			MainController.NO_GUI_debugPrint ("Created new chatting session");
			this.start();
		}

		//Will send a dated message and begin the TCP connection if it is the first message in the conversation (i.e. socket hasn't been instanciated yet)
		public void sendMessage(String M) {
			try {
				
				if (socket == null) {
						socket = new Socket(other_user.getAddress(), CHAT_PORT);
						MainController.NO_GUI_debugPrint ("Created socket to chat with " + other_user.toString());
						this.start();
				}
				PrintWriter output = new PrintWriter(socket.getOutputStream()); 
				Message msg = new Message(M);
				MainController.NO_GUI_debugPrint ("Sent "+msg.toString()) ;
				output.println(msg);
			
			} catch (Exception e) {
				MainController.NO_GUI_debugPrint ("Blimey! It appears " + other_user.toString() + " is busy or something");
				e.printStackTrace();
			}
			
		}
		
		public String toString() {
			return other_user.toString();
		}
		
		public UserID getID() {
			return this.other_user ; 
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
			
			BufferedReader input;
			try {
				MainController.NO_GUI_debugPrint ("Listenning . . .");
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input_msg = input.readLine();
				MainController.NO_GUI_debugPrint ("Received: "+input_msg);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			
		}

	}

	public static void start_deamon() {
		MainController.NO_GUI_debugPrint ("Starting deamon . . .");
		CSM_Deamon csm_deamon = new CSM_Deamon("CSM_Deamon");
		csm_deamon.start();
	}

	static class CSM_Deamon extends Thread {
		ServerSocket chat_socket_generator;

		CSM_Deamon(String name) {
			super(name);
			try {
				chat_socket_generator = new ServerSocket(CHAT_PORT); // Socket to receive incoming chat request

			} catch (Exception E) {
				E.printStackTrace();
			}
		}

		public void run() {

			// Closes sockets when the user closes the agent
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						//Shutting down deamon socket 
						DistributedDataController.notifyDisconnection(); // Notifying every user in the local network
						chat_socket_generator.close();
						MainController.NO_GUI_debugPrint ("Chat socket generator is shut down!");
						
						//Shutting down sockets for each chatting session
						for(ChattingSession session: chatlist) {
							session.socket.close();
							MainController.NO_GUI_debugPrint ("Chat socket connected to "+session.getID().toString()+" is shut down!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//Listenning for incoming TCP connection request and gerating a new chatting session if the source IP known 
			try {
				Socket new_sock;
				MainController.NO_GUI_debugPrint ("Listening on port "+CHAT_PORT);
				while (true) {
					new_sock = chat_socket_generator.accept();
					MainController.NO_GUI_debugPrint ("Received chat request from " + new_sock.getInetAddress().toString());
					MainController.NO_GUI_debugPrint ("Looking for username . . .");
					UserID usrid = DistributedDataController.getIDByIP(new_sock.getInetAddress()) ; 
					if (usrid== null) {
						MainController.NO_GUI_debugPrint ("No user with address " + new_sock.getInetAddress() + " has been found ! Ignoring");
					}else {
						MainController.NO_GUI_debugPrint ("Found user " + usrid.getName() + " for address " + new_sock.getInetAddress());
						chatlist.add(new ChattingSession(usrid, new_sock, "Chat Thread " + chatlist.size()));
					}
					
				}

			} catch (Exception E_sock) {
				E_sock.printStackTrace();
			}
		}
	}
}
