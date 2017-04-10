package com.ttu.appathon.request.models;

/**
 * Created by Pareshan on 10/31/2015.
 */
public class Emergency {

    private long countryID;
    private String countryName;
    private String policeNo;
    private String ambulanceNo;
    private String fireControlNo;
    private String notes;

    public long getCountryID() {
        return countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPoliceNo() {
        return policeNo;
    }

    public void setPoliceNo(String policeNo) {
        this.policeNo = policeNo;
    }

    public String getAmbulanceNo() {
        return ambulanceNo;
    }

    public void setAmbulanceNo(String ambulanceNo) {
        this.ambulanceNo = ambulanceNo;
    }

    public String getFireControlNo() {
        return fireControlNo;
    }

    public void setFireControlNo(String fireControlNo) {
        this.fireControlNo = fireControlNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}// end of class
