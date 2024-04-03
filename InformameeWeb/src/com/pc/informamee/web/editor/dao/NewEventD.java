package com.pc.informamee.web.editor.dao;

import java.sql.*;
import java.util.List;

public class NewEventD {
    private Connection connection;

    public NewEventD(Connection DbConn) {
        this.connection = DbConn;
    }

    private final String INSERT_EVENT_QUERY =
            "INSERT INTO Event (danger, description, begintime, endtime, forecastid) VALUES (?, ?, ?, ?, ?)";
    private final String RETRIEVE_ID_QUERY = "SELECT eventid FROM Event WHERE eventid = LAST_INSERT_ID()";
    private final String ADD_HAPPENING =
            "INSERT INTO happeningplace(eventid, cap) VALUES(?, ?)";
    private String WEATHER_INSERT_QUERY =
            "INSERT INTO weatherevent(eventid, windspeed, type)" +
                    "VALUES (?, ?, ?)";
    private String SEISMIC_INSERT_QUERY =
            "INSERT INTO seismicevent(eventid, richtermagnitude, mercallimagnitude, epicentrecap)" +
                    "VALUES (?, ?, ?, ?)";
    private String INSERT_QUERY =
            "INSERT INTO terroristevent(eventid)" +
                    "VALUES (?)";

    private int InsertEvent(int forecastid, List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime) throws SQLException {

        int eventid = 0;
        PreparedStatement ps = connection.prepareStatement(INSERT_EVENT_QUERY);
        ps.setInt(1, Danger);
        ps.setString(2, Description);
        ps.setTimestamp(3, begintime);
        ps.setTimestamp(4, endtime);
        ps.setInt(5, forecastid);
        Integer rs = ps.executeUpdate();

        PreparedStatement ps2 = connection.prepareStatement(RETRIEVE_ID_QUERY);
        ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            //TODO: check if the eventid is correctly retreived
            eventid = rs2.getInt(1); //1 is the index of the unique column the Query returns
        } else
            throw new SQLException("Couldn't retrieve the eventid");
        PreparedStatement ps3;
        for (Integer current : Caps) {
            ps3 = connection.prepareStatement(ADD_HAPPENING);
            ps3.setInt(1, eventid);
            ps3.setInt(2, current);
            ps3.executeUpdate();
        }
        return eventid;
    }

    public void InsertWeatherEvent(int forecastid, List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime, float windspeed, int Type) throws SQLException {
        int eventid = InsertEvent(forecastid, Caps, Danger, Description, begintime, endtime);
        PreparedStatement ps = connection.prepareStatement(WEATHER_INSERT_QUERY);
        ps.setInt(1, eventid);
        if (windspeed != 0)
            ps.setFloat(2, windspeed);
        else
            ps.setNull(2, Types.FLOAT);
        if (Type != 0)
            ps.setInt(3, Type);
        else
            ps.setNull(3, Types.INTEGER);
        int rs = ps.executeUpdate();
        System.out.println("DB insertion result: " + rs);
    }

    public void InsertSeismicEvent(int forecastid, List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime, double mercallimagnitude, double richtermagnitude, int epicentrecap) throws SQLException {
        int eventid = InsertEvent(forecastid, Caps, Danger, Description, begintime, endtime);
        PreparedStatement ps = connection.prepareStatement(SEISMIC_INSERT_QUERY);
        ps.setInt(1, eventid);
        if (richtermagnitude != 0)
            ps.setDouble(2, richtermagnitude);
        else
            ps.setNull(2, Types.FLOAT);
        if (mercallimagnitude != 0)
            ps.setDouble(3, mercallimagnitude);
        else
            ps.setNull(3, Types.FLOAT);
        if (epicentrecap != 0)
            ps.setDouble(4, epicentrecap);
        else
            ps.setNull(4, Types.INTEGER);
        int rs = ps.executeUpdate();
        System.out.println("DB insertion result: " + rs);
    }

    public void InsertTerroristEvent(int forecastid, List<Integer> Caps, int Danger, String Description, Timestamp begintime, Timestamp endtime) throws SQLException {
        int eventid = InsertEvent(forecastid, Caps, Danger, Description, begintime, endtime);

        PreparedStatement ps = connection.prepareStatement(INSERT_QUERY);
        ps.setInt(1, eventid);
        int rs = ps.executeUpdate();
        System.out.println("DB insertion result: " + rs);
    }
}
