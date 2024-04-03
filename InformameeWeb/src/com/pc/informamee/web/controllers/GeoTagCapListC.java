package com.pc.informamee.web.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.dao.CapD;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeoTagCapListC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        BufferedReader reader = req.getReader();
        String line = null;
        while ((line = reader.readLine()) != null)
            jb.append(line);
        String ContextPath = req.getContextPath();
        Connection DbConn = null;
        try {
            JSONArray CapListJA = new JSONArray(jb.toString());
            DbConn = DbConnectionHandler.getConnection();
            List<List<Float>> Res = new ArrayList<>(CapListJA.length());
            for (int i = 0; i < CapListJA.length(); i++) {
                int CurrCap = CapListJA.getInt(i);
                CapD CapDInst = new CapD(DbConn);
                Res.add(CapDInst.GeoTag(CurrCap));
            }
            JSONArray ResJA = new JSONArray(Res);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().println(ResJA.toString());
        } catch (JSONException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
            e.printStackTrace();
        } catch (SQLException e) {
            res.sendRedirect(ContextPath + "/error?code=3");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            res.sendRedirect(ContextPath + "/error?code=5");
            e.printStackTrace();
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}
