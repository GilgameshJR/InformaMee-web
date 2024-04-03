package com.pc.informamee.web.dao;

import com.pc.informamee.web.beans.SeismicEvent;
import com.pc.informamee.web.beans.TerroristEvent;
import com.pc.informamee.web.beans.WeatherEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchNoTimeD extends SearchD {

    public SearchNoTimeD(Connection DbConn) {
        super(DbConn);
    }


    public final String eventid = "WHERE event.eventid = ?";

    public WeatherEvent getWeather(int Id) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement(WEATHER_QUERY + eventid);
        PS.setInt(1, Id);
        ResultSet RS=PS.executeQuery();
        if (RS.next()) {
            WeatherEvent current = new WeatherEvent();
            current.setId(RS.getInt("eventid"));
            current.setDanger(RS.getInt("danger"));
            current.setDescription(RS.getString("description"));
            current.setBeginTime(RS.getTimestamp("begintime"));
            current.setEndTime(RS.getTimestamp("endtime"));
            current.setWindSpeed(RS.getFloat("windspeed"));
            current.setType(RS.getInt("type"));

            current.setInvolvedCap(getInvolvedCap(Id));
            RS.close();
            PS.close();
            return current;
        }
        RS.close();
        PS.close();
        return null;
    }
    public SeismicEvent getSeismic(int Id) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement(SEISMIC_QUERY + eventid);
        PS.setInt(1, Id);
        ResultSet RS=PS.executeQuery();

        if (RS.next()) {
            SeismicEvent current = new SeismicEvent();
            current.setId(RS.getInt("eventid"));
            current.setDanger(RS.getInt("danger"));
            current.setDescription(RS.getString("description"));
            current.setBeginTime(RS.getTimestamp("begintime"));
            current.setEndTime(RS.getTimestamp("endtime"));
            current.setEpicentreCAP(RS.getInt("epicentrecap"));
            current.setMercalliMagnitude(RS.getFloat("mercallimagnitude"));
            current.setRichterMagnitude(RS.getInt("richtermagnitude"));

            current.setInvolvedCap(getInvolvedCap(Id));
            RS.close();
            PS.close();
            return current;
        }
        RS.close();
        PS.close();
        return null;
    }
    public TerroristEvent getTerrorist(int Id) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement(TERRORIST_QUERY + eventid);
        PS.setInt(1, Id);
        ResultSet RS=PS.executeQuery();

        if (RS.next()) {
            TerroristEvent current = new TerroristEvent();
            current.setId(RS.getInt("eventid"));
            current.setDanger(RS.getInt("danger"));
            current.setDescription(RS.getString("description"));
            current.setBeginTime(RS.getTimestamp("begintime"));
            current.setEndTime(RS.getTimestamp("endtime"));

            current.setInvolvedCap(getInvolvedCap(Id));
            RS.close();
            PS.close();
            return current;
        }
        RS.close();
        PS.close();
        return null;
    }
}
