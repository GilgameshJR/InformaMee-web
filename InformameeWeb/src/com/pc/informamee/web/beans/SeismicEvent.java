package com.pc.informamee.web.beans;

public class SeismicEvent extends Event {
    private double richterMagnitude;
    private double mercalliMagnitude;
    private Integer epicentreCAP;
    private String epicentreCAPJson;

    public void setEpicentreCAP(Integer epicentreCAP) {
        this.epicentreCAP = epicentreCAP;
    }

    public void setMercalliMagnitude(double mercalliMagnitude) {
        this.mercalliMagnitude = mercalliMagnitude;
    }

    public void setRichterMagnitude(double richterMagnitude) {
        this.richterMagnitude = richterMagnitude;
    }

    public double getRichterMagnitude() {
        return richterMagnitude;
    }

    public double getMercalliMagnitude() {
        return mercalliMagnitude;
    }

    public Integer getEpicentreCAP() {
        return epicentreCAP;
    }

    public String getEpicentreCAPJson() {
        return epicentreCAPJson;
    }

    public void setEpicentreCAPJson(String epicentreCAPJson) {
        this.epicentreCAPJson = epicentreCAPJson;
    }
}
