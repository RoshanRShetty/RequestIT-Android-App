package com.ttu.appathon.request.Hotels;

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

public class Accomadations extends AppCompatActivity {
    private ListView hotels_ListView;
    private List<HotelDetails> hotels = new ArrayList<HotelDetails>();
    private HotelDetailsAdapter hotels_adapter;
    private Double Latitude;
    private Double Longitude;
    private String next_page_token;
    private boolean flag_onScroll;
    private int last_Visible_item = 0;
    private HotelResults hotelResults = new HotelResults();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_list_layout);
        Intent intent = getIntent();
        // Get Latitude and Longitude from parent activity
        Latitude = intent.getDoubleExtra("Latitude", 0.00);
        Longitude = intent.getDoubleExtra("Longitude", 0.00);
        hotels_ListView = (ListView) findViewById(R.id.hotellist);
        hotels_adapter = new HotelDetailsAdapter(Accomadations.this, new ArrayList<HotelDetails>());
        //
        // Register ScrollListener to List view
        hotels_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    // If list item is scrolled to last item, request more information using next page token
                    if (flag_onScroll == false && hotelResults.next_page_token != null) {
                        last_Visible_item = firstVisibleItem;
                        flag_onScroll = true;
                        try {
                            // Get more places
                            getAttractions ga = new getAttractions(Accomadations.this);
                            ga.execute("Test");
                        } catch (Exception E) {
                        }
                    }
                }
            }
        });
        hotels_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                HotelDetails selectedPlace = hotels.get(position);
                if (position > -1) {
                    Intent travel_intent = new Intent(Accomadations.this, Accommodations_Details.class);
                    travel_intent.putExtra("PlaceId", selectedPlace.getPlaceId());
                    travel_intent.putExtra("PlaceName", selectedPlace.getName());
                    travel_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(travel_intent);
                }


            }
        });
        try {
            getAttractions ga = new getAttractions(Accomadations.this);
            ga.execute("Test");
        } catch (Exception E) {
        }
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    // Class to hold the token for next page results
    public class HotelResults {
        protected String next_page_token;
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

    //
    // AsyncTask class to get the places
    private class getAttractions extends AsyncTask<String, Void, List<HotelDetails>> {
        private ProgressDialog dialog;

        public getAttractions(Accomadations activity) {
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
        protected List<HotelDetails> doInBackground(String... urls) {

            try {
                HotelsAPI client = new HotelsAPI("AIzaSyCsF0oWNw0R05Ky7Pfmw2DkrRtA9vOs3D8");
                if (flag_onScroll == true) {
                    if (hotelResults.next_page_token != null) {
                        sleep(3000);
                        // Requesting next page
                        hotels.addAll(client.getNearbyPlaces(hotelResults, 20));
                        flag_onScroll = false;
                    }

                } else {
                    // Initiating  Request
                    hotels.addAll(client.getNearbyPlaces(Latitude, Longitude, 15000, 20, hotelResults));
                }
            } catch (Throwable t) {
            }
            return hotels;
        }

        @Override
        protected void onPostExecute(List<HotelDetails> hotels) {
            hotels_adapter.setPlacesList(hotels);
            hotels_ListView.setAdapter(hotels_adapter);
            hotels_adapter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            hotels_ListView.setSelection(last_Visible_item);
        }
    }
}