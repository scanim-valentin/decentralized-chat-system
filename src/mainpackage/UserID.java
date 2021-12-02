package mainpackage;

public class UserID {
	    // Déclaration des strings depuis cette classe seulement
	    private String nom;
	    private String hostAddress;
	    
	    // Definition de nom et de @IP
	    protected void User(String nom, String address) {
	        this.nom=nom;
	        this.hostAddress=address;
	    }
	    
	    // Lire le nom dans le string GetNom
	    public String getNom() {
	    	return this.nom;
	    	}
	    // Ecrire le nom dans le string name
	    public String setNom(String name) {
	    	return nom = name;
	    	}
	    
	    // Lire l'@IP de l'utilisateur
	    public String getAddress() { 
	    	return this.hostAddress;  // hostAddress prend l'@IP
	    	}
	    // Afficher le nom et l'adresse IP de l'utilisateur
	    public String toString() {
	    	return this.getNom()+" : "+this.getAddress();
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