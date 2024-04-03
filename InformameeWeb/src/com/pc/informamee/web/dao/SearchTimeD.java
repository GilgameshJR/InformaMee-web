package com.pc.informamee.web.dao;

import com.pc.informamee.web.beans.ResultContainer;
import com.pc.informamee.web.beans.SeismicEvent;
import com.pc.informamee.web.beans.TerroristEvent;
import com.pc.informamee.web.beans.WeatherEvent;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SearchTimeD extends SearchD {
    protected final static String DANGER_Q = " AND event.danger = ?";
    protected final static String WEATHERTYPE = " AND weatherevent.type = ?";
    protected final static String CAPTIME = " AND event.eventid IN (SELECT eventid FROM happeningplace WHERE cap = ? ";

    protected SearchTimeD(Connection DbConn) {
        super(DbConn);
    }

    protected List<TerroristEvent> TerroristEvFactory(PreparedStatement PS) throws SQLException {
        ResultSet rs = PS.executeQuery();
        List<TerroristEvent> TEvents = new ArrayList<TerroristEvent>();
        while (rs.next()) {
            TerroristEvent current = new TerroristEvent();
            current.setId(rs.getInt("eventid"));
            current.setDanger(rs.getInt("danger"));
            current.setDescription(rs.getString("description"));
            current.setBeginTime(rs.getTimestamp("begintime"));
            current.setEndTime(rs.getTimestamp("endtime"));
            List<Integer> InvolvedCap = getInvolvedCap(current.getId());
            current.setInvolvedCap(InvolvedCap);
            current.setInvolvedCapJson((new JSONArray(InvolvedCap)).toString());
            TEvents.add(current);
        }
        rs.close();
        PS.close();
        return TEvents;
    }
    protected List<WeatherEvent> WeatherEvFactory(PreparedStatement PS) throws SQLException {
        ResultSet rs = PS.executeQuery();
        List<WeatherEvent> TEvents = new ArrayList<WeatherEvent>();
        while (rs.next()) {
            WeatherEvent current = new WeatherEvent();
            current.setId(rs.getInt("eventid"));
            current.setDanger(rs.getInt("danger"));
            current.setDescription(rs.getString("description"));
            current.setBeginTime(rs.getTimestamp("begintime"));
            current.setEndTime(rs.getTimestamp("endtime"));
            current.setWindSpeed(rs.getFloat("windspeed"));
            current.setType(rs.getInt("type"));
            List<Integer> InvolvedCap = getInvolvedCap(current.getId());
            current.setInvolvedCap(InvolvedCap);
            current.setInvolvedCapJson((new JSONArray(InvolvedCap)).toString());

            TEvents.add(current);
        }
        rs.close();
        PS.close();
        return TEvents;
    }
    protected List<SeismicEvent> SeismicEvFactory(PreparedStatement PS) throws SQLException {
        ResultSet rs = PS.executeQuery();
        List<SeismicEvent> TEvents = new ArrayList<SeismicEvent>();
        while (rs.next()) {
            SeismicEvent current = new SeismicEvent();
            current.setId(rs.getInt("eventid"));
            current.setDanger(rs.getInt("danger"));
            current.setDescription(rs.getString("description"));
            current.setBeginTime(rs.getTimestamp("begintime"));
            current.setEndTime(rs.getTimestamp("endtime"));
            Integer epicentrecap = rs.getInt("epicentrecap");
            current.setEpicentreCAP(epicentrecap);
            List<Integer> epicentrecapArr = new ArrayList<>(1);
            epicentrecapArr.add(epicentrecap);
            current.setEpicentreCAPJson((new JSONArray(epicentrecapArr)).toString());
            current.setMercalliMagnitude(rs.getFloat("mercallimagnitude"));
            current.setRichterMagnitude(rs.getInt("richtermagnitude"));
            List<Integer> InvolvedCap = getInvolvedCap(current.getId());
            current.setInvolvedCap(InvolvedCap);
            current.setInvolvedCapJson((new JSONArray(InvolvedCap)).toString());

            TEvents.add(current);
        }
        rs.close();
        PS.close();
        return TEvents;
    }

    protected String CreateMoreCapPS(int CapListSize) {
        String CapTimeBase = "OR cap = ? ";
        StringBuilder CapTimeFinal = new StringBuilder((CapTimeBase.length() * (CapListSize - 1)) + 1);
        for (int i = 1; i < CapListSize; i++) {
            CapTimeFinal.append(CapTimeBase);
        }
        CapTimeFinal.append(")");
        String CapTimeAddit = CapTimeFinal.toString();
        return CapTimeAddit;
    }

    protected abstract PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger) throws SQLException;

    protected abstract PreparedStatement CapFiller(PreparedStatement ToFill, List<Integer> CAP, int Danger, int WeatherType) throws SQLException;

    protected abstract PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger) throws SQLException;

    protected abstract PreparedStatement GlobalFiller(PreparedStatement ToFill, int Danger, int WeatherType) throws SQLException;

    protected abstract String getTimeQuery();

    public final ResultContainer FindByCap(List<Integer> CAPList, boolean isWeather, boolean isTerrorist, boolean isSeismic, int Danger, int WeatherType) throws SQLException {
        ResultContainer ToReturn = new ResultContainer();
        PreparedStatement SS = null;
        PreparedStatement WS = null;
        PreparedStatement TS = null;
        String CapTimeAddit = CreateMoreCapPS(CAPList.size());
        String TimeQuery = getTimeQuery();
        if (Danger == -1) {
            if (isSeismic) {
                SS = CapFiller(DbConn.prepareStatement(SEISMIC_QUERY + TimeQuery + CAPTIME + CapTimeAddit), CAPList, Danger);
                ToReturn.setSeismic(SeismicEvFactory(SS));
            }
            if (isWeather) {
                if (WeatherType == -1)
                    WS = CapFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + CAPTIME + CapTimeAddit), CAPList, Danger);
                else
                    WS = CapFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + CAPTIME + CapTimeAddit + WEATHERTYPE), CAPList, Danger, WeatherType);
                ToReturn.setWeather(WeatherEvFactory(WS));
            }
            if (isTerrorist) {
                TS = CapFiller(DbConn.prepareStatement(TERRORIST_QUERY + TimeQuery + CAPTIME + CapTimeAddit), CAPList, Danger);
                ToReturn.setTerrorist(TerroristEvFactory(TS));
            }
        } else {
            if (isSeismic) {
                SS = CapFiller(DbConn.prepareStatement(SEISMIC_QUERY + TimeQuery + CAPTIME + CapTimeAddit + DANGER_Q), CAPList, Danger);
                ToReturn.setSeismic(SeismicEvFactory(SS));
            }
            if (isWeather) {
                if (WeatherType == -1)
                    WS = CapFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + CAPTIME + CapTimeAddit + DANGER_Q), CAPList, Danger);
                else
                    WS = CapFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + CAPTIME + CapTimeAddit + DANGER_Q + WEATHERTYPE), CAPList, Danger, WeatherType);
                ToReturn.setWeather(WeatherEvFactory(WS));
            }
            if (isTerrorist) {
                TS = CapFiller(DbConn.prepareStatement(TERRORIST_QUERY + TimeQuery + CAPTIME + CapTimeAddit + DANGER_Q), CAPList, Danger);
                ToReturn.setTerrorist(TerroristEvFactory(TS));
            }
        }
        return ToReturn;
    }

    public final ResultContainer FindGlobal(boolean isWeather, boolean isTerrorist, boolean isSeismic, int Danger, int WeatherType) throws SQLException {
        ResultContainer ToReturn = new ResultContainer();
        PreparedStatement SS = null;
        PreparedStatement WS = null;
        PreparedStatement TS = null;
        String TimeQuery = getTimeQuery();
        if (Danger == -1) {
            if (isSeismic) {
                SS = GlobalFiller(DbConn.prepareStatement(SEISMIC_QUERY + TimeQuery), Danger);
                ToReturn.setSeismic(SeismicEvFactory(SS));
            }
            if (isWeather) {
                if (WeatherType == -1)
                    WS = GlobalFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery), Danger);
                else
                    WS = GlobalFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + WEATHERTYPE), Danger, WeatherType);
                ToReturn.setWeather(WeatherEvFactory(WS));
            }
            if (isTerrorist) {
                TS = GlobalFiller(DbConn.prepareStatement(TERRORIST_QUERY + TimeQuery), Danger);
                ToReturn.setTerrorist(TerroristEvFactory(TS));
            }
        } else {
            if (isSeismic) {
                SS = GlobalFiller(DbConn.prepareStatement(SEISMIC_QUERY + TimeQuery + DANGER_Q), Danger);
                ToReturn.setSeismic(SeismicEvFactory(SS));
            }
            if (isWeather) {
                if (WeatherType == -1)
                    WS = GlobalFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + DANGER_Q), Danger);
                else
                    WS = GlobalFiller(DbConn.prepareStatement(WEATHER_QUERY + TimeQuery + DANGER_Q + WEATHERTYPE), Danger, WeatherType);

                ToReturn.setWeather(WeatherEvFactory(WS));
            }
            if (isTerrorist) {
                TS = GlobalFiller(DbConn.prepareStatement(TERRORIST_QUERY + TimeQuery + DANGER_Q), Danger);
                ToReturn.setTerrorist(TerroristEvFactory(TS));
            }
        }
        return ToReturn;
    }
}
