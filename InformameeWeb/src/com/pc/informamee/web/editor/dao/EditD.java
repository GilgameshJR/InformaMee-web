package com.pc.informamee.web.editor.dao;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class EditD {
    Connection DbConn;
    private final String EVENT_UPDATE_QUERY = "UPDATE event SET danger = ? , description = ? , begintime = ? , endtime = ? WHERE eventid = ?";
    private final String EVENT_DELETE_CAP = "DELETE FROM happeningplace WHERE eventid = ?";
    private final String WEATHER_UPDATE_QUERY = "UPDATE weatherevent SET type = ? , windspeed = ? WHERE eventid = ?";
    private final String SEISMIC_UPDATE_QUERY = "UPDATE seismicevent SET richtermagnitude = ? , mercallimagnitude = ? , epicentrecap = ? WHERE eventid = ?";
    private final String ADD_HAPPENING =
            "INSERT INTO happeningplace(eventid, cap) VALUES(?, ?)";
    int eventid;

    public EditD(Connection DbConnection, int eventid) {
        this.DbConn = DbConnection;
        this.eventid = eventid;
    }

    public boolean isLicensed(int forecastid, Date ReqTime) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement("SELECT * FROM event WHERE eventid = ? AND forecastid = ? AND ? < Event.endtime");
        PS.setInt(1, eventid);
        PS.setInt(2, forecastid);
        PS.setTimestamp(3, new Timestamp(ReqTime.getTime()));
        ResultSet RS = PS.executeQuery();
        if (RS.next())
            return true;

        return false;
    }

    private void updateEvent(List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement(EVENT_UPDATE_QUERY);
        PS.setInt(1, Danger);
        PS.setString(2, Description);
        PS.setTimestamp(3, begintime);
        PS.setTimestamp(4, endtime);
        PS.setInt(5, eventid);
        PS.executeUpdate();

        //first delete old cap
        PreparedStatement PSR = DbConn.prepareStatement(EVENT_DELETE_CAP);
        PSR.setInt(1, eventid);
        PSR.executeUpdate();
        //then insert new cap
        PreparedStatement ps3 = DbConn.prepareStatement(ADD_HAPPENING);
        for (Integer current : Caps) {
            ps3 = DbConn.prepareStatement(ADD_HAPPENING);
            ps3.setInt(1, eventid);
            ps3.setInt(2, current);
            ps3.executeUpdate();
        }
    }


    public void updateSeismic(List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime, float mercallimagnitude, float richtermagnitude, int epicentrecap) throws SQLException {
        updateEvent(Caps, Danger, Description, begintime, endtime);

        PreparedStatement PS = DbConn.prepareStatement(SEISMIC_UPDATE_QUERY);
        PS.setFloat(1, richtermagnitude);
        PS.setFloat(2, mercallimagnitude);
        PS.setInt(3, epicentrecap);
        PS.setInt(4, eventid);
        PS.executeUpdate();
    }

    public void updateWeather(List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime, float windspeed, int Type) throws SQLException {
        updateEvent(Caps, Danger, Description, begintime, endtime);

        PreparedStatement PS = DbConn.prepareStatement(WEATHER_UPDATE_QUERY);
        PS.setInt(1, Type);
        PS.setFloat(2, windspeed);
        PS.setInt(3, eventid);
        PS.executeUpdate();
    }

    public void updateTerrorist(List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime) throws SQLException {
        updateEvent(Caps, Danger, Description, begintime, endtime);
    }
}
