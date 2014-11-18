package com.google.riosport.elements;

import com.google.riosport.webservice.WebServiceException;

/**
 * Created by Rémi Prévost on 11/11/2014.
 */
public class Event {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;

    private String sport = "";
    private int owner = -1;
    private String visibility = "";
    private String dateTime = null;
    private int duration = -1;
    private String location = "";
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String description = "";

    public Event() {}

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(int year, int month, int day, int hour, int minute, int second) {
        this.dateTime = getTimeDate(year, month, day, hour, minute, second);
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) throws WebServiceException {
        if (visibility == PUBLIC)
            this.visibility = "public";
        else if (visibility == PRIVATE)
            this.visibility = "private";
        else
            throw new WebServiceException("Wrong visibility code");
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getTimeDate() {
        return "0000-00-00 00:00:00";
    }

    private String getTimeDate(int year, int month, int day, int hour, int minute, int second) {
        String month_string;
        if (month < 10)
            month_string = "0"+month;
        else
            month_string = ""+month;

        String day_string;
        if (day < 10)
            day_string = "0"+day;
        else
            day_string = ""+day;

        String hour_string;
        if (hour < 10)
            hour_string = "0"+hour;
        else
            hour_string = ""+hour;

        String minute_string;
        if (minute < 10)
            minute_string = "0"+minute;
        else
            minute_string = ""+minute;

        String second_string;
        if (second < 10)
            second_string = "0"+second;
        else
            second_string = ""+second;

        return year+"-"+month_string+"-"+day_string+" "+hour_string+":"+minute_string+":"+second_string;
    }
}
