package fr.insa.chatSystem.Model;

import java.net.InetAddress;

public class LocalUser extends UserID {
	// Fiche ID de l'utilisateur local
	public LocalUser(String name, InetAddress address) {
		super(name, address);
	}
}