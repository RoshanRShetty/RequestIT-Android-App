package com.ttu.appathon.request.layoutSelector;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ttu.appathon.request.R;
import com.ttu.appathon.request.database.VolunteerDataSource;

/**
 * Created by Pareshan on 11/16/2015.
 */
public class VolunteerLayout extends AppCompatActivity implements LocationListener {


    protected LocationManager locationManager;
    protected Context context;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    private boolean setLocation;
    GridView gridView;
    Menu actionbar_menu;
    CurrentCity currentCity = new CurrentCity();
    public static final int CITY_REQUEST_CODE = 1001;

    VolunteerDataSource vdatasource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_layout);
        // creating a new EmergencyDatasource object.
        vdatasource = new VolunteerDataSource(this);

        Intent intent = getIntent();
        setLocation = true;
        // getting country and cit data from MainActivity.java
        currentCity.city_name = intent.getStringExtra("CityName");
        currentCity.country = intent.getStringExtra("Country");
        vdatasource.open();
        Cursor cursor = vdatasource.getData(currentCity.city_name,currentCity.country);

        if ( cursor.getCount() > 0){
            // Setup mapping from cursor to view fields
            String[] fromFields = new String[]{
                    "volunteer_name",
                    "volunteer_email",
                    "volunteer_teleno"
            };

            int[] toViewIDs = new int[]{
                    R.id.volunteer_name,
                    R.id.emailid,
                    R.id.contact_no
            };

            //Setting adapter to map DB columns to UI text boxes
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                    this,                           // context
                    R.layout.volunteer_row,         // layout
                    cursor,                         // cursor
                    fromFields,
                    toViewIDs
            );

            ListView myList = (ListView) findViewById(R.id.volunteerlist);
            myList.setAdapter(simpleCursorAdapter);
        }
        else{

            Toast.makeText(getApplicationContext(), "Apologies! No Volunteer for this City",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        vdatasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vdatasource.close();
    }


}// end of class
