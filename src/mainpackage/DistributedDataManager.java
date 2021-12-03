package mainpackage;

import java.util.List ;
import java.net.* ; 
import java.io.* ; 

public class DistributedDataManager {

	final private int DGRAM_PORT = 1234 ;
	final private String ID_REQUEST_SIG = "ID_REQUEST" ; 
	final private String ONLINE_SIG = "ONLINE" ; 
	final private String OFFLINE_SIG = "OFFLINE" ;
	final private String NEW_NAME_SIG = "NEW_NAME" ; 
	final private String SEP = "■" ; //Separator, forbidden string
	
	DistributedDataManager(){
		DDM_Deamon ddm_deamon = new DDM_Deamon("DDM_Deamon") ; 
		ddm_deamon.start();  
	}

	private class DDM_Deamon extends Thread {
		DDM_Deamon(String name){
			super(name) ; 
		}
		public void run() {
			try {
				DatagramSocket dgramSocket = new DatagramSocket(DGRAM_PORT); //Socket to receive answers 
				byte[] buffer = new byte[256]; //Socket buffer
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length); //Incoming dgram packet 
				String packed ; 
				String[] unpacked ; 
				UDPBroadcast_IDRequest(dgramSocket) ; //Will request everyone's ID
				while(true) {
					try { 
						dgramSocket.receive(inPacket); //Receiving UDP answer
						packed = new String(inPacket.getData(),0, inPacket.getLength()) ; 
						unpacked = unpack(packed) ; 
						switch(unpacked[0]) { //First element of the array is the datagram type
							case ONLINE_SIG :
								//userlist.add(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an online signal the second element of the array is the username of the sender
								break ; 
							case OFFLINE_SIG :
								//userlist.remove(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an online signal the second element of the array is the username of the sender
								break ; 
							case NEW_NAME_SIG :
								//userlist.add(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an online signal the second element of the array is the username of the sender
								break;
						}//TODO 
					}
					catch(Exception E_rec) {
						E_rec.printStackTrace();
					}
				}
			}catch(Exception E_sock) {
				E_sock.printStackTrace();
			}
		}
	}

	
	private String[] unpack(String dgram) { //Turns received string into multiple strings
		return dgram.split("\\"+SEP);
	}
	private String pack(String[] unpacked_dgram) { //Packs multiple strings into one datagram string
		String dgram = unpacked_dgram[0] ; 
		for(int i = 1 ; i < unpacked_dgram.length ; i++) {
			dgram += SEP+unpacked_dgram[i] ; 
		}
		return dgram;
	}
	
	//used to broadcast an ID request to everyone on the local network 
	//and returns the list of every user on the network 
	public List<UserID> retrieveUserList() {
		List<UserID> userlist = null; //List of users to fill with other UsersID
		boolean done = false ; //A condition to exit the loop
		try {
		DatagramSocket dgramSocket = new DatagramSocket(DGRAM_PORT); //Socket to receive answers 
		byte[] buffer = new byte[256]; //Socket buffer
		DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length); //Incoming dgram packet 
		String packed ; 
		String[] unpacked ; 
		UDPBroadcast_IDRequest(dgramSocket) ; //Will request everyone's ID
		while(!done) {
			try { 
				dgramSocket.receive(inPacket); //Receiving UDP answer
				packed = new String(inPacket.getData(),0, inPacket.getLength()) ; 
				unpacked = unpack(packed) ; 
				if(unpacked[0].contentEquals(ONLINE_SIG)) //First element of the array is the datagram type
					userlist.add(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an online signal the second element of the array is the username of the sender
			}
			catch(Exception E_rec) {
				E_rec.printStackTrace();
			}
		}
		}catch(Exception E_sock) {
			E_sock.printStackTrace();
		}
		return userlist ; 
	}
	
	private void UDPBroadcast(String sig, DatagramSocket dgramSocket) {
		try {
			InetAddress BROADCAST_ADDR = InetAddress.getByName("255.255.255.255");
			DatagramPacket outPacket = new DatagramPacket(sig.getBytes(), sig.length(),BROADCAST_ADDR, DGRAM_PORT); //A packet containing only the signal to be broadcasted
			dgramSocket.send(outPacket) ; //Broadcast ID request on the local network 
		}
		catch(Exception E_bc) {
			E_bc.printStackTrace();
		}
	}
	
	
	
	private void UDPBroadcast_IDRequest(DatagramSocket dgramSocket) {
		UDPBroadcast(ID_REQUEST_SIG, dgramSocket) ; 
	}
	
	private void UDPBroadcast_NotifyConnection(String Name, DatagramSocket dgramSocket) {
		String[] unpacked = {ONLINE_SIG,Name} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ; 
	}
	
	private void UDPBroadcast_NotifyDisconnection(String Name, DatagramSocket dgramSocket) {
		String[] unpacked = {OFFLINE_SIG,Name} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ;  
	}
	
	private void UDPBroadcast_NotifyNewName(String currentName, String newName, DatagramSocket dgramSocket) {
		String[] unpacked = {NEW_NAME_SIG,currentName,newName} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ; 
	}
	
	
}
