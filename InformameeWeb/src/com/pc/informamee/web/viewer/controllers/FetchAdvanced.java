package com.pc.informamee.web.viewer.controllers;

import com.pc.informamee.web.CapParseException;
import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.MultiCapParser;
import com.pc.informamee.web.beans.ResultContainer;
import com.pc.informamee.web.dao.CapD;
import com.pc.informamee.web.dao.SearchTimeD;
import com.pc.informamee.web.dao.SearchTimeInstantD;
import com.pc.informamee.web.dao.SearchTimeIntervalD;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class FetchAdvanced extends HttpServlet {

    private void HandleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String ContextPath = req.getContextPath();
        Connection DbConn = null;
        try {
            DbConn = DbConnectionHandler.getConnection();

            HttpSession sess = req.getSession();

            String TimecodeStr = req.getParameter("timecode");

            String CapStr = req.getParameter("cap");
            CapStr = CapStr.trim();
            String isWStr = req.getParameter("isW");
            String isTStr = req.getParameter("isT");
            String isSStr = req.getParameter("isS");
            String DangerStr = req.getParameter("danger");
            if (isWStr == null) isWStr = "false";
            if (isSStr == null) isSStr = "false";
            if (isTStr == null) isTStr = "false";

            boolean isW = Boolean.parseBoolean(isWStr);
            boolean isT = Boolean.parseBoolean(isTStr);
            boolean isS = Boolean.parseBoolean(isSStr);
            if (!(isS || isT || isW)) throw new MalformedURLException();
            int Danger = Integer.parseInt(DangerStr);

            if (Danger != -1 && (Danger <= 0 || Danger > 4)) throw new MalformedURLException();

            int WeatherDetail = -1;
            if (isW) {
                String WeatherDetailStr = req.getParameter("weatherdetail");
                if (WeatherDetailStr == null)
                    throw new MalformedURLException();
                WeatherDetail = Integer.parseInt(WeatherDetailStr);
                if (!(WeatherDetail == -1 || (WeatherDetail > 0 && WeatherDetail < 11)))
                    throw new MalformedURLException();
            }

            ResultContainer Res = null;
            SearchTimeD SDInst = null;
            if (TimecodeStr.equals("instant")) {
                String InstantStr = req.getParameter("instant");
                if (InstantStr == null)
                    throw new MalformedURLException();

                Date Instant = new Date(Long.parseLong(InstantStr));

                SDInst = new SearchTimeInstantD(DbConn, Instant);
            } else if (TimecodeStr.equals("interval")) {
                String BeginStr = req.getParameter("begin");
                String EndStr = req.getParameter("end");
                if (BeginStr == null || EndStr == null)
                    throw new MalformedURLException();
                Date Begin = new Date(Long.parseLong(BeginStr));
                Date End = new Date(Long.parseLong(EndStr));

                SDInst = new SearchTimeIntervalD(DbConn, Begin, End);
            } else throw new MalformedURLException();


            if (CapStr.isEmpty()) {
                Res = SDInst.FindGlobal(isW, isT, isS, Danger, WeatherDetail);
                req.setAttribute("isglobal", true);
            } else {
                List<Integer> CapList = MultiCapParser.ParseCap(CapStr);
                CapD CCDAOInst = new CapD(DbConn);
                Res = new ResultContainer();
                for (Integer Cap : CapList) {
                    if (!CCDAOInst.isValid(Cap))
                        throw new MalformedURLException();
                }
                Res = SDInst.FindByCap(CapList, isW, isT, isS, Danger, WeatherDetail);
            }
            req.setAttribute("results", Res);
            RequestDispatcher RD = req.getRequestDispatcher("AdvancedSearchRes.jsp");
            RD.forward(req, res);
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } catch (SQLException e) {
            res.sendRedirect(ContextPath + "/error?code=3");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            res.sendRedirect(ContextPath + "/error?code=5");
            e.printStackTrace();
        } catch (CapParseException e) {
            res.sendRedirect(ContextPath + "/error?code=6");
        } finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        {
            HandleRequest(req, res);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        {
            HandleRequest(req, res);
        }
    }
}


/*
                                String DateStr = req.getParameter("date");
                String TimeStr = req.getParameter("time");
                int Year = 0;
                int Month = 0;
                int Day = 0;
                int Hour = 0;
                int Minute = 0;

                int SplitDateCtr = 0;
                int DateRes = 0;
                for (int i = 0; i < DateStr.length(); i++) {
                    char CurrChar = DateStr.charAt(i);
                    if (CurrChar == '-') {
                        switch (SplitDateCtr) {
                            case 0:
                                Year = DateRes;
                                break;
                            case 1:
                                Month = DateRes;
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                        SplitDateCtr++;
                        DateRes = 0;
                    } else {
                        int digit = (int) CurrChar - (int) '0';
                        if ((digit < 0) || (digit > 9)) throw new IllegalArgumentException();
                        DateRes *= 10;
                        DateRes += digit;
                    }
                }
                if (SplitDateCtr != 2) throw new IllegalArgumentException();
                Day = DateRes;
                int SplitTimeCtr = 0;
                int TimeRes = 0;
                for (int i = 0; i < TimeStr.length(); i++) {
                    char CurrChar = TimeStr.charAt(i);
                    if (CurrChar == ':') {
                        if (TimeRes == 0) throw new IllegalArgumentException();
                        switch (SplitTimeCtr) {
                            case 0:
                                Hour = TimeRes;
                                break;
                            case 1:
                                Minute = TimeRes;
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                        SplitTimeCtr++;
                        TimeRes = 0;
                    } else {
                        int digit = (int) CurrChar - (int) '0';
                        if ((digit < 0) || (digit > 9)) throw new IllegalArgumentException();
                        TimeRes *= 10;
                        TimeRes += digit;
                    }
                }
                if (SplitTimeCtr != 1) throw new IllegalArgumentException();
                Minute = TimeRes;
                Calendar cal = Calendar.getInstance();
                cal.set(Year, Month, Day, Hour, Minute);
                */