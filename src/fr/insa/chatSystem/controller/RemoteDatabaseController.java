package fr.insa.chatSystem.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import fr.insa.chatSystem.model.Message;



public abstract class RemoteDatabaseController {
	
	static private String user_id = null ;
	static private String user_password = null ;
	
	static private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_004";
	static private String DB_PASSWORD =  "ish6uo2U" ; 
	static private String DB_USER = "tp_servlet_004" ;

	static private Connection connection = null;
	static private Statement statement = null;

	public static void initializeConnection(String username, String password) {
		try {
			MainController.NO_GUI_debugPrint("Connecting to " + DB_URL);
			// connection = DriverManager.getConnection(DATABASE,username,password);
			connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
			statement = connection.createStatement();
			statement.executeUpdate("USE tp_servlet_004;");
			MainController.NO_GUI_debugPrint("Succcesfully connected to " + DB_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void endConnection() {
		try {
			connection.close();
			MainController.NO_GUI_debugPrint("Succcesfully disconnected from " + DB_URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getHistory(String other_user) {
		try {
			ResultSet rs = statement.executeQuery("SELECT message FROM histories WHERE (source='"+user_id+"' AND dest="+SQL_getIDNumber(other_user)+") OR (source='"+SQL_getIDNumber(other_user)+"' AND dest="+user_id+");");
			while (rs.next()) {
				MainController.NO_GUI_debugPrint(rs.getString(1) + ":");
				MainController.NO_GUI_debugPrint("" + rs.getInt("history"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String SQL_getIDNumber(String name_input) {
		return "(SELECT id FROM IDs WHERE username='"+name_input+"';)";
	}
	
	//Vérifie l'identité de l'utilisateur pour pouvoir accéder à ses données
	public static boolean AuthCheck(String id, String password) {
		boolean R = false ;
		try {
			R = statement.executeQuery("SELECT * FROM IDs WHERE id='"+id+"' AND password='"+password+"';").next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return R ; 
	}
	
	// Permet de s'incrire dans la base de donnée avec un pseudo et un mdp et retourne l'identifiant unique (int)
	public static int signUp(String pseudo, String pswd) {
		int R = 0 ; 
		try {
			int affected_rows = statement.executeUpdate("INSERT INTO IDs(password,username) VALUES ('"+pswd+"','"+pseudo+"') ; ");  
			R = statement.executeQuery("SELECT LAST_INSERT_ID() FROM IDs ;").getInt(0) ; 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return R ;
	}
	
	//Permet de mettre à jour le pseudo dans la base de donnée 
	public static void updateName(String new_name) {
		try {
			statement.executeUpdate("UPDATE IDs SET username='"+new_name+"' WHERE id='"+user_id+"'; ");  
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	//Permet de mettre à jour le pseudo dans la base de donnée 
	private static void addMessage(List<Message> messages) {
		try {
			for (Message message : messages) {
				Timestamp timestamp = new Timestamp(message.getTime().);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		*/
		
}
