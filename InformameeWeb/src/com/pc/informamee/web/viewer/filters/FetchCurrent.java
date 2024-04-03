package com.pc.informamee.web.viewer.filters;

import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.beans.ResultContainer;
import com.pc.informamee.web.dao.SearchTimeInstantD;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FetchCurrent extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession sess=req.getSession();
        Integer CurrentCap= (Integer) sess.getAttribute("currentcap");

        Connection DbConn=null;
        try {
            if (CurrentCap != null) {
                DbConn = DbConnectionHandler.getConnection();
                Calendar myCal = Calendar.getInstance();
                SearchTimeInstantD FRDao = new SearchTimeInstantD(DbConn, myCal.getTime());
                List<Integer> TempList = new ArrayList<>(1);
                TempList.add(CurrentCap);
                ResultContainer Res = FRDao.FindCurrent(TempList);

                req.setAttribute("wcurrcount", Res.getWeather().size());
                req.setAttribute("scurrcount", Res.getSeismic().size());
                req.setAttribute("tcurrcount", Res.getTerrorist().size());
            }
            chain.doFilter(req, res);
        } catch (SQLException e) {
            e.printStackTrace();
            //res.sendRedirect("/InformameeWeb_war_exploded/error?code=3");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //res.sendRedirect("/InformameeWeb_war_exploded/error?code=5");
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}
