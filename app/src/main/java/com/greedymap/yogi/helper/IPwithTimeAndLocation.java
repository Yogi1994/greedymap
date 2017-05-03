package com.greedymap.yogi.helper;

import java.util.Date;

/**
 * Created by yogi on 3/5/17.
 */

public class IPwithTimeAndLocation {
    double longitude;
    double latitude;
    Date accesTime;
    String IP;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIP() {
        return IP;
    }

    public Date getAccesTime() {
        return accesTime;
    }

    public void setAccesTime(Date accesTime) {
        this.accesTime = accesTime;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLocation(double latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
