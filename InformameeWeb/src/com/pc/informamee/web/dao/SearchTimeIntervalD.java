package com.pc.informamee.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SearchTimeIntervalD extends SearchTimeD {
    private Date Begin;
    private Date End;

    private final String TIME_INTERVAL_QUERY = " WHERE event.begintime < ? AND event.endtime > ?";
    //1 END, 2BEGIN

    public SearchTimeIntervalD(Connection Conn, Date Begin, Date End) {
        super(Conn);
        this.Begin = Begin;
        this.End = End;
    }

    @Override
    protected PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(End.getTime()));
        ToFill.setTimestamp(2, new Timestamp(Begin.getTime()));
        for (int i = 0; i < CAP.size(); i++) {
            ToFill.setInt(i + 3, CAP.get(i));
        }
        if (Danger != -1)
            ToFill.setInt(CAP.size() + 3, Danger);
        return ToFill;
    }

    @Override
    protected PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger, int WeatherType) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(End.getTime()));
        ToFill.setTimestamp(2, new Timestamp(Begin.getTime()));
        for (int i = 0; i < CAP.size(); i++) {
            ToFill.setInt(i + 3, CAP.get(i));
        }
        int startingpt = CAP.size() + 3;
        if (Danger != -1) {
            ToFill.setInt(startingpt, Danger);
            ToFill.setInt(startingpt + 1, WeatherType);
        } else {
            ToFill.setInt(startingpt, WeatherType);
        }
        return ToFill;
    }

    @Override
    protected String getTimeQuery() {
        return TIME_INTERVAL_QUERY;
    }

    @Override
    protected PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(End.getTime()));
        ToFill.setTimestamp(2, new Timestamp(Begin.getTime()));
        if (Danger != -1)
            ToFill.setInt(3, Danger);
        return ToFill;
    }

    @Override
    protected PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger, int WeatherType) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(End.getTime()));
        ToFill.setTimestamp(2, new Timestamp(Begin.getTime()));
        if (Danger != -1) {
            ToFill.setInt(3, Danger);
            ToFill.setInt(4, WeatherType);
        } else {
            ToFill.setInt(3, WeatherType);
        }
        return ToFill;
    }
}
