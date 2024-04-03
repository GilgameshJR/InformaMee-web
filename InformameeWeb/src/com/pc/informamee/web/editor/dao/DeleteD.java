package com.pc.informamee.web.editor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteD {

    Connection DbConn;
    private final String EVENT_DELETE = "DELETE FROM event WHERE eventid = ?";
    /*
    private final String EVENT_DELETE_CAP = "DELETE FROM happeningplace WHERE eventid = ?";
    private final String WEATHER_DELETE = "DELETE FROM weatherevent WHERE eventid= ?";
    private final String SEISMIC_DELETE = "DELETE FROM seismicevent WHERE eventid= ?";
    private final String TERRORIST_DELETE = "DELETE FROM terroristevent WHERE eventid= ?";*/
    //int eventid;

    public DeleteD(Connection DbConn) {
        this.DbConn = DbConn;
        //this.eventid = eventid;
    }

    public void deleteEvent(int eventid) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement(EVENT_DELETE);
        PS.setInt(1, eventid);
        PS.executeUpdate();
        /*
        PreparedStatement PSCap = DbConn.prepareStatement(EVENT_DELETE_CAP);
        PSCap.setInt(1, eventid);
        PSCap.executeUpdate();*/
    }
/*
    public void deleteWeather() throws SQLException {
        deleteEvent();
        PreparedStatement PS=DbConn.prepareStatement(WEATHER_DELETE);
        PS.setInt(1, eventid);
        PS.executeUpdate();
    }

    public void deleteSeismic() throws SQLException {
        deleteEvent();
        PreparedStatement PS=DbConn.prepareStatement(SEISMIC_DELETE);
        PS.setInt(1, eventid);
        PS.executeUpdate();
    }

    public void deleteTerrorist() throws SQLException {
        deleteEvent();
        PreparedStatement PS=DbConn.prepareStatement(TERRORIST_DELETE);
        PS.setInt(1, eventid);
        PS.executeUpdate();
    }
*/
}
