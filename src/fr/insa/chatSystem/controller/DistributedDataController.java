package fr.insa.chatSystem.controller;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import fr.insa.chatSystem.model.UserID;
import fr.insa.chatSystem.controller.MainController.result;
import fr.insa.chatSystem.gui.ChatWindow;

public abstract class DistributedDataController {

	// PARTIE PUBLIQUES

	// Lance le thread d'ecoute UDP
	// A APPELER DES LE DEBUT
	public static void start_deamon() {
		MainController.NO_GUI_debugPrint("Starting deamon . . .");
		DDC_Deamon DDC_deamon = new DDC_Deamon("DDC_Deamon");
		DDC_deamon.start();
	}

	// Permet de changer le nom d'utilisateur
	public static void updateUserList(String oldname, String newname) {
		for (UserID id : userlist) {
			if (id.getName().equals(oldname))
				id.setName(newname);
		}
	}

	// Retourne la liste d'utilisateurs
	// Utile pour l'afficher
	public static List<UserID> getUserList() {
		return userlist;
	}

	public static UserID getIDByName(String name) {
		MainController.NO_GUI_debugPrint("Looking for username . . .");
		for (UserID id : userlist) {
			if (id.getName().equals(name)) {
				MainController.NO_GUI_debugPrint("Found user with address " + id.getAddress() + " for name " + name);
				return id;
			}
		}
		return null;
	}

	public static UserID getIDByIP(InetAddress IP) {
		MainController.NO_GUI_debugPrint("Looking for IP adress . . .");
		for (UserID id : userlist) {
			if (id.getAddress().equals(IP)) {
				MainController
						.NO_GUI_debugPrint("Found user with name " + id.getName() + " for address " + IP.toString());
				return id;
			}
		}
		return null;
	}

