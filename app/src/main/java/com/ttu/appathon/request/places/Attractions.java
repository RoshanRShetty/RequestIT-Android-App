package com.ttu.appathon.request.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ttu.appathon.request.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manohar on 27-Oct-15.
 */

/**
 * Launch the attractions based on the latitude and longitude
 */

public class Attractions extends AppCompatActivity {
    private ListView places_ListView;
    private List<Place> places = new ArrayList<Place>();
    private PlacesAdapter places_adapter;
    private Double Latitude;
    private Double Longitude;
    private String next_page_token;
    private boolean flag_onScroll;
    private int last_Visible_item = 0;
    private PlaceResults placeResults = new PlaceResults();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attractions_layout);
        Intent intent = getIntent();
        // Get Latitude and Longitude from parent activity
        Latitude = intent.getDoubleExtra("Latitude", 0.00);
        Longitude = intent.getDoubleExtra("Longitude", 0.00);
        places_ListView = (ListView) findViewById(R.id.placeslist);
        places_adapter = new PlacesAdapter(Attractions.this, new ArrayList<Place>());
        //
        // Register ScrollListener to List view
        places_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    // If list item is scrolled to last item, request more information using next page token
                    if (flag_onScroll == false && placeResults.next_page_token != null) {
                        last_Visible_item = firstVisibleItem;
                        flag_onScroll = true;
                        try {
                            // Get more places
                            getAttractions ga = new getAttractions(Attractions.this);
                            ga.execute("Test");
                        } catch (Exception E) {
                        }
                    }
                }
            }
        });
        places_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Place selectedPlace = places.get(position);
                if (position > -1) {
                    Intent attractions_intent = new Intent(Attractions.this, AttractionPlaceDetails.class);
                    attractions_intent.putExtra("PlaceName", selectedPlace.getName());
                    attractions_intent.putExtra("City", "Lubbock");
                    attractions_intent.putExtra("PlaceId", selectedPlace.getPlaceId());
                    startActivity(attractions_intent);
                }


            }
        });

        try {
            getAttractions ga = new getAttractions(Attractions.this);
            ga.execute("Test");
        } catch (Exception E) {
        }
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    // Class to hold the token for next page results
    public class PlaceResults {
        protected String next_page_token;
    }

    //
    // AsyncTask class to get the places
    private class getAttractions extends AsyncTask<String, Void, List<Place>> {
        private ProgressDialog dialog;

        public getAttractions(Attractions activity) {
            dialog = new ProgressDialog(activity);
        }

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected List<Place> doInBackground(String... urls) {

            try {
                FindPlaces client = new FindPlaces("AIzaSyCsF0oWNw0R05Ky7Pfmw2DkrRtA9vOs3D8");
                if (flag_onScroll == true) {
                    if (placeResults.next_page_token != null) {
                        sleep(3000);
                        // Requesting next page
                        places.addAll(client.getNearbyPlaces(placeResults, 20));
                        flag_onScroll = false;
                    }

                } else {
                    // Initiating  Request
                    places.addAll(client.getNearbyPlaces(Latitude, Longitude, 15000, 20, placeResults));
                }
            } catch (Throwable t) {
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            places_adapter.setPlacesList(places);
            places_ListView.setAdapter(places_adapter);
            places_adapter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            places_ListView.setSelection(last_Visible_item);
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


}