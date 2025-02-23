package com.pc.informamee.web.editor.controllers;

import com.pc.informamee.web.CapParseException;
import com.pc.informamee.web.DbConnectionHandler;
import com.pc.informamee.web.MultiCapParser;
import com.pc.informamee.web.dao.CapD;
import com.pc.informamee.web.editor.dao.EditD;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class EditC extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect("index");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Connection DbConn = null;
        String ContextPath = req.getContextPath();

        try {
            int CurrentUserId = (Integer) req.getAttribute("currentuserid");
            String TypeStr = (String) req.getParameter("type");
            String DescriptionStr = ((String) req.getParameter("description")).trim();
            String DangerStr = (String) req.getParameter("danger");
            String BeginStr = req.getParameter("begin");
            String EndStr = req.getParameter("end");
            String CapsStr = req.getParameter("cap");
            String EventIdString = req.getParameter("id");

            if (TypeStr==null || DescriptionStr==null || DangerStr==null || BeginStr==null || EndStr==null || DescriptionStr.isEmpty()) throw new MalformedURLException();

            int Danger=Integer.parseInt(DangerStr);
            if (Danger<=0 || Danger>4) throw new MalformedURLException();

            Long BeginLong=Long.parseLong(BeginStr);
            Long EndLong=Long.parseLong(EndStr);

            if( EndLong < BeginLong ) throw new MalformedURLException();

            Timestamp Begin=new Timestamp(BeginLong);
            Timestamp End=new Timestamp(EndLong);


            List<Integer> CapList= MultiCapParser.ParseCap(CapsStr);
            DbConn= DbConnectionHandler.getConnection();

            CapD CCDAOInst = new CapD(DbConn);
            for (Integer Cap : CapList) {
                if (!CCDAOInst.isValid(Cap))
                    throw new MalformedURLException();
            }

            int EventId=Integer.parseInt(EventIdString);
            EditD EDInst=new EditD(DbConn, EventId);
            switch (TypeStr) {
                case "weather": {
                    String WDetailStr=(String)req.getParameter("weatherdetail");
                    String WindSpeedStr=(String)req.getParameter("windspeed");
                    if (WDetailStr==null) throw new MalformedURLException();
                    int WDetail=Integer.parseInt(WDetailStr);
                    if (!( WDetail==-1 || (WDetail>0 && WDetail<11) )) throw new MalformedURLException();

                    Float WindSpeed;
                    if (WindSpeedStr==null || WindSpeedStr.isEmpty())
                        WindSpeed=Float.valueOf(0);
                    else
                        WindSpeed=Float.parseFloat(WDetailStr);

                    EDInst.updateWeather(CapList, Danger, DescriptionStr, Begin, End, Float.parseFloat(WindSpeedStr), Integer.parseInt(WDetailStr));
                    res.getWriter().println("EVENTO METEOROLOGICO MODIFICATO");
                    break;
                }
                case "seismic": {
                    String MercalliStr=(String)req.getParameter("mercalli");
                    String RichterStr=(String)req.getParameter("richter");
                    String EpicentreCapStr=(String)req.getParameter("epicentrecap");
                    if (MercalliStr==null || RichterStr==null || EpicentreCapStr==null || !CCDAOInst.isValid(Integer.parseInt(EpicentreCapStr))) throw new MalformedURLException();
                    EDInst.updateSeismic(CapList, Danger, DescriptionStr, Begin, End, Float.parseFloat(MercalliStr), Float.parseFloat(RichterStr), Integer.parseInt(EpicentreCapStr));
                    res.getWriter().println("EVENTO SIMSICO MODIFICATO");
                    break;
                }
                case "terrorist": {
                    EDInst.updateTerrorist(CapList, Danger, DescriptionStr, Begin, End);
                    break;
                }
                default: {
                    throw new MalformedURLException();
                }
            }
            res.sendRedirect("index");
        } catch (MalformedURLException e) {
            res.sendRedirect(ContextPath + "/error?code=1");
        } catch (CapParseException e) {
            res.sendRedirect(ContextPath + "/error?code=6");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=5");
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=3");
        } catch (NumberFormatException e) {
            res.sendRedirect(ContextPath + "/error?code=2");
        } catch (NullPointerException e) {
            e.printStackTrace();
            res.sendRedirect(ContextPath + "/error?code=4");
        }finally {
            DbConnectionHandler.disposeConnection(DbConn);
        }
    }
}