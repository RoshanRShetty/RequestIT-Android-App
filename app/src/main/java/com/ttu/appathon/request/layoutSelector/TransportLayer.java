package com.ttu.appathon.request.layoutSelector;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.ttu.appathon.request.R;
import com.ttu.appathon.request.Transport.PlaceAutocompleteAdapter;
import com.ttu.appathon.request.Transport.TransportDetails;

import java.util.ArrayList;
import java.util.List;

public class TransportLayer extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mPlaceFilter;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private TextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;
    private LatLngBounds bounds;
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

   //         Log.i(TAG, "Autocomplete item selected: " + primaryText);
            Intent travel_intent = new Intent(TransportLayer.this, TransportDetails.class);
            travel_intent.putExtra("PlaceId", placeId);
            startActivity(travel_intent);
            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
        //    Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        setContentView(R.layout.transport_layout);
        Intent intent = getIntent();
        // Get Latitude and Longitude from parent activity
       double Latitude = intent.getDoubleExtra("Latitude", 0.00);
        double  Longitude = intent.getDoubleExtra("Longitude", 0.00);

        LatLng center = new LatLng(Latitude, Longitude);
        LatLng southwest = SphericalUtil.computeOffset(center, 20000 * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, 20000 * Math.sqrt(2.0), 45);
        bounds = new LatLngBounds(southwest, northeast);
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);
        mAutocompleteView.setText("");
        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        //    mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        //   mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        List<Integer> filters = new ArrayList<Integer>();
        filters.add(Place.TYPE_GEOCODE);
        mPlaceFilter = AutocompleteFilter.create(filters);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, bounds,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        //   Button clearButton = (Button) findViewById(R.id.button_clear);
        // clearButton.setOnClickListener(new View.OnClickListener() {
        // @Override
        //   public void onClick(View v) {
        //     mAutocompleteView.setText("");
        //}
        //});
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    //    Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
      //          + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}
