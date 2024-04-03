package com.pc.informamee.web.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.dao.CapD;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class CapCheckC extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String CapStr = req.getParameter("cap");
        String ContextPath = req.getContextPath();
        PrintWriter PW = res.getWriter();
        if (CapStr == null) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } else {
            Connection DbConn = null;
            try {
                DbConn = DbConnectionHandler.getConnection();
                CapD CCInst = new CapD(DbConn);
                String ReqDispatcherStr;
                if (CCInst.isValid(Integer.parseInt(CapStr)))
                    ReqDispatcherStr = "CapOkResponseString.jsp";
                else
                    ReqDispatcherStr = "CapInexistentResponseString.jsp";
                RequestDispatcher RD = req.getRequestDispatcher(ReqDispatcherStr);
                RD.forward(req, res);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                res.sendRedirect(ContextPath + "/error?code=5");
            } catch (SQLException e) {
                res.sendRedirect(ContextPath + "/error?code=3");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                res.sendRedirect(ContextPath + "/error?code=2");
            } catch (NullPointerException e) {
                e.printStackTrace();
                res.sendRedirect(ContextPath + "/error?code=4");
            } finally {
                DbConnectionHandler.disposeConnection(DbConn);
            }
        }
    }
}
