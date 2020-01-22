package com.example.android.quakereport;

public class Quake {
    private double magnitude;
    private String location,url;
    long timems;

    public String getLocation(){return location;}
    public long getTimems(){return timems;}
    public double getMagnitude(){return magnitude;}
    public String getUrl(){return url;}

    public Quake(double mag, String loc,long time, String u)
    {
        magnitude=mag;
        timems=time;
        location=loc;
        url=u;
    }
}
