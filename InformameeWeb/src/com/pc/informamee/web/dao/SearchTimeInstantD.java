package com.pc.informamee.web.dao;

import com.pc.informamee.web.beans.ResultContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SearchTimeInstantD extends SearchTimeD {
    private Date Instant;

    //DB RELATED THINGS_____________________________________________________________________________________________
    private final String TIME_INSTANT_QUERY = " WHERE ?  BETWEEN event.begintime AND event.endtime ";

    public SearchTimeInstantD(Connection Conn, Date Instant) {
        super(Conn);
        this.Instant = Instant;
    }

    //CURRENT REQUEST RELATED THINGS________________________________________________
    private final String CURRENT_REQUEST = " WHERE ? < event.endtime" + " AND event.eventid IN" + "(SELECT eventid FROM happeningplace WHERE cap = ? ";

    //CAP REQUEST RELATED THINGS_________________________________________________________________________________
    @Override
    protected PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger, int WeatherType) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        for (int i = 0; i < CAP.size(); i++) {
            ToFill.setInt(i + 2, CAP.get(i));
        }
        int startingpt = CAP.size() + 2;
        if (Danger != -1) {
            ToFill.setInt(startingpt, Danger);
            ToFill.setInt(startingpt + 1, WeatherType);
        } else {
            ToFill.setInt(startingpt, WeatherType);
        }
        return ToFill;
    }

    @Override
    protected PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        for (int i = 0; i < CAP.size(); i++) {
            ToFill.setInt(i + 2, CAP.get(i));
        }
        if (Danger != -1)
            ToFill.setInt(CAP.size() + 2, Danger);
        return ToFill;
    }

    @Override
    protected String getTimeQuery() {
        return TIME_INSTANT_QUERY;
    }

    //GLOBAL REQUEST RELATED THINGS________________________________________________________________________
    @Override
    protected PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger, int WeatherType) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        if (Danger != -1) {
            ToFill.setInt(2, Danger);
            ToFill.setInt(3, WeatherType);
        } else {
            ToFill.setInt(2, WeatherType);
        }
        return ToFill;
    }

    @Override
    protected PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        if (Danger != -1)
            ToFill.setInt(2, Danger);
        return ToFill;
    }

    private PreparedStatement CurrentFiller(PreparedStatement ToFill, List<Integer> Cap) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        for (int i = 0; i < Cap.size(); i++) {
            ToFill.setInt(i + 2, Cap.get(i));
        }
        return ToFill;
    }

    public ResultContainer FindCurrent(List<Integer> Cap) throws SQLException {
        ResultContainer ToReturn = new ResultContainer();
        PreparedStatement SS = CurrentFiller(DbConn.prepareStatement(SEISMIC_QUERY + CURRENT_REQUEST + CreateMoreCapPS(Cap.size())), Cap);
        PreparedStatement WS = CurrentFiller(DbConn.prepareStatement(WEATHER_QUERY + CURRENT_REQUEST + CreateMoreCapPS(Cap.size())), Cap);
        PreparedStatement TS = CurrentFiller(DbConn.prepareStatement(TERRORIST_QUERY + CURRENT_REQUEST + CreateMoreCapPS(Cap.size())), Cap);

        ToReturn.setTerrorist(TerroristEvFactory(TS));
        ToReturn.setWeather(WeatherEvFactory(WS));
        ToReturn.setSeismic(SeismicEvFactory(SS));
        return ToReturn;
    }

    //EDITABLEBYFORECAST RELATED THINGS
    private final String STANDARD_FORECAST = //select all Events that are not ended yet
            " WHERE ? < event.endtime" +
                    " AND event.forecastid = ?";

    private PreparedStatement EditableFiller(PreparedStatement ToFill, int forecastid) throws SQLException {
        ToFill.setTimestamp(1, new Timestamp(Instant.getTime()));
        ToFill.setInt(2, forecastid);
        return ToFill;
    }

    public ResultContainer getEditable(int forecastid) throws SQLException {
        ResultContainer ToReturn = new ResultContainer();
        PreparedStatement SS = EditableFiller(DbConn.prepareStatement(SEISMIC_QUERY + STANDARD_FORECAST), forecastid);
        PreparedStatement WS = EditableFiller(DbConn.prepareStatement(WEATHER_QUERY + STANDARD_FORECAST), forecastid);
        PreparedStatement TS = EditableFiller(DbConn.prepareStatement(TERRORIST_QUERY + STANDARD_FORECAST), forecastid);

        ToReturn.setWeather(WeatherEvFactory(WS));
        ToReturn.setSeismic(SeismicEvFactory(SS));
        ToReturn.setTerrorist(TerroristEvFactory(TS));

        return ToReturn;
    }

}

