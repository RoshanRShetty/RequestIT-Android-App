package com.ttu.appathon.request.CitySelection;

/**
 * Created by Manohar on 10/15/2015.
 */

/**
 * This class represents the a City as returned during the search process. The search can be done using name pattern
 * or using geographic location. The weather provider used will create the instance of city
 *
 * */
public class City {

    /*
    * Unique city identfier
    * */
    private String id;

    /**
     * City name
     */
    private String name;

    /*
    * Country name
    * */
    private String country;

    /*
    * region
    * */
    private String region;

    /**
     * longitude
     */
    private double lat;

    /**
     * latitude
     * */
    private double lon;

    public City() {
    }

    public City(String id, String name, String country,double lat,double lon) {
        super();
        this.id = id;
        this.name = name;
     //   this.region = region;
        this.country = country;
        this.lat=lat;
        this.lon=lon;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }
    public double getLatitude() {
        return lat;
    }
    public double getLongitude() {
        return lon;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String toString(){
        return this.name + ", " + this.country;
    }



}

