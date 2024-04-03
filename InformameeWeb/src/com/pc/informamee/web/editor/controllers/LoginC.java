package com.pc.informamee.web.editor.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.editor.beans.Credentials;
import com.pc.informamee.web.editor.dao.LoginD;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginC extends HttpServlet {

    public void doPost (HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String id=req.getParameter("id");
        String pwd=req.getParameter("pwd");

        String ContextPath = req.getContextPath();
        Connection DbConn = null;

        if (id!=null && pwd!=null) {
            try {
                DbConn= DbConnectionHandler.getConnection();
                LoginD LogDao = new LoginD(DbConn);
                PrintWriter ResWriter = res.getWriter();
                Credentials User = LogDao.CheckCredentials(Integer.parseInt(id), pwd);
                if (User==null) {
                    res.setStatus(401);
                    ResWriter.println("Credenziali errate");
                } else {
                    req.getSession().setAttribute("currentuser", User);
                    res.setStatus(200);
                    ResWriter.println(ContextPath + "/editor/priv/index");
                }
            } catch (MalformedURLException e) {
                res.sendRedirect(ContextPath + "/error?code=1");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                res.sendRedirect(ContextPath + "/error?code=5");
            } catch (SQLException e) {
                e.printStackTrace();
                res.sendRedirect(ContextPath + "/error?code=3");
            } catch (NullPointerException e) {
                e.printStackTrace();
                res.sendRedirect(ContextPath + "/error?code=4");
            } catch (NumberFormatException e) {
                res.setStatus(401);
                res.getWriter().println("L'ID deve essere esclusivamente numerico");
            } finally {
                DbConnectionHandler.disposeConnection(DbConn);
            }
        } else {
            res.setStatus(401);
            res.getWriter().println("Ricevute credenziali vuote, compila i campi");
        }
    }
}
