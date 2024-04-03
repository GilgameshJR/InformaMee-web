package com.pc.informamee.web.editor.filters;

import com.pc.informamee.web.editor.beans.Credentials;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoggedCheck extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession sess = req.getSession();
        String ContextPath = req.getContextPath();

        if (sess == null || sess.isNew() || sess.getAttribute("currentuser") == null) {
            res.sendRedirect(ContextPath + "/editor/loginpage.html");
        } else {
            req.setAttribute("currentuserid", ((Credentials)sess.getAttribute("currentuser")).getId());
            chain.doFilter(req, res);
        }
    }
}
