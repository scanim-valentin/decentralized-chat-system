package fr.insa.chatSystem.model;

import java.net.*;

public class UserID {
	// Déclaration des strings depuis cette classe seulement
	private String name;
	private String hostAddress;

	// Definition de nom et de @IP
	public UserID(String name, String addr ) {
		this.name = name;
		this.hostAddress = addr;
	}

	// Lire le nom dans le string GetNom
	public String getName() {
		return this.name;
	}

	// Ecrire le nom dans le string name
	public String setName(String name) {
		return this.name = name;
	}

	// Lire l'@IP de l'utilisateur
	public String getAddress() {
		return this.hostAddress; // hostAddress prend l'@IP
	}

	// Afficher le nom et l'adresse IP de l'utilisateur & @Override controle la
	// classe parente
	@Override
	public String toString() {
		return this.getName() + " : " + this.getAddress().toString();
	}

	// @Override controle de la classe parente (arg type de retour)
	@Override
	public boolean equals(Object o) {
		// Si l'utilisateur ne rentre pas de login
		UserID u = null;
		if (o != null && o instanceof UserID) {
			u = (UserID) o;
		}
		// Si l'utilisateur rentre un pseudo
		if (u != null)
			return (this.hostAddress.equals(u.getAddress()));
		else
			return false;
	}
}