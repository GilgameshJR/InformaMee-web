package com.pc.informamee.web.editor.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.beans.Event;
import com.pc.informamee.web.dao.SearchNoTimeD;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

public class GetEditorC extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String IdStr = req.getParameter("id");
        String TypeStr = req.getParameter("type");
        String ContextPath = req.getContextPath();
        Connection DbConn = null;
        try {
            int Id=Integer.parseInt(IdStr);
            DbConn = DbConnectionHandler.getConnection();
            SearchNoTimeD SDInst = new SearchNoTimeD(DbConn);
            Event Res;
            switch (TypeStr) {
                case "weather": {
                    Res = SDInst.getWeather(Id);
                    req.setAttribute("type", 1);
                    req.setAttribute("typename", "weather");
                    break;
                }
                case "seismic": {
                    Res = SDInst.getSeismic(Id);
                    req.setAttribute("type", 2);
                    req.setAttribute("typename", "seismic");
                    break;
                }
                case "terrorist": {
                    Res = SDInst.getTerrorist(Id);
                    req.setAttribute("type", 3);
                    req.setAttribute("typename", "terrorist");
                    break;
                }
                default:
                    throw new MalformedURLException();
            }
            if (Res==null)
                throw new MalformedURLException();
            req.setAttribute("result", Res);
            req.getRequestDispatcher("editorExisting.jsp").forward(req, res);
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=3");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=5");
        } catch (NullPointerException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=4");
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}
