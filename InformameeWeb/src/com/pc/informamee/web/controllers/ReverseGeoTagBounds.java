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

public class ReverseGeoTagBounds extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String MinLatStr = req.getParameter("minlat");
        String MinLngStr = req.getParameter("minlng");
        String MaxLatStr = req.getParameter("maxlat");
        String MaxLngStr = req.getParameter("maxlng");
        Connection DbConn = null;
        String ContextPath = req.getContextPath();
        try {
            if (MinLatStr == null || MinLngStr == null || MaxLatStr == null || MaxLngStr == null)
                throw new MalformedURLException();
            Float MinLat = Float.parseFloat(MinLatStr);
            Float MinLng = Float.parseFloat(MinLngStr);
            Float MaxLat = Float.parseFloat(MaxLatStr);
            Float MaxLng = Float.parseFloat(MaxLngStr);
            DbConn = DbConnectionHandler.getConnection();
            CapD CapDInst = new CapD(DbConn);
            List<Integer> Res = CapDInst.ReverseGeoTag(MinLat, MinLng, MaxLat, MaxLng);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            JSONArray json = new JSONArray(Res);
            res.getWriter().println(json);
        } catch (NumberFormatException e) {
            res.sendRedirect(ContextPath + "/error?code=2");
            //e.printStackTrace();
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