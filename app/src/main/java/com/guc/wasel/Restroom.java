package com.guc.wasel;

/**
 * Created by Zaher Abdelrahman on 4/27/2017.
 */

/**
 * This Class defines the Restroom Model
 */
public class Restroom {

    private boolean isAccessible;
    private double longitude;
    private double latitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}
