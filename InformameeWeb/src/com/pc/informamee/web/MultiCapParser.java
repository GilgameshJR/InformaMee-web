package com.pc.informamee.web;

import java.util.ArrayList;
import java.util.List;

public class MultiCapParser {


    public static List<Integer> ParseCap(String CAPs) throws CapParseException {
        if (CAPs==null) throw new CapParseException();
        CAPs=CAPs.trim();
        if (CAPs.length()==0) throw new CapParseException();
        List<Integer> CAPList=new ArrayList<Integer>();
        boolean resultempty=true;
        int result = 0;
        for (int charit=0; charit<CAPs.length(); charit++) {
            char c=CAPs.charAt(charit);
            if (c==' ') {
                    CAPList.add(result);
                    result=0;
                    resultempty=true;
            }
            else {
                int digit = (int) c - (int) '0';
                if ((digit < 0) || (digit > 9)) throw new CapParseException();
                result *= 10;
                result += digit;
                if (resultempty) resultempty=false;
            }
        } if (!resultempty) CAPList.add(result);
        return CAPList;
    }


/*
    public final static String CAPNOTNUMBERSSTRING="CAP non valido: ammessi solo numeri e simboli per separare i CAP";
    public final static String CAPLENGHTEXCEPTIONSTRING="CAP non valido: i CAP italiani sono di 5 cifre";
    public static List<Integer> ParseCap(String CAPs) throws CapParseException, CapLenghtException {
        CAPs=CAPs.trim();
        List<Integer> CAPList=new ArrayList<Integer>();
        int result = 0;
        int count=0;
        for (int charit=0; charit<CAPs.length(); charit++) {
            char c=CAPs.charAt(charit);
            if (c==' '||c=='/'||c=='.'||c==','||c=='-') {
                if (count==5) {
                    CAPList.add(Integer.valueOf(result));
                    result=0;
                    count=0;
                }
                else throw new CapLenghtException();
            }
            else {
                int digit = (int) c - (int) '0';
                if ((digit < 0) || (digit > 9)) throw new CapParseException();
                result *= 10;
                result += digit;
                count++;
            }
        }
        if (count!=5)
            throw new CapLenghtException();
        CAPList.add(Integer.valueOf(result));
        return CAPList;
    }

 */
}
