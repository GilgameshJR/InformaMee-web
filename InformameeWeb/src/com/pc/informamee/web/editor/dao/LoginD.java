package com.pc.informamee.web.editor.dao;

import com.pc.informamee.web.editor.beans.Credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginD {
    private Connection connection;
    public LoginD(Connection DbConnection) {
        this.connection=DbConnection;
    }
    public Credentials CheckCredentials(int ID, String Password) throws SQLException {
        PreparedStatement PS = connection.prepareStatement("SELECT * FROM forecast WHERE forecastid=? AND password=?");
        PS.setInt(1,ID);
        PS.setString(2,Password);
        ResultSet RS=PS.executeQuery();
        if (RS.next()) {
            Credentials CurrCr=new Credentials();
            CurrCr.setId(RS.getInt("forecastid"));
            CurrCr.setPassword(RS.getString("Password"));
            return CurrCr;
        }
        return null;
    }
}