	// Checks the username validity and changes username if it is valid
	// Retourne SUCCESS si le nom d'utilisateur est valide et a ete change
	// Retourne INVALID_CONTENT si le nom contient des charactere interdits ou si il
	// est vide
	// Retourne ALREADY_EXISTS si le nom existe deja dans la liste d'utilisateur
	public static result setUsername(String username) {
		result R = isValid(username);
		try {
			// Reading data using readLine
			if (R == result.SUCCESS) {
				// Notifying everyone on the local network
				if (MainController.username.isEmpty()) {
					MainController.username = username;
					DistributedDataController.notifyConnection();
				} else {
					DistributedDataController.notifyNewName(username);
					MainController.username = username;
				}
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
		return R;
	}

	// Notifie les autres utilisateur d'une connection
	// A APPELER DES QUE LE NOM A ETE DEFINI
	static public void notifyConnection() {
		String[] unpacked = { ONLINE_SIG, MainController.username, RemoteDatabaseController.getDB_ID() };
		MainController.NO_GUI_debugPrint("Notifying online status to everyone");
		UDPBroadcast(pack(unpacked), dgramSocket_TX);
	}

	static public void notifyDisconnection() {
		String[] unpacked = { OFFLINE_SIG, MainController.username };
		MainController.NO_GUI_debugPrint("Notifying offline status to everyone");
		UDPBroadcast(pack(unpacked), dgramSocket_TX);
	}

	static public void notifyNewName(String newname) {
		String[] unpacked = { NEW_NAME_SIG, MainController.username, newname };
		MainController.NO_GUI_debugPrint(
				"Notifying change of username (" + MainController.username + "->" + newname + ") to everyone");
		UDPBroadcast(pack(unpacked), dgramSocket_TX);
	}

	static public String getIllegalContent() {
		return SEP;
	}

	// PARTIE PRIVEES

	static private int BROADCAST_REPETITION = 1;

	static private List<UserID> userlist = new ArrayList<UserID>(); // List of users to fill with other UsersID

	static final private int DGRAM_PORT_RX = 1238;
	static final private int DGRAM_PORT_TX = 1239;

	// Each datagram sequence begins with one of these words indicating the type of
	// content
	static final private String ID_REQUEST_SIG = "ID_REQUEST"; // Indicates that someone on the network is willing to
	// update its userlist
	static final private String ONLINE_SIG = "ONLINE"; // Indicates that someone went online and is follow by its
	// username, then its unique DB ID
	static final private String OFFLINE_SIG = "OFFLINE"; // Indicates that someone went offline and is follow by its
	// username, then its unique DB ID
	static final private String NEW_NAME_SIG = "NEW_NAME"; // Indicates that someone changed their name and is followed
	// by their old name and their new name, then its unique DB ID

	static final private String SEP = "|"; // Separator, forbidden character in username choice

	static DatagramSocket dgramSocket_RX;
	static DatagramSocket dgramSocket_TX;

	// Retourne SUCCESS si le nom d'utilisateur est valide (non utilise par
	// quelqu'un d'autre et n'utilise pas le charactere interdit et n'est pas vide)
	// Retourne INVALID_CONTENT si le nom contient des charactere interdits ou si il
	// est vide
	// Reoturne ALREADY_EXISTS si le nom existe deja dans la liste d'utilisateur
	public static result isValid(String S) {

		if (S.isBlank() || S.contains(DistributedDataController.SEP))
			return result.INVALID_CONTENT;

		for (UserID id : userlist) {
			if (id.getName().equals(S))
				return result.ALREADY_EXISTS;
		}

		return result.SUCCESS;
	}

	// Continuously listen on DGRAM_PORT for incoming UPD notifications
	static class DDC_Deamon extends Thread {
		DDC_Deamon(String name) {
			super(name);
			try {

				if (MainController.addr == null) {
					dgramSocket_RX = new DatagramSocket(DGRAM_PORT_RX); // Socket to receive notifications
					dgramSocket_TX = new DatagramSocket(DGRAM_PORT_TX); // Socket to send notifications
				} else {
					dgramSocket_RX = new DatagramSocket(DGRAM_PORT_RX, MainController.addr); // Socket to receive
																								// notifications
					dgramSocket_TX = new DatagramSocket(DGRAM_PORT_TX, MainController.addr); // Socket to send
																								// notifications
				}
			} catch (Exception E) {
				E.printStackTrace();
			}
		}

		public void run() {

			// Closes sockets when the user closes the agent
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						DistributedDataController.notifyDisconnection(); // Notifying every user in the local network
						dgramSocket_RX.close();
						dgramSocket_TX.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			try {
				byte[] buffer = new byte[256]; // Entry socket buffer
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length); // Incoming dgram packet
				String packed;
				String[] unpacked;
				MainController.NO_GUI_debugPrint("Requesting IDs to establish user list");
				UDPBroadcast_IDRequest(dgramSocket_TX); // Will request everyone's ID

				while (true) {
					try {
						MainController.NO_GUI_debugPrint("Waiting for incoming signal . . .");
						dgramSocket_RX.receive(inPacket); // Receiving UDP answer
						packed = new String(inPacket.getData(), 0, inPacket.getLength());
						MainController.NO_GUI_debugPrint(
								" Received packet data: " + packed + " from user " + inPacket.getAddress().toString());
						unpacked = unpack(packed);
						MainController.NO_GUI_debugPrint(
								"unpacked packet len=" + unpacked.length + ": " + unpacked.toString());
						UserID usr = null;
						if ((MainController.addr != null && MainController.addr != inPacket.getAddress())
								|| (NetworkInterface.getByInetAddress(inPacket.getAddress()) == null)) { // Checks if
																											// received
							// packet is from any
							// local interface ...
							// Important : always checking if debug mode is activated (testing on a single
							// machine with a SPECIFIED address bound to an agent)
							switch (unpacked[0]) { // First element of the array is the datagram type

							case ID_REQUEST_SIG: // In the case someone on the network request everyone's identity, the
								// agent answers with username
								if (!MainController.username.isEmpty()) { // An empty string as a username is forbidden
																			// and
									// should only happen if the user has yet to
									// choose a name
									MainController.NO_GUI_debugPrint("Sending username \"" + MainController.username
											+ "\" to " + inPacket.getAddress().toString());
									InetAddress sender_addr = inPacket.getAddress();
									String[] unpacked_answer = { ONLINE_SIG, MainController.username,
											RemoteDatabaseController.getDB_ID() };
									UDPUnicast(sender_addr, pack(unpacked_answer), dgramSocket_TX);
								} else {
									MainController.NO_GUI_debugPrint("Not connected yet! Not answering");
								}
								break;
							case ONLINE_SIG:
								MainController.NO_GUI_debugPrint(
										"Identified " + ONLINE_SIG + " from " + inPacket.getAddress().toString() + "(\""
												+ unpacked[1] + "\") with database ID " + unpacked[2]);
								usr = new UserID(unpacked[1], inPacket.getAddress(), unpacked[2]);
								if (!userlist.contains(usr))
									userlist.add(usr);
								// ChatWindow.remoteUserList.add(new UserID(unpacked[1],
								// inPacket.getAddress(),unpacked[2])) ;
								// In the case of an online signal the second element of the array is the
								// username of the sender
								MainController.NO_GUI_debugPrint("Added name in userlist : " + userlist.toString());
								if (!MainController.no_gui)
									ChatWindow.refreshList();
								// To be added to the list
								break;

							case OFFLINE_SIG:
								MainController.NO_GUI_debugPrint("Identified " + OFFLINE_SIG + " from "
										+ inPacket.getAddress().toString() + "(\"" + unpacked[1] + "\")");
								MainController.NO_GUI_debugPrint("userlist = " + userlist.toString());
								usr = null;
								for (UserID user : userlist) {
									if (user.getName().equals(unpacked[1]))
										usr = user;
								}

								if (usr != null) {
									switch (ChattingSessionController.closeSession(usr.getName())) {
									case INVALID_USERNAME:
										MainController.NO_GUI_debugPrint("USERNAME INVALID " + unpacked[1]);
										break;
									case SESSION_DOES_NOT_EXIST:
										MainController.NO_GUI_debugPrint("SESSION DOES NOT EXIST " + unpacked[1]);
										break;
									default:
										break;
									}
									userlist.remove(usr);
								}

								// In the case of an offline signal the second element of the array is the
								// username of the sender

								MainController.NO_GUI_debugPrint("Removed name in userlist : " + userlist.toString());
								// To be removed from the list
								if (!MainController.no_gui)
									ChatWindow.refreshList();
								break;

							case NEW_NAME_SIG:
								MainController.NO_GUI_debugPrint(
										"Identified " + NEW_NAME_SIG + " from " + inPacket.getAddress().toString()
												+ "(prev. \"" + unpacked[1] + "\", now \"" + unpacked[2] + "\")");
								updateUserList(unpacked[1], unpacked[2]);
								if (!MainController.no_gui)
									ChatWindow.refreshList();
								break;
							}
						} else {
							MainController.NO_GUI_debugPrint("Identified sender as localhost ("
									+ InetAddress.getLocalHost() + ") , ignoring packet");
						}
					} catch (SocketException E_rec) {
						// Ignore
					}
				}
			} catch (Exception E_sock) {
				E_sock.printStackTrace();
			}
		}
	}

	// Turns received string into multiple strings based on predefined separator
	static private String[] unpack(String dgram) {
		return dgram.split("\\" + SEP);
	}

	// Packs multiple strings into one datagram string seprated by the predfined
	// character
	static private String pack(String[] unpacked_dgram) {
		String dgram = unpacked_dgram[0];
		for (int i = 1; i < unpacked_dgram.length; i++) {
			dgram += SEP + unpacked_dgram[i];
		}
		return dgram;
	}

	// Broadcast a notification on the local network
	static private void UDPBroadcast(String sig, DatagramSocket dgramSocket) {
		try {
			InetAddress BROADCAST_ADDR = InetAddress.getByName("255.255.255.255");
			for (int i = 0; i < BROADCAST_REPETITION; i++)
				UDPUnicast(BROADCAST_ADDR, sig, dgramSocket);
		} catch (Exception E_bc) {
			E_bc.printStackTrace();
		}
	}

	// Sends a datagram to a unique recipient
	static private void UDPUnicast(InetAddress dest_addr, String sig, DatagramSocket dgramSocket) {
		try {
			DatagramPacket outPacket = new DatagramPacket(sig.getBytes(), sig.length(), dest_addr, DGRAM_PORT_RX);
			// A packet containing only the signal to be broadcasted

			dgramSocket.send(outPacket); // Broadcast ID request on the local network
		} catch (Exception E_bc) {
			E_bc.printStackTrace();
		}
	}

	static private void UDPBroadcast_IDRequest(DatagramSocket dgramSocket) {
		UDPBroadcast(ID_REQUEST_SIG, dgramSocket);
	}

	@SuppressWarnings("unused")
	static private void UDPBroadcast_NotifyConnection(final String Name, final DatagramSocket dgramSocket) {
		String[] unpacked = { ONLINE_SIG, Name };
		UDPBroadcast(pack(unpacked), dgramSocket);
	}

	@SuppressWarnings("unused")
	static private void UDPBroadcast_NotifyDisconnection(final String Name, final DatagramSocket dgramSocket) {
		String[] unpacked = { OFFLINE_SIG, Name };
		UDPBroadcast(pack(unpacked), dgramSocket);
	}

	@SuppressWarnings("unused")
	static private void UDPBroadcast_NotifyNewName(final String currentName, final String newName,
			final DatagramSocket dgramSocket) {
		String[] unpacked = { NEW_NAME_SIG, currentName, newName };
		UDPBroadcast(pack(unpacked), dgramSocket);
	}
}