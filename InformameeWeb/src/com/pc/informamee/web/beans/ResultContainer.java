package com.pc.informamee.web.beans;

import java.util.ArrayList;
import java.util.List;

public class ResultContainer {
    public List<WeatherEvent> Weather;
    public List<SeismicEvent> Seismic;
    public List<TerroristEvent> Terrorist;

    public ResultContainer() {
        Weather=new ArrayList<WeatherEvent>();
        Seismic=new ArrayList<SeismicEvent>();
        Terrorist=new ArrayList<TerroristEvent>();
    }

    public List<SeismicEvent> getSeismic() {
        return Seismic;
    }

    public List<TerroristEvent> getTerrorist() {
        return Terrorist;
    }

    public List<WeatherEvent> getWeather() {
        return Weather;
    }

    public void setSeismic(List<SeismicEvent> seismicRes) {
        Seismic = seismicRes;
    }

    public void setTerrorist(List<TerroristEvent> terrorisRes) {
        Terrorist = terrorisRes;
    }

    public void setWeather(List<WeatherEvent> weatherRes) {
        Weather = weatherRes;
    }
}
