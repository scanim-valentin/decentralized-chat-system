package fr.insa.chatSystem.Model;

import java.net.*;

public class UserID {
	// Déclaration des strings depuis cette classe seulement
	private String name;
	private InetAddress hostAddress;
	private String id = null;

	// Definition de nom et de @IP
	public UserID(String name, InetAddress address) {
		this.name = name;
		this.hostAddress = address;
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
	public InetAddress getAddress() {
		return this.hostAddress; // hostAddress prend l'@IP
	}

	// Afficher le nom et l'adresse IP de l'utilisateur
	public String toString() {
		return this.getName() + " : " + this.getAddress().toString();
	}

	// Ecrire l'identifiant de l'utilisateur
	public void setId(String id) {
		this.id = id;
	}

	// Obtenir l'identifiant de l'utilisateur
	public String getId() {
		return id;
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