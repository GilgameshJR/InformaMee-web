package com.pc.informamee.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SearchD {
    protected Connection DbConn;

    protected final static String EVENT_QUERY = "SELECT event.eventid, event.danger, event.description, event.begintime, event.endtime, event.forecastid";
    protected final static String SEISMIC_QUERY = EVENT_QUERY + ", seismicevent.richtermagnitude, seismicevent.mercallimagnitude, seismicevent.epicentrecap " + "FROM seismicevent JOIN event ON seismicevent.eventid=event.eventid ";

    protected final static String WEATHER_QUERY = EVENT_QUERY + ", weatherevent.type, weatherevent.windspeed" + "  FROM weatherevent JOIN event on weatherevent.eventid = event.eventid ";
    protected final static String TERRORIST_QUERY = EVENT_QUERY + " FROM terroristevent JOIN event ON terroristevent.eventid=event.eventid ";

    private final String GETINVOLVEDCAP = "SELECT cap FROM happeningplace WHERE eventid = ?";

    protected SearchD(Connection DbConn) {
        this.DbConn = DbConn;
    }

    protected List<Integer> getInvolvedCap(int eventid) throws SQLException {
        List<Integer> InvolvedCap = new ArrayList<Integer>();
        PreparedStatement CS = DbConn.prepareStatement(GETINVOLVEDCAP);
        CS.setInt(1, eventid);
        ResultSet CRS = CS.executeQuery();
        while (CRS.next()) {
            InvolvedCap.add(CRS.getInt("cap"));
        }
        CS.close();
        CRS.close();
        return InvolvedCap;
    }
}
