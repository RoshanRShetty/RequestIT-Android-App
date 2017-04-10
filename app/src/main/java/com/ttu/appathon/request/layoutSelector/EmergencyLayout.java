package com.ttu.appathon.request.layoutSelector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttu.appathon.request.CitySelection.CitySelectionLayout;
import com.ttu.appathon.request.CitySelection.SelectedCitySerializable;
import com.ttu.appathon.request.R;
import com.ttu.appathon.request.Transport.TransportDetails;
import com.ttu.appathon.request.database.EmergencyDatasource;
import com.ttu.appathon.request.models.Emergency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class EmergencyLayout extends AppCompatActivity implements LocationListener {

    // Varaibles to be used.
    private static final String LOGTAG = "EmergencyLayout";
    public String placeId;

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
    EmergencyDatasource datasource;
    double Latitude;
    double Longitude;
    public String keyword="Nearest+Police+Station";

    private String country;
    private String policeNo;
    private String ambulance;
    private String fireControl;

    private String myAddress;



    ImageView shareButton;
    ImageView shareSos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        // creating a new EmergencyDatasource object.
        datasource = new EmergencyDatasource(this);


        Intent intent = getIntent();
        setLocation = true;
        // getting country and cit data from MainActivity.java
        currentCity.city_name = intent.getStringExtra("CityName");
        currentCity.country = intent.getStringExtra("Country");
         Latitude = intent.getDoubleExtra("Latitude", 0.00);
          Longitude = intent.getDoubleExtra("Longitude", 0.00);


        // calling the getData method to display emergenc details according to the city.
        //datasource.getData(currentCity.country);
        Log.i(LOGTAG, "onCreate");
        List<Emergency> fetchedDetails = datasource.getData(currentCity.country);

        TextView editCountry = (TextView) findViewById(R.id.country_value);

        TextView editPolice = (TextView) findViewById(R.id.police_no_value);
        TextView editAmbulance = (TextView) findViewById(R.id.ambulance_no_value);
        TextView editFire = (TextView) findViewById(R.id.firecontrol_no_value);


        if (fetchedDetails.isEmpty()) {

            editCountry.setText(" No Country Found");
            editPolice.setText(" NA");
            editAmbulance.setText(" NA");
            editFire.setText(" NA");
        } else {

            editCountry.setText(" " + fetchedDetails.get(0).getCountryName().toString());
            editPolice.setText(" " + fetchedDetails.get(0).getPoliceNo().toString());
            editAmbulance.setText(" " + fetchedDetails.get(0).getAmbulanceNo().toString());
            editFire.setText(" " + fetchedDetails.get(0).getFireControlNo().toString());

            country = fetchedDetails.get(0).getCountryName().toString();
            policeNo = fetchedDetails.get(0).getPoliceNo().toString();
            ambulance = fetchedDetails.get(0).getAmbulanceNo().toString();
            fireControl = fetchedDetails.get(0).getFireControlNo().toString();


        }


        // Functionality for button click
        shareButton = (ImageView) findViewById(R.id.imageView);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message =  "Emergency Details: \n" +
                        "\nCountry: " + country +
                        "\nPolice: " + policeNo +
                        "\nAmbulance: " + ambulance +
                        "\nFire Control: " + fireControl;

                sendIntent.putExtra(Intent.EXTRA_TEXT, message);

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, message));

            }
        });


        shareSos = (ImageView) findViewById(R.id.imageView2);
        shareSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyLocationAddress();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message =  "HELP ME, I AM IN TROUBLE \n" +
                        "\n MY LOCATION IS: \n " +
                        myAddress ;

                sendIntent.putExtra(Intent.EXTRA_TEXT, message);

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, message));

            }
        });



    }

    public void getMyLocationAddress() {

        Geocoder geocoder= new Geocoder(this, Locale.getDefault());

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(Latitude,Longitude, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                myAddress = strAddress.toString();

            }

            else
                myAddress = "No location found..!";

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putString("Saving", "State");
        super.onSaveInstanceState(state);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String test = savedInstanceState.getString("Saving");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // location identifier
    @Override
    public void onLocationChanged(Location location) {
        // Turn off the location update requests
        if (setLocation == false) {
            Log.d("Location", "Service stopped");
            locationManager.removeUpdates(this);
        } else {
            double current_Latitude = location.getLatitude();
            double current_Longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(current_Latitude, current_Longitude, 1);
                // Get the city name from the address list and save to current city class
                String cityName = addresses.get(0).getLocality();
                currentCity.city_name = cityName;
                currentCity.country = addresses.get(0).getCountryName();
                currentCity.latitude = current_Latitude;
                currentCity.longitude = current_Longitude;
                //
                MenuItem location_item = actionbar_menu.findItem(R.id.city_textview);
                location_item.setTitle(cityName);
                datasource = new EmergencyDatasource(this);
                Log.i(LOGTAG, "onLocationChange");
                List<Emergency> fetchedDetails = datasource.getData(currentCity.country);

                TextView editCountry = (TextView) findViewById(R.id.country_value);
                TextView editPolice = (TextView) findViewById(R.id.police_no_value);
                TextView editAmbulance = (TextView) findViewById(R.id.ambulance_no_value);
                TextView editFire = (TextView) findViewById(R.id.firecontrol_no_value);


                if (fetchedDetails.isEmpty()) {

                    editCountry.setText(" No Country Found");
                    editPolice.setText(" NA");
                    editAmbulance.setText(" NA");
                    editFire.setText(" NA");
                } else {

                    editCountry.setText(" " + fetchedDetails.get(0).getCountryName().toString());
                    editPolice.setText(" " + fetchedDetails.get(0).getPoliceNo().toString());
                    editAmbulance.setText(" " + fetchedDetails.get(0).getAmbulanceNo().toString());
                    editFire.setText(" " + fetchedDetails.get(0).getFireControlNo().toString());

                    country = fetchedDetails.get(0).getCountryName().toString();
                    policeNo = fetchedDetails.get(0).getPoliceNo().toString();
                    ambulance = fetchedDetails.get(0).getAmbulanceNo().toString();
                    fireControl = fetchedDetails.get(0).getFireControlNo().toString();


                }


                setLocation = false;
                Log.d("Location", "Found city");
            } catch (IOException e) {
                Log.e("Location", "Unable connect to Geocoder", e);
            }
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "status");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        actionbar_menu = menu;
        MenuItem location_item = actionbar_menu.findItem(R.id.city_textview);
        location_item.setTitle(currentCity.city_name);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.cityicon) {
            //Launch City selection activity
            Intent city_Selection_Intent = new Intent(EmergencyLayout.this, CitySelectionLayout.class);
            startActivityForResult(city_Selection_Intent, CITY_REQUEST_CODE);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                SelectedCitySerializable selected_city = (SelectedCitySerializable) data.getSerializableExtra("Selected_City");
                // Save selected city details to currentCity class
                currentCity.city_name = selected_city.getCity();
                currentCity.country = selected_city.getCountry();
                currentCity.latitude = selected_city.getLatitude();
                currentCity.longitude = selected_city.getLongitude();
                MenuItem location_item = actionbar_menu.findItem(R.id.city_textview);
                location_item.setTitle(currentCity.city_name);
                datasource = new EmergencyDatasource(this);
                Log.i(LOGTAG, "onActivityResult");
                List<Emergency> fetchedDetails = datasource.getData(currentCity.country);
                TextView editCountry = (TextView) findViewById(R.id.country_value);
                TextView editPolice = (TextView) findViewById(R.id.police_no_value);
                TextView editAmbulance = (TextView) findViewById(R.id.ambulance_no_value);
                TextView editFire = (TextView) findViewById(R.id.firecontrol_no_value);


                if (fetchedDetails.isEmpty()) {

                    editCountry.setText(" No Country Found");
                    editPolice.setText(" NA");
                    editAmbulance.setText(" NA");
                    editFire.setText(" NA");
                } else {

                    editCountry.setText(" " + fetchedDetails.get(0).getCountryName().toString());
                    editPolice.setText(" " + fetchedDetails.get(0).getPoliceNo().toString());
                    editAmbulance.setText(" " + fetchedDetails.get(0).getAmbulanceNo().toString());
                    editFire.setText(" " + fetchedDetails.get(0).getFireControlNo().toString());


                    country = fetchedDetails.get(0).getCountryName().toString();
                    policeNo = fetchedDetails.get(0).getPoliceNo().toString();
                    ambulance = fetchedDetails.get(0).getAmbulanceNo().toString();
                    fireControl = fetchedDetails.get(0).getFireControlNo().toString();

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }
    public void getNearestPlace(View v){
        placeId="";


        new getPlaceDetails().execute();


    }
    //// TODO: 20-Feb-16
    private class getPlaceDetails extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(Void... placeId) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            String data;
            String uri;
            String line;
            try {

                uri="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyCsF0oWNw0R05Ky7Pfmw2DkrRtA9vOs3D8&location="+Latitude+","+Longitude+"&radius=15000.000000&keyword="+keyword;
                connection = (HttpURLConnection) (new URL(uri)).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
            } catch (Throwable t) {

            }
            data=buffer.toString();
            return data;    }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(EmergencyLayout.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String str) {
            JSONObject json=null;
            JSONArray results=null;
            int i=1;
            try {
                json = new JSONObject(str);

                results = json.getJSONArray("results");
                if (results.length() >= 1) {

                    JSONObject result = results.getJSONObject(i);
                     placeId = result.getString("place_id");
                    if (placeId !=""){
                        Intent travel_intent = new Intent(EmergencyLayout.this, TransportDetails.class);
                        travel_intent.putExtra("PlaceId", placeId);
                        travel_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(travel_intent);
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
            catch (JSONException E){

            }
    }
    }
    public void onPoliceCall(View v) {
        TextView iv=(TextView) findViewById(R.id.police_no_value);
        String number = "tel:" +iv.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }
    public void onAmbCall(View v) {
        TextView iv=(TextView) findViewById(R.id.ambulance_no_value);
        String number = "tel:" +iv.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }
    public void onFireCall(View v) {
        TextView iv=(TextView) findViewById(R.id.firecontrol_no_value);
        String number = "tel:" +iv.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }
}// end of class
