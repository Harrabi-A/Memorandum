package com.example.memorandum.model;

import android.annotation.SuppressLint;

public class GeoPosition {
    private String name;
    private double latitude ;
    private double longitude;

    public GeoPosition(String n, double lat, double lon){
        name = n;
        latitude = lat;
        longitude = lon;
    }

    public String getName(){
        return name;
    }
    public void setName(String n){
        name = n;
    }

    public double getLatitude(){
        return latitude;
    }
    public void setLatitude(double x){
        latitude = x;
    }

    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double x){
        longitude = x;
    }

}
