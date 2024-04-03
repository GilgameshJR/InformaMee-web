package com.pc.informamee.web.beans;

public class WeatherEvent extends Event {
    private float windSpeed;
    private int type;

    public void setWindSpeed(float WindSpeed) {
        this.windSpeed=WindSpeed;
    }
    public void setType(int Type){
        this.type=Type;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public int getType() {
        return type;
    }
}
