package com.pc.informamee.web.viewer.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Index extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession sess=req.getSession();
        Integer CurrentCap= (Integer) sess.getAttribute("currentcap");

        if (CurrentCap==null) {
            res.sendRedirect("SetDefaultCap.jsp");
        } else {
            res.sendRedirect("FetchCurrent?cap=" + CurrentCap);
        }
    }
}
