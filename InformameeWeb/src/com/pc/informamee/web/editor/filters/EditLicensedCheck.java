package com.pc.informamee.web.editor.filters;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.editor.dao.EditD;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class EditLicensedCheck extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String IdStr=req.getParameter("id");
        if (IdStr==null || IdStr.isEmpty())
            throw new MalformedURLException();
        Connection DbConn;
        try {
            DbConn = DbConnectionHandler.getConnection();
            EditD EDInst=new EditD(DbConn, Integer.parseInt(IdStr));
            if ( EDInst.isLicensed ( ((int)req.getAttribute("currentuserid")) , Calendar.getInstance().getTime() ) )
                chain.doFilter(req, res);
            else
                res.getWriter().println("NOT LICENSED TO EDIT THIS EVENT");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
