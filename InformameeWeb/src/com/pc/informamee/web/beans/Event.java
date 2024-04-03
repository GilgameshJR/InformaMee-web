package com.pc.informamee.web.beans;

import java.util.Date;
import java.util.List;

public abstract class Event {
    private Integer danger;
    private String description;
    //https://stackoverflow.com/questions/6777810/a-datetime-equivalent-in-java-sql-is-there-a-java-sql-datetime
    private Date beginTime;
    private Date endTime;
    private int id;
    private List<Integer> involvedCap;
    private String involvedCapJson;

    public List<Integer> getInvolvedCap() {
        return involvedCap;
    }

    public void setInvolvedCap(List<Integer> involvedCap) {
        this.involvedCap = involvedCap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getDanger() {
        return danger;
    }

    public String getDescription() {
        return description;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setDanger(Integer danger) {
        this.danger = danger;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getInvolvedCapJson() {
        return involvedCapJson;
    }

    public void setInvolvedCapJson(String involvedCapJson) {
        this.involvedCapJson = involvedCapJson;
    }
}
