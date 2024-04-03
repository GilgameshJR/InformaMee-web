package com.pc.informamee.web.editor.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.editor.dao.DeleteD;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteC extends HttpServlet {
    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String EventIdStr=req.getParameter("id");
        //String TypeStr = req.getParameter("type");
        String ContextPath = req.getContextPath();
        Connection DbConn = null;
        try {
            DbConn=DbConnectionHandler.getConnection();
            DeleteD DDInst = new DeleteD(DbConn);
            /*switch (TypeStr) {
                case "weather": {
                    DDInst.deleteWeather();
                    res.getWriter().println("EVENTO METEOROLOGICO ELIMINATO");
                    break;
                }
                case "seismic": {
                    DDInst.deleteSeismic();
                    res.getWriter().println("EVENTO SISMICO ELIMINATO");
                    break;
                }
                case "terrorist": {
                    DDInst.deleteTerrorist();
                    res.getWriter().println("EVENTO TERRORISTICO ELIMINATO");
                    break;
                }
                default: throw new MalformedURLException();
            }*/
            DDInst.deleteEvent(Integer.parseInt(EventIdStr));
            res.sendRedirect("index");
        } catch (SQLException e) {
            res.sendRedirect(ContextPath + "/error?code=3");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=5");
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } catch (NullPointerException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=4");
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}
