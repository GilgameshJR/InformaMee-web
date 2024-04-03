package com.pc.informamee.web.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.dao.CapD;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReverseGeoTagPoint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String LatStr = req.getParameter("lat");
        String LngStr = req.getParameter("lng");
        Connection DbConn = null;
        String ContextPath = req.getContextPath();
        try {
            if (LatStr == null || LngStr == null)
                throw new MalformedURLException();
            Float Lat = Float.parseFloat(LatStr);
            Float Lng = Float.parseFloat(LngStr);
            DbConn = DbConnectionHandler.getConnection();
            CapD CapDInst = new CapD(DbConn);
            Float Delta = 0.040f;
            List<Integer> Res = CapDInst.ReverseGeoTag(Lat - Delta, Lng - Delta, Lat + Delta, Lng + Delta);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            JSONArray json = new JSONArray(Res);
            res.getWriter().println(json);
        } catch (NumberFormatException e) {
            res.sendRedirect(ContextPath + "/error?code=2");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
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



