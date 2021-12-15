package fr.insa.chatSystem.model;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public abstract class ChattingSessionManager{
	
	static final private int INC_CHAT_PORT = 1240 ; 
	
	static public List<ChattingSession> chatlist = new ArrayList<ChattingSession>() ; //List of active chat sessions
	
	static public boolean newChat(String other_user_name) {
		boolean R = false ; 
		for(UserID id: MainClass.userlist) {
			if(id.getName().equals(other_user_name)) { //Checking the user existence 
				 debugPrint("Identified "+id.toString()+" in user list") ;
				 ChattingSession new_chat =  new ChattingSession(id,"ChattingSession"+ ( chatlist.size() + 1 ) ) ;  
		         if(!chatlist.contains(new_chat)) { //Checking if a conversation is already opened with that perso
		        	 debugPrint("Creating new chatting session with user"+id.toString());
		        	 chatlist.add(new_chat); 
		        	 new_chat.start(); //Starts the chat thread
		        	 R = true ; 
		         
		         }
			}
		}
		return R;
	}
		
	static class ChattingSession extends Thread {
		
		private UserID other_user ; //Other participant to the conversation
		
		//private List<Message> message_list = new ArrayList<Message>() ;  //List of messages (history)
		
		private Socket socket ; 
		
		public ChattingSession(UserID other_user, String threadname) {
			super(threadname);
			this.other_user = other_user ; 
		}
		
		
		private void debugPrint(String str) {
			System.out.println("["+Thread.currentThread().getName()+"] ChattingSession: "+str);
		}
		
		public void run() {
			
		}
		
	}
	
	static class CSM_Deamon extends Thread {
		ServerSocket chat_socket_generator ; 
		CSM_Deamon(String name){
			super(name) ; 
			try {
				chat_socket_generator = new ServerSocket(INC_CHAT_PORT); //Socket to receive incoming chat request
				
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
				while(true) {
					new_sock = chat_socket_generator.accept();
					debugPrint("Received chat request from "+new_sock.getInetAddress().toString()) ; 
					int i = 0;
					boolean in_list = false;
					debugPrint("Looking for username . . .") ; 
					while(( i < MainClass.userlist.size() ) &&  in_list) {
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
		System.out.println("["+Thread.currentThread().getName()+"] DistributedDataManager : "+str) ; 
	}
}
