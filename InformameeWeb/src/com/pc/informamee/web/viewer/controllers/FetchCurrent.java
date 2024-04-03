package com.pc.informamee.web.viewer.controllers;

import com.pc.informamee.web.CapParseException;
import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.MultiCapParser;
import com.pc.informamee.web.beans.ResultContainer;
import com.pc.informamee.web.dao.CapD;
import com.pc.informamee.web.dao.SearchTimeInstantD;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class FetchCurrent extends HttpServlet {

    private void HandleReq(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection DbConn = null;
        String ContextPath = req.getContextPath();
        try {
            DbConn = DbConnectionHandler.getConnection();
            String CapStr = req.getParameter("cap");

            List<Integer> CapList = MultiCapParser.ParseCap(CapStr);

            CapD CCDAOInst = new CapD(DbConn);
            Calendar CurrCal = Calendar.getInstance();
            SearchTimeInstantD CurrSearchDAO = new SearchTimeInstantD(DbConn, CurrCal.getTime());

            for (Integer Cap : CapList) {
                if (!CCDAOInst.isValid(Cap))
                    throw new MalformedURLException();
            }
            ResultContainer Res = CurrSearchDAO.FindCurrent(CapList);
            req.setAttribute("capsearch", CapStr);
            req.setAttribute("results", Res);
            RequestDispatcher fwd = req.getRequestDispatcher("SearchCurrentRes.jsp");
            fwd.forward(req, res);
        } catch (ClassNotFoundException e) {
            res.sendRedirect(ContextPath + "/error?code=5");
            e.printStackTrace();
        } catch (SQLException e) {
            res.sendRedirect(ContextPath + "/error?code=3");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } catch (CapParseException e) {
            res.sendRedirect(ContextPath + "/error?code=6");
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HandleReq(req, res);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HandleReq(req, res);
    }
}
