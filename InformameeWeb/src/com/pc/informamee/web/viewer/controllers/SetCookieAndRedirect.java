package com.pc.informamee.web.viewer.controllers;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.dao.CapD;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

public class SetCookieAndRedirect extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String CapStr=req.getParameter("cap");
        if (CapStr==null) throw new MalformedURLException();
        String StoreStr = req.getParameter("store");
        Connection DbConn = null;
        String ContextPath = req.getContextPath();
        try {
            DbConn= DbConnectionHandler.getConnection();
            CapStr=CapStr.trim();
            if (CapStr.length()==0) throw new MalformedURLException();
            int Cap = Integer.parseInt(CapStr);
            CapD CCDao = new CapD(DbConn);
            if (CCDao.isValid(Cap)) {
                    Cookie[] CookieContainer = req.getCookies();
                    for (int i = 0; CookieContainer != null && i < CookieContainer.length; i++) {
                        if (CookieContainer[i].getName().equals("currentcap")) {
                            CookieContainer[i].setMaxAge(0);
                            res.addCookie( CookieContainer[i] );
                            }
                    }
                    if (StoreStr != null && StoreStr.equals("on")) {
                        Cookie CapCookie = new Cookie("currentcap", CapStr.trim());
                        CapCookie.setMaxAge(999999999);
                        res.addCookie(CapCookie);
                    }
                req.getSession().setAttribute("currentcap", Cap);
                res.sendRedirect("index");
            } else {
                res.getWriter().println("CAP INESISTENTE");
            }
        } catch (SQLException e) {
            res.sendRedirect(ContextPath + "/error?code=3");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            res.sendRedirect(ContextPath + "/error?code=5");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        }
        finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}
