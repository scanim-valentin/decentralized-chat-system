package fr.insa.chatSystem.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.Message;
import fr.insa.chatSystem.model.UserID;

public abstract class ChattingSessionController {

	static final private int CHAT_PORT = 1240;

	static public List<ChattingSession> chatlist = new ArrayList<ChattingSession>(); // List of active chat sessions

	public static class ChattingSession extends Thread {

					
		private UserID other_user; // Other participant to the conversation
		private List<Message> message_list = new ArrayList<Message>(); // List of messages (history)

		private Socket socket = null;

		public ChattingSession(UserID other_user, Socket socket, String threadname) {
			super(threadname);
			this.other_user = other_user;
			this.socket = socket;
			MainController.NO_GUI_debugPrint ("Created new chatting session");
			// TODO to remove
		}

		public ChattingSession(UserID other_user, String threadname) {
			super(threadname);
			this.other_user = other_user;
			MainController.NO_GUI_debugPrint ("Created new chatting session");
			sendMessage("test");
			// TODO to remove
		}

		private void sendMessage(String M) {
			if (socket == null) {

				try {
					socket = new Socket(other_user.getAddress(), CHAT_PORT);
					MainController.NO_GUI_debugPrint ("Created socket to chat with " + other_user.toString());
				} catch (IOException e) {
					MainController.NO_GUI_debugPrint ("Blimey! It appears " + other_user.toString() + " is busy or something");
					e.printStackTrace();
				}
			}
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
							MainController.NO_GUI_debugPrint ("Chat socket connected to "+session.getName()+" is shut down!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			try {
				Socket new_sock;
				MainController.NO_GUI_debugPrint ("Listening");
				while (true) {
					new_sock = chat_socket_generator.accept();
					MainController.NO_GUI_debugPrint ("Received chat request from " + new_sock.getInetAddress().toString());
					int i = 0;
					boolean in_list = false;
					MainController.NO_GUI_debugPrint ("Looking for username . . .");
					while ((i < MainController.userlist.size()) && !in_list) {
						UserID usrid = MainController.userlist.get(i);
						if (usrid.getAddress().equals(new_sock.getInetAddress())) {
							MainController.NO_GUI_debugPrint ("Found user " + usrid.getName() + " for address " + new_sock.getInetAddress());
							in_list = true;
							chatlist.add(new ChattingSession(usrid, "Chat Thread " + chatlist.size()));
						}
						i++;
					}
					if (!in_list)
						MainController.NO_GUI_debugPrint ("No user with address " + new_sock.getInetAddress() + " has been found ! Ignoring");

				}

			} catch (Exception E_sock) {
				E_sock.printStackTrace();
			}
		}
	}
}
