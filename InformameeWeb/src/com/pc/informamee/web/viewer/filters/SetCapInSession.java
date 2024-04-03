package com.pc.informamee.web.viewer.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class SetCapInSession extends HttpFilter {
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpSession sess=req.getSession();
        Integer CurrentCap=(Integer) sess.getAttribute("currentcap");
        try {
            if(CurrentCap==null) {
                Cookie[] CookieContainer = req.getCookies();
                for (int i = 0; CookieContainer != null && i < CookieContainer.length; i++) {
                    if (CookieContainer[i].getName().equals("currentcap")) {
                        sess.setAttribute("currentcap", Integer.parseInt(CookieContainer[i].getValue()));
                        break;
                    }
                }
            }
            chain.doFilter(req, res);
        } catch (NumberFormatException e) {
            //handle
        }
    }
}
