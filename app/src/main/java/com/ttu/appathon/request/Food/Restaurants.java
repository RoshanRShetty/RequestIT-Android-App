package com.ttu.appathon.request.Food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ttu.appathon.request.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 29-10-2015.
 */
public class Restaurants extends AppCompatActivity {
    protected ListView Restaurants_list_view;
    protected List<RestaurantDetailsJsonParser> places;
    protected RestaurantDetailsJsonParserAdapter Restaurants_adapter;
    Double Latitude;
    Double Longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list_layout);
           Intent intent = getIntent();
        // Get Latitude and Longitude from parent activity
         Latitude = intent.getDoubleExtra("Latitude", 0.00);
        Longitude = intent.getDoubleExtra("Longitude", 0.00);

        Restaurants_list_view = (ListView) findViewById(R.id.restlist);
        Restaurants_adapter = new RestaurantDetailsJsonParserAdapter(Restaurants.this, new ArrayList<RestaurantDetailsJsonParser>());
        try {
            getRestaurants ga = new getRestaurants(Restaurants.this);
            ga.execute("Test");
        } catch (Exception E) {

        }
    }
    private class getRestaurants extends AsyncTask<String, Void, List<RestaurantDetailsJsonParser>> {
        private ProgressDialog dialog;
        public getRestaurants(Restaurants activity) {
            dialog = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }
        @Override
        protected List<RestaurantDetailsJsonParser> doInBackground(String... urls) {

            try {
//
                FoodZomato client = new FoodZomato("189ac73a52a3ee1a46826c9f9ebe2cb8");
                places = client.getNearbyPlaces(Latitude, Longitude, 50000, 20);

            } catch (Exception e) {
               e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<RestaurantDetailsJsonParser> places) {

            Restaurants_adapter.setPlacesList(places);
            Restaurants_list_view.setAdapter(Restaurants_adapter);
            Restaurants_adapter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }
}
