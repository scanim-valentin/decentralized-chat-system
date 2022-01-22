package fr.insa.chatSystem.model;

import java.net.*;

public class UserID {
	// Déclaration des strings depuis cette classe seulement

	//Pseudo de la personne (oeut changer)
	private String name;

	//IP
	private InetAddress hostAddress;

	//ID unique dans la BDD des historiques (ne change jamais)
	private String DB_ID = null;

	public String getDB_ID() {
		return DB_ID;
	}

	// Definition de nom et de @IP
	public UserID(String name, InetAddress address, String unique_DB_ID) {
		this.name = name;
		this.hostAddress = address;
		this.DB_ID = unique_DB_ID ;
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