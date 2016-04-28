/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

/**
 *
 * @author rkmalik
 */
public class Location {

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
    
    private double latitude;
    private double longitude;
    
    public Location () {
        latitude = 0.0;
        longitude = 0.0;
    }
    
}
