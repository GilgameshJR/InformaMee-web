package com.pc.informamee.web.editor.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.beans.ResultContainer;
import com.pc.informamee.web.dao.SearchTimeInstantD;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class IndexC extends HttpServlet {
public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    Connection DbConn = null;
    String ContextPath = req.getContextPath();
    try {
        DbConn = DbConnectionHandler.getConnection();
        SearchTimeInstantD SDInst=new SearchTimeInstantD(DbConn, Calendar.getInstance().getTime());
        ResultContainer Res=SDInst.getEditable((int)req.getAttribute("currentuserid"));
        req.setAttribute("results", Res);
        req.getRequestDispatcher("index.jsp").forward(req, res);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        res.sendRedirect(ContextPath + "/error?code=5");
    } catch (SQLException e) {
        e.printStackTrace();
        res.sendRedirect(ContextPath + "/error?code=3");
    } catch (NullPointerException e) {
        e.printStackTrace();
        res.sendRedirect(ContextPath + "/error?code=4");
    } finally {
        DbConnectionHandler.disposeConnection(DbConn);
    }
}
}
