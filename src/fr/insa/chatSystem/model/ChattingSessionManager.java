package fr.insa.chatSystem.model;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.DistributedDataManager.DDM_Deamon;

public abstract class ChattingSessionManager{
	
	static final private int CHAT_PORT = 1240 ; 
	
	static public List<ChattingSession> chatlist = new ArrayList<ChattingSession>() ; //List of active chat sessions
	
		
	static class ChattingSession extends Thread {
		
		private UserID other_user ; //Other participant to the conversation
		private List<Message> message_list = new ArrayList<Message>() ;  //List of messages (history)
		
		private Socket socket = null; 
		
		public ChattingSession(UserID other_user, Socket socket, String threadname) {
			super(threadname);
			this.other_user = other_user ; 
			this.socket = socket; 
			debugPrint("Created new chatting session");
			//TODO to remove
		}
		
		public ChattingSession(UserID other_user, String threadname) {
			super(threadname);
			this.other_user = other_user ;  
			debugPrint("Created new chatting session");
			sendMessage("test");
			//TODO to remove
		}
		
		private void sendMessage(String M) {
			if(socket == null) {
				
				try {
					socket = new Socket(other_user.getAddress(),CHAT_PORT);
					debugPrint("Created socket to chat with "+other_user.toString());
				} catch (IOException e) {
					debugPrint("Blimey! It appears "+other_user.toString()+" is busy or something");
					e.printStackTrace();
				}
			}
		}
		
		private void debugPrint(String str) {
			System.out.println("["+Thread.currentThread().getName()+"] ChattingSession: "+str);
		}
		
		public void run() {
			
		}
		
	}
	
	public static void start_deamon(){
		debugPrint("Starting deamon . . .") ; 
		CSM_Deamon csm_deamon = new CSM_Deamon("CSM_Deamon") ; 
		csm_deamon.start();  
	}
	
	static class CSM_Deamon extends Thread {
		ServerSocket chat_socket_generator ; 
		CSM_Deamon(String name){
			super(name) ; 
			try {
				chat_socket_generator = new ServerSocket(CHAT_PORT); //Socket to receive incoming chat request
				
			}catch(Exception E) {
				E.printStackTrace();
			}
		}
		
		public void run() {
			
			//Closes sockets when the user closes the agent
			Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
			    try {
        			DistributedDataManager.notifyDisconnection(); //Notifying every user in the local network
        			chat_socket_generator.close();
			        System.out.println("Chat socket generator is shut down!");
			    } catch (Exception e) { e.printStackTrace(); }
			}});
			
			try {
				Socket new_sock ; 
				debugPrint("Listening") ; 
				while(true) {
					new_sock = chat_socket_generator.accept();
					debugPrint("Received chat request from "+new_sock.getInetAddress().toString()) ; 
					int i = 0;
					boolean in_list = false;
					debugPrint("Looking for username . . .") ; 
					while(( i < MainClass.userlist.size() ) &&  !in_list) {
						UserID usrid = MainClass.userlist.get(i);
						if(usrid.getAddress().equals(new_sock.getInetAddress())) {
							debugPrint("Found user "+usrid.getName()+" for address "+new_sock.getInetAddress()) ; 
							in_list = true ; 
							chatlist.add(new ChattingSession(usrid,"Chat Thread "+chatlist.size())) ;
						}
						i++;
					}
					if(!in_list)
						debugPrint("No user with address "+new_sock.getInetAddress()+" has been found ! Ignoring") ; 

				}
					
			}catch(Exception E_sock) {
				E_sock.printStackTrace();
			}
		}
	}
	
	static private void debugPrint(String str) {
		System.out.println("["+Thread.currentThread().getName()+"] ChattingSessionManager : "+str) ; 
	}
}
