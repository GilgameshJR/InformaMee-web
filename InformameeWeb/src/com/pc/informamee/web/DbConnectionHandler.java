package com.pc.informamee.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHandler {

    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String DATABASE_URL = "jdbc:mysql://eu-cdbr-west-02.cleardb.net:3306/heroku_6930ed6834c5b31";
    private static String USER = "b05ba16e997f46";
    private static String PWD = "d4bf155e";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(DATABASE_URL, USER, PWD);
    }

    public static void disposeConnection(Connection ToDispose) {
        if (ToDispose != null) {
            try {
                ToDispose.close();
            } catch (SQLException e) {
                e.printStackTrace(); //ignore
            }
        }
    }
}
