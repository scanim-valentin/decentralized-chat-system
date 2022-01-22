package fr.insa.chatSystem.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import fr.insa.chatSystem.model.Message;

public abstract class RemoteDatabaseController {

	static private String user_id = null;

	static private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_004";
	static private String DB_PASSWORD = "ish6uo2U";
	static private String DB_USER = "tp_servlet_004";

	static private Connection connection = null;
	static private Statement statement = null;

	public static String getDB_ID() {
		return user_id;
	}

	// Initialisation de la liaison avec la BDD avec en param�tre l'URL, le nom de
	// la base de donn�e et le mot de passe pour y acc�der (que l'administrarteur
	// doit renseigner)
	public static void initializeConnection(String URL, String NOM, String MDP) {
		try {
			// POUR FACILITER LES TESTS ET EVITER DE TOUT TAPER A CHAQUE FOIS
			if (!URL.isBlank() || !NOM.isBlank() || !MDP.isBlank()) {
				DB_URL = URL;
				DB_USER = NOM;
				DB_PASSWORD = MDP;
			}
			MainController.NO_GUI_debugPrint("Connecting to " + DB_URL);
			// connection = DriverManager.getConnection(DATABASE,username,password);
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			statement = connection.createStatement();
			statement.executeUpdate("USE tp_servlet_004;");
			MainController.NO_GUI_debugPrint("Succcesfully connected to " + DB_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Obtention de l'historique (liste de messages) � partir du pseudo de la
	// personne � qui l'utilisateur parle
	public static List<Message> getHistory(String other_user) {
		List<Message> messages = new ArrayList<Message>();
		try {
			String other_id;
			// TODO supprimer ça à la fin
			// if(other_user.equals("USER_1"))
			// other_id = "19" ;
			// else
			other_id = DistributedDataController.getIDByName(other_user).getDB_ID();
			ResultSet rs = statement
					.executeQuery("SELECT * FROM histories WHERE (source=" + user_id + " AND destination=" + other_id
							+ ") OR (destination=" + user_id + " AND source=" + other_id + ");");
			while (rs.next()) {
				if (rs.getString("source").equals(getDB_ID())) {
					messages.add(
							new Message(rs.getString("content"), MainController.username, rs.getTimestamp("time")));
				} else {
					String name = getNameFromDB_ID(rs.getString("source"));
					messages.add(new Message(rs.getString("content"), name, rs.getTimestamp("time")));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}

	// Termine la liaison � la BDD
	public static void endConnection() {
		try {
			connection.close();
			MainController.NO_GUI_debugPrint("Succcesfully disconnected from " + DB_URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String getNameFromDB_ID(String other_id) {
		String other_user = "";
		try {
			// TODO supprimer ça à la fin
			// if(other_id.equals("19"))
			// other_user = "19" ;
			// else{
			ResultSet rs = statement.executeQuery("SELECT username FROM IDs WHERE id=" + user_id + ";");
			while (rs.next()) {
				other_user = rs.getString("username");
			}
			rs.close();
			// }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return other_user;
	}

	// Permet d'ajouter un message � l'historique de conversation dans la BDD
	public static void addHistory(String other_user, List<Message> messages) {
		try {
			String other_id;
			if (other_user.equals("USER_1"))
				other_id = "19";
			else
				other_id = DistributedDataController.getIDByName(other_user).getDB_ID();
			for (Message message : messages) {
				statement.executeUpdate("INSERT INTO histories(source,destination,time,content) VALUES (" + getDB_ID()
						+ "," + other_id + ",'" + message.getTime() + "','" + message.getText() + "'); ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setDB_ID(String id) {
		user_id = id;
	}

	// V�rifie l'identit� de l'utilisateur pour pouvoir acc�der � ses donn�es
	public static boolean AuthCheck(String id, String password) {
		boolean R = false;
		try {
			ResultSet RS = statement
					.executeQuery("SELECT * FROM IDs WHERE id='" + id + "' AND password='" + password + "';");
			R = RS.next();
			MainController.NO_GUI_debugPrint(R + "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return R;
	}

	// Permet de s'incrire dans la base de donn�e avec un pseudo et un mdp et
	// retourne l'identifiant unique (int)
	public static void signUp(String pseudo, String pswd) {
		try {
			int affected_rows = statement
					.executeUpdate("INSERT INTO IDs(password,username) VALUES ('" + pswd + "','" + pseudo + "') ; ");
			MainController.NO_GUI_debugPrint("Affected rows = " + affected_rows);
			ResultSet RS = statement.executeQuery("SELECT LAST_INSERT_ID() FROM IDs ;");
			RS.next();
			setDB_ID("" + RS.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Permet de mettre � jour le pseudo dans la base de donn�e
	public static void updateName(String new_name) {
		try {
			statement.executeUpdate("UPDATE IDs SET username='" + new_name + "' WHERE id='" + user_id + "'; ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Une m�thode priv�e pour g�n�rer une requ�te SQL d'obtention d'ID � partir du
	// pseudo
	@SuppressWarnings("unused")
	private static String SQL_getIDNumber(String name_input) {
		return "SELECT id FROM IDs WHERE username='" + name_input + "';";
	}

}
