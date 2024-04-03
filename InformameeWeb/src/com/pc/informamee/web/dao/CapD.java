package com.pc.informamee.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CapD {

    Connection DbConn;

    public CapD(Connection DbConn) {
        this.DbConn = DbConn;
    }

    public boolean isValid(int CapToCheck) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement("SELECT cap FROM capgeo WHERE cap = ? LIMIT 1");
        PS.setInt(1, CapToCheck);
        ResultSet RS = PS.executeQuery();
        Boolean Res = RS.next();
        RS.close();
        PS.close();
        return Res;
    }

    public List<Integer> ReverseGeoTag(float minlat, float minlng, float maxlat, float maxlng) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement("SELECT cap FROM capgeo WHERE id IN ( SELECT id FROM geo WHERE minlat >= ? AND minlng >= ? AND maxlat <= ?  AND maxlng <= ? )");
        PS.setFloat(1, minlat);
        PS.setFloat(2, minlng);
        PS.setFloat(3, maxlat);
        PS.setFloat(4, maxlng);

        ResultSet RS = PS.executeQuery();
        List<Integer> Res = new ArrayList<>();
        while (RS.next()) {
            Res.add(RS.getInt("cap"));
        }
        RS.close();
        PS.close();
        return Res;
    }

    public List<Float> GeoTag(int Cap) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement("SELECT minlat, minlng, maxlat, maxlng FROM geo WHERE id IN (SELECT id FROM capgeo WHERE cap = ? )");
        PS.setInt(1, Cap);

        ResultSet RS = PS.executeQuery();
        List<Float> Res = new ArrayList<>(4);
        while (RS.next()) {
            Res.add(RS.getFloat(1));
            Res.add(RS.getFloat(2));
            Res.add(RS.getFloat(3));
            Res.add(RS.getFloat(4));
        }
        RS.close();
        PS.close();
        return Res;
    }
}
/*
    public List<Integer> ReverseGeo(float Lat, float Lng) throws SQLException {
        PreparedStatement PS = DbConn.prepareStatement("SELECT cap FROM capgeo WHERE id IN ( SELECT id FROM geo WHERE MinLat >= ? AND minlng >= ? AND maxlat <= ?  AND maxlng <= ? )");
        PS.setFloat(1, MinLat);
        PS.setFloat(2, minlng);
        PS.setFloat(3, maxlat);
        PS.setFloat(4, maxlng);

        ResultSet RS = PS.executeQuery();
        List<Integer> Res = new ArrayList<>();
        while (RS.next()) {
            Res.add(RS.getInt("cap"));
        }
        RS.close();
        return Res;
    }
}
*/