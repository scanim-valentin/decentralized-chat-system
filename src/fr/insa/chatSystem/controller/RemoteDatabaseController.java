package fr.insa.chatSystem.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.insa.chatSystem.Model.UserID;

public abstract class RemoteDatabaseController {
    static final private String DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver" ;
    static final private String DATABASE = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_004" ;
    static private Connection connection = null ;
    static private Statement statement = null ;

    public static void initializeConnection(String username, String password) {
        try {
            MainController.NO_GUI_debugPrint("Connecting to "+DATABASE);
            //connection = DriverManager.getConnection(DATABASE,username,password);
            connection = DriverManager.getConnection(DATABASE,"tp_servlet_004","ish6uo2U");
            statement = connection.createStatement() ;
            statement.executeUpdate("USE tp_servlet_004;") ;
            MainController.NO_GUI_debugPrint("Succcess connected to "+DATABASE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void endConnection() {
        try {
            connection.close() ;
            MainController.NO_GUI_debugPrint("Succcesfully disconnected from "+DATABASE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getHistory(String username1, String username2) {
        try {
            ResultSet rs = statement.executeQuery("SELECT history FROM histories WHERE id1=(SELECT id FROM IDs WHERE username=\""+username1+"\") AND id2=(SELECT id FROM IDs WHERE username=\""+username2+"\");");
            while(rs.next()) {
            	MainController.NO_GUI_debugPrint(rs.getString(1)+":");
            	MainController.NO_GUI_debugPrint(""+rs.getInt("history"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
