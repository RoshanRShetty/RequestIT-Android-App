package com.ttu.appathon.request.CitySelection;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Manohar on 10/17/2015.
 */

@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class SelectedCitySerializable implements Serializable {
    protected String city;
    protected String country;
    protected double lat;
    protected double lon;

    public SelectedCitySerializable(String city, String country, double lat, double lon){
        this.city = city;
        this.country = country;
        this.lat=lat;
        this.lon=lon;
    }

    public String getCity() {
        return city;
    }
    public String getCountry() {
        Locale loc = new Locale("",country);
        return loc.getDisplayCountry();
    }
    public double getLatitude() {
        return lat;
    }
    public double getLongitude() {
        return lon;
    }
}