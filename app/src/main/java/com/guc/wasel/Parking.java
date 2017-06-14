package com.guc.wasel;

/**
 * Created by New on 5/7/2017.
 */

/**
 * This Class defines the Parking model
 */
public class Parking {

    private boolean isAccessible;
    private double longitude;
    private double latitude;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
