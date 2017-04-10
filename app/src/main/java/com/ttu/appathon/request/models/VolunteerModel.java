package com.ttu.appathon.request.models;

/**
 * Created by Pareshan on 11/16/2015.
 */
public class VolunteerModel {

    private long volunteerID;
    private String volunteerName;
    private String emailID;
    private String telephoneNo;
    private String city;
    private String dateModified;
    private String notes;

    public long getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(long volunteerID) {
        this.volunteerID = volunteerID;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dataModified) {
        this.dateModified = dataModified;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
} // end of class
