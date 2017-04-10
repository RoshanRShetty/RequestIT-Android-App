package com.ttu.appathon.request.Transport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.ttu.appathon.request.R;
import com.usebutton.sdk.Button;
import com.usebutton.sdk.ButtonContext;
import com.usebutton.sdk.ButtonDropin;
import com.usebutton.sdk.context.Location;
import com.usebutton.sdk.util.LocationProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Manohar on 14-Nov-15.
 */
public class TransportDetails extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    protected ImageView mapImage;
    protected TextView placeName;
    protected TextView placeAddr;
    protected Double Latitude;
    protected Double Longitude;
    // Prepare the Button for display
    protected ButtonDropin buttonDropin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_place_details);
        Button.getButton(this).start();
        Intent intent = getIntent();
        // Get Place_id from parent activity
        String Place_id = intent.getStringExtra("PlaceId");

        // Tell Button SDK about any inbound deeplinks we got from Intent
        //   Button.getButton(this).handleIntent(getIntent());
        //   Let's register our user ID so we can correlate our users with Button's data
        //  Button.getButton(this).setThirdPartyId("manoharkotapati@usebutton.com");
        buttonDropin = (ButtonDropin) this.findViewById(R.id.button_dropin_id);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mapImage = (ImageView) findViewById(R.id.mapArea);
        placeName=(TextView)findViewById(R.id.placeName);
        placeAddr = (TextView) findViewById(R.id.placeAddr);
        new getPlaceDetails().execute(Place_id);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    public void getMapImage(LatLng latlng) {


    }

    public void openMapNavigation(View v) {
        String transitMode="w";
        switch (v.getId()){
            case R.id.transitByWalk:
                transitMode="w";
                break;
            case R.id.transitByBus:
                transitMode="b";
                break;
            case R.id.transitByCar:
                transitMode="d";
                break;
            case R.id.transitByBike:
                transitMode="b";
                break;

        }

        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + Latitude + "," + Longitude + "&mode="+transitMode));
        startActivity(i);
    }

    private class GetMapImage extends AsyncTask<LatLng, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(LatLng... urls) {
            HttpURLConnection connection = null;
            String uri;
            Bitmap bitmapImage=null;
            Double Latitude, Longitude;
            try {
                LatLng latlng = urls[0];
                Latitude = latlng.latitude;
                Longitude = latlng.longitude;
                uri = "http://maps.googleapis.com/maps/api/staticmap?center=" + Latitude + "," + Longitude + "&zoom=14&size=500x500&maptype=roadmap&scale=2&markers=color:red|scale:2|" + Latitude + "," + Longitude + "&key=AIzaSyAKm623cYiQRMpv8c-qYMTmrEmAEG5wCjg";
                URL imageURL = new URL(uri);
                bitmapImage = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (Throwable t) {

            }
            return bitmapImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapImage) {
            if (bitmapImage !=null) {
                mapImage.setImageBitmap(bitmapImage);
            }
        }

    }

    private class getPlaceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... placeId) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            String data;
            String uri;
            String line;
            try {
                String place= new String(placeId[0]);
                uri="https://maps.googleapis.com/maps/api/place/details/json?placeid="+place+"&key=AIzaSyCsF0oWNw0R05Ky7Pfmw2DkrRtA9vOs3D8";
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
            dialog = new ProgressDialog(TransportDetails.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.length()>0) {
                try {
                    parsePlaceDetails(result);
                    LinearLayout placeDetailsLayout =(LinearLayout)findViewById(R.id.placeDetailsLayout);
                    placeDetailsLayout.setVisibility(View.VISIBLE);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                } catch (JSONException J) {
                }
            }
        }
        protected void parsePlaceDetails(String result) throws  JSONException{
            JSONObject placeJson = new JSONObject(result);
            JSONObject jObj = placeJson.getJSONObject("result");

            String Name = jObj.getString("name");
            String Addr = jObj.getString("formatted_address");
            JSONObject location = jObj.getJSONObject("geometry").getJSONObject("location");
            Latitude = location.getDouble("lat");
            Longitude= location.getDouble("lng");
            placeName.setText(Name);
            placeAddr.setText(Addr);
            // Set destination for Uber ride
            final Location Dest = new Location(Name, Latitude, Longitude);
            final ButtonContext context = ButtonContext.withSubjectLocation(Dest);

            final android.location.Location userLocation = new LocationProvider(TransportDetails.this).getBestLocation();
            if (userLocation != null) {
                context.setUserLocation(new Location(userLocation));
            }

            // Prepare the Button for display with our context
            buttonDropin.prepareForDisplay(context);
            //Get Map of the place
            LatLng latlng =new LatLng(Latitude,Longitude);
            new GetMapImage().execute(latlng);
        }
    }

}


