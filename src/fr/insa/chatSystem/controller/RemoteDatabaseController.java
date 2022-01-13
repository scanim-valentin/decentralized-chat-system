package fr.insa.chatSystem.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class RemoteDatabaseController {
    static final private String DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver" ;
    static final private String DATABASE = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_004" ;
    static private Connection connection = null ;
    static private Statement statement = null ;

    public static void initializeConnection(String username, String password) {
        try {
            MainController.NO_GUI_debugPrint("Connecting to "+DATABASE);
            connection = DriverManager.getConnection(DATABASE,username,password);
            statement = connection.createStatement() ;
            statement.executeUpdate("USE tp_servlet_004;") ;
            MainController.NO_GUI_debugPrint("Succcess");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void endConnection() {
        try {
            connection.close() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    private static int getUniqueID(UserID other_user) {
        try {
            ResultSet rs = statement.executeQuery("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
