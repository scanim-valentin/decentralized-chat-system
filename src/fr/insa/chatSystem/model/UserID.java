package fr.insa.chatSystem.model;
import java.net.* ;

public class UserID {
	    // Déclaration des strings depuis cette classe seulement
	    private String name;
	    private InetAddress hostAddress;
	    
	    // Definition de nom et de @IP
	    public UserID(InetAddress address, String name) {
	        this.name=name;
	        this.hostAddress=address;
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
	    	return this.hostAddress;  // hostAddress prend l'@IP
	    	}
	    // Afficher le nom et l'adresse IP de l'utilisateur
	    public String toString() {
	    	return this.getName()+" : "+this.getAddress().toString();
	    	}
	    
	    public boolean equals(Object o)
	    {
	    	// Si l'utilisateur ne rentre pas de login
	        UserID u = null;
	        if (o != null && o instanceof UserID){
	            u = (UserID) o;
	        }
	        // Si l'utilisateur rentre un pseudo
	        if(u != null)
	            return (this.hostAddress.equals(u.getAddress()));
	        else
	            return false;
	    }
	}