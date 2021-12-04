import java.util.List ;
import java.net.* ; 
import java.io.* ; 

abstract class DistributedDataManager {

	static final private int DGRAM_PORT_RX = 1236 ;
	static final private int DGRAM_PORT_TX = 1237 ;
	
	//Each datagram sequence begins with one of these words indicating the type of content
	static final private String ID_REQUEST_SIG = "ID_REQUEST" ; //Indicates that someone on the network is willing to update its userlist 
	static final private String ONLINE_SIG = "ONLINE" ; //Indicates that someone went online and is follow by its username
	static final private String OFFLINE_SIG = "OFFLINE" ; //Indicates that someone went offline and is follow by its username
	static final private String NEW_NAME_SIG = "NEW_NAME" ;  //Indicates that someone changed their name and is followed by their old name and their new name
	
	static final private String SEP = "|" ; //Separator, forbidden character in username choice
	
	static void start_deamon(){
		debugPrint("Starting deamon . . .") ; 
		DDM_Deamon ddm_deamon = new DDM_Deamon("DDM_Deamon") ; 
		ddm_deamon.start();  
	}
	
	//Continuously listen on DGRAM_PORT for incoming UPD notifications 
	static class DDM_Deamon extends Thread {
		DDM_Deamon(String name){
			super(name) ; 
		}
		public void run() {
			try {
				DatagramSocket dgramSocket_RX = new DatagramSocket(DGRAM_PORT_RX); //Socket to receive notifications
				DatagramSocket dgramSocket_TX = new DatagramSocket(DGRAM_PORT_TX); //Socket to send notifications
				byte[] buffer = new byte[256]; //Entry socket buffer
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length); //Incoming dgram packet 
				String packed ; 
				String[] unpacked ; 
				debugPrint("Requesting IDs to establish user list") ; 
				UDPBroadcast_IDRequest(dgramSocket_TX) ; //Will request everyone's ID
				while(true) {
					try { 
						dgramSocket_RX.receive(inPacket); //Receiving UDP answer
						packed = new String(inPacket.getData(),0, inPacket.getLength()) ; 
						debugPrint("Received packet data: "+packed) ; 
						unpacked = unpack(packed) ; 
						switch(unpacked[0]) { //First element of the array is the datagram type
							
							case ID_REQUEST_SIG : //In the case someone on the network request everyone's identity, the agent answers with username
								if(!MainClass.username.isEmpty()) { //An empty string as a username is forbidden and should only happen if the user has yet to choose a name
									debugPrint("Sending username \""+MainClass.username+"\" to "+inPacket.getAddress().toString()) ; 
									InetAddress sender_addr = inPacket.getAddress() ;
									String[] unpacked_answer = {ONLINE_SIG , MainClass.username} ; 
									UDPUnicast(sender_addr, pack(unpacked_answer),dgramSocket_TX) ;
								}
								break; 
							case ONLINE_SIG :
								debugPrint("Identified "+ID_REQUEST_SIG+" from "+inPacket.getAddress().toString()+"(\""+unpacked[1]+"\")") ; 
								MainClass.userlist.add(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an online signal the second element of the array is the username of the sender
								debugPrint("Added name in userlist : "+MainClass.userlist.toString()) ;//To be added to the list
								break ; 																
								
							case OFFLINE_SIG :
								debugPrint("Identified "+ID_REQUEST_SIG+" from "+inPacket.getAddress().toString()+"(\""+unpacked[1]+"\")") ; 
								MainClass.userlist.remove(new UserID(unpacked[1], inPacket.getAddress())) ; //In the case of an offline signal the second element of the array is the username of the sender
								debugPrint("Removed name in userlist : "+MainClass.userlist.toString()) ; //To be removed from the list
								break ; 																	
								
							case NEW_NAME_SIG :
								debugPrint("Identified "+ID_REQUEST_SIG+" from "+inPacket.getAddress().toString()+"(prev. \""+unpacked[1]+"\", now \""+unpacked[2]+"\")") ; 
								int i = 0 ; 
								while( (i < MainClass.userlist.size()) ) {
									i++ ;
									if(MainClass.userlist.get(i).getName().equals(unpacked[1])) {
										MainClass.userlist.get(i).setName(unpacked[2]) ; //In that case, the second element is the old name  and the third element correspond to the new name
										i = MainClass.userlist.size() ;  
									}
									debugPrint("Replaced name in userlist : "+MainClass.userlist.toString()) ; 
								}
								break ;
								
						}
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

	//Turns received string into multiple strings based on predefined separator
	static private String[] unpack(String dgram) { 
		return dgram.split("\\"+SEP);
	}
	
	//Packs multiple strings into one datagram string seprated by the predfined character
	static private String pack(String[] unpacked_dgram) { 
		String dgram = unpacked_dgram[0] ; 
		for(int i = 1 ; i < unpacked_dgram.length ; i++) {
			dgram += SEP+unpacked_dgram[i] ; 
		}
		return dgram;
	}
	
	//Broadcasts an ID request to everyone on the local network 
	static public void requestUserList() {		
		try {
			DatagramSocket dgramSocket = new DatagramSocket(DGRAM_PORT_TX); //Socket to receive answers 
			UDPBroadcast_IDRequest(dgramSocket) ; //Will request everyone's ID
		}catch(Exception E){
			E.printStackTrace() ; 
		}
	}
	
	//Broadcast a notification on the local network
	static private void UDPBroadcast(String sig, DatagramSocket dgramSocket) {
		try {
			InetAddress BROADCAST_ADDR = InetAddress.getByName("255.255.255.255");
			UDPUnicast(BROADCAST_ADDR,sig,dgramSocket) ; 
		}
		catch(Exception E_bc) {
			E_bc.printStackTrace();
		}
	}
	
	//Sends a datagram to a unique recipient 
	static private void UDPUnicast(InetAddress dest_addr, String sig, DatagramSocket dgramSocket) {
		try {
			DatagramPacket outPacket = new DatagramPacket(sig.getBytes(), sig.length(),dest_addr, DGRAM_PORT_TX); //A packet containing only the signal to be broadcasted
			dgramSocket.send(outPacket) ; //Broadcast ID request on the local network 
		}
		catch(Exception E_bc) {
			E_bc.printStackTrace();
		}
	}
	
	
	
	static private void UDPBroadcast_IDRequest(DatagramSocket dgramSocket) {
		UDPBroadcast(ID_REQUEST_SIG, dgramSocket) ; 
	}
	
	static private void UDPBroadcast_NotifyConnection(String Name, DatagramSocket dgramSocket) {
		String[] unpacked = {ONLINE_SIG,Name} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ; 
	}
	
	static private void UDPBroadcast_NotifyDisconnection(String Name, DatagramSocket dgramSocket) {
		String[] unpacked = {OFFLINE_SIG,Name} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ;  
	}
	
	static private void UDPBroadcast_NotifyNewName(String currentName, String newName, DatagramSocket dgramSocket) {
		String[] unpacked = {NEW_NAME_SIG,currentName,newName} ; 
		UDPBroadcast( pack(unpacked) , dgramSocket) ; 
	}
	
	static private void debugPrint(String str) {
		System.out.println("DistributedDataManager : "+str) ; 
	}
}