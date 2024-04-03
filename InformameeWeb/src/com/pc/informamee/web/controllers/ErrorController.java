package com.pc.informamee.web.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorController extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String ErrorStr = req.getParameter("code");
            if (ErrorStr == null) {
                res.getWriter().println("RICHIESTA NON VALIDA, QUESTA E' UNA PAGINA DI SERVIZIO");
            }
            req.setAttribute("error", Integer.parseInt(ErrorStr));
            RequestDispatcher RD = req.getRequestDispatcher("error.jsp");
            RD.forward(req, res);
        } catch (NumberFormatException e) {
            res.getWriter().println("CODICE DI ERRORE NON VALIDO, QUESTA E' UNA PAGINA DI SERVIZIO");
        }
    }
}
