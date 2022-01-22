package fr.insa.chatSystem.model;

import java.net.InetAddress;

public class RemoteUser extends UserID {
	// Fiche ID de l'utilisateur distant
	public RemoteUser(String name, InetAddress address) {
		super(name, address,-1+"");
	}
}