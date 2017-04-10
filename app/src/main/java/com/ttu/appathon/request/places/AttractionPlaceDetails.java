package com.ttu.appathon.request.places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ttu.appathon.request.R;
import com.ttu.appathon.request.Transport.TransportDetails;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Manohar on 17-Nov-15.
 */
public class AttractionPlaceDetails extends AppCompatActivity {
    protected TextView place_name;
    protected TextView place_adr;
    public String placeId;
public Place PlaceDetails;

    TextView shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attractions_place_details);
        final String PlaceName;
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            // Get Latitude and Longitude from parent activity
            PlaceName = intent.getStringExtra("PlaceName");
            String city = intent.getStringExtra("City");
            String placeId = intent.getStringExtra("PlaceId");
            place_name = (TextView) findViewById(R.id.place_name);
            place_adr = (TextView) findViewById(R.id.place_addr);
            place_name.setText(PlaceName);
            place_adr.setText(city);
            new getPlaceDetails().execute(placeId);

// Functionality for button click
            shareButton = (TextView) findViewById(R.id.fullAddress);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String message =  PlaceName + "\n" +
                            "\nAddress: \n" + PlaceDetails.getAddress().toString();
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);

                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, message));

                }
            });




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

    private class getPlaceDetails extends AsyncTask<String, Void, String> {
        protected String place;
        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... placeId) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            String data;
            String uri;
            Bitmap bm = null;
            Double Latitude, Longitude;
            String line;
            try {
////
                place = new String(placeId[0]);
                uri = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place + "&key=AIzaSyCsF0oWNw0R05Ky7Pfmw2DkrRtA9vOs3D8";

                connection = (HttpURLConnection) (new URL(uri)).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }
            } catch (Throwable t) {
                //Toast.makeText(context, "Unable to get cities. Please check internet connection",
                //      Toast.LENGTH_LONG).show();

            }
            data = buffer.toString();
            return data;
        }

        @Override
        protected void onPreExecute() {

                dialog = new ProgressDialog(AttractionPlaceDetails.this);
            dialog.setCanceledOnTouchOutside(false);


            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.length() > 0) {
                try {
                    parsePlaceDetails(result);
                    RelativeLayout placeDetailsLayout =(RelativeLayout)findViewById(R.id.place_Details_Layout);
                    placeDetailsLayout.setVisibility(View.VISIBLE);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }


                } catch (JSONException J) {
                }
            }
        }

        protected void parsePlaceDetails(String result) throws JSONException {
            FindPlaces client = new FindPlaces("AIzaSyDtS5nYWh9bMAdmdofOfKOkfkPe3SG_qxk");
            Place selectedPlace = new Place();
            selectedPlace.setClient(client);
            selectedPlace.setPlaceId(place);
           PlaceDetails=selectedPlace.parseDetails(client, result);
            TextView fullAddr =(TextView) findViewById(R.id.fullAddress);
            fullAddr.setText(PlaceDetails.getAddress());
            List<AddressComponent> a=PlaceDetails.getAddressComponents();
            place_adr.setText(a.get(1).getLongName());
            TextView rating=(TextView) findViewById(R.id.rating);
            Double reviewRate =PlaceDetails.getRating();
            rating.setText(reviewRate.toString());
            TextView reviewCountView=(TextView) findViewById(R.id.reviewCount);
            Integer reviewCount =new Integer(PlaceDetails.getTotalReviews());
            reviewCountView.setText(reviewCount.toString()+" Reviews");
            //
            // Get working hours and display
Hours hours=PlaceDetails.getHours();
          List<Hours.Period> periods_List=  hours.getPeriods();
            int day=0;
            int size=periods_List.size();
            if (size==0){
                TextView HoursTitle=(TextView) findViewById(R.id.HoursTitle);
                HoursTitle.setVisibility(View.GONE);
            }
            while (size!=0 && day<size){
                Hours.Period period=periods_List.get(day);
                switch (period.getOpeningDay()) {
                    case MONDAY:
                        TextView mon = (TextView) findViewById(R.id.Hour_Mon);
                        TextView monday = (TextView) findViewById(R.id.Mon);
                        monday.setVisibility(View.VISIBLE);
                        mon.setVisibility(View.VISIBLE);
                        mon.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        break;
                    case TUESDAY:
                        TextView Tue = (TextView) findViewById(R.id.Hour_Tue);
                        Tue.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView tuesday = (TextView) findViewById(R.id.Tue);
                        tuesday.setVisibility(View.VISIBLE);
                        Tue.setVisibility(View.VISIBLE);
                        break;
                    case WEDNESDAY:
                        TextView Wed = (TextView) findViewById(R.id.Hour_Wed);
                        Wed.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView wednesday = (TextView) findViewById(R.id.Wed);
                        wednesday.setVisibility(View.VISIBLE);
                        Wed.setVisibility(View.VISIBLE);
                        break;
                    case THURSDAY:
                        TextView Thur = (TextView) findViewById(R.id.Hour_Thu);
                        Thur.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView Thursday = (TextView) findViewById(R.id.Thu);
                        Thursday.setVisibility(View.VISIBLE);
                        Thur.setVisibility(View.VISIBLE);
                        break;
                    case FRIDAY:
                        TextView Fri = (TextView) findViewById(R.id.Hour_Fri);
                        Fri.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView Friday = (TextView) findViewById(R.id.Fri);
                        Friday.setVisibility(View.VISIBLE);
                        Fri.setVisibility(View.VISIBLE);
                        break;
                    case SATURDAY:
                        TextView Sat = (TextView) findViewById(R.id.Hour_Sat);
                        Sat.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView Saturday = (TextView) findViewById(R.id.Sat);
                        Saturday.setVisibility(View.VISIBLE);
                        Sat.setVisibility(View.VISIBLE);
                        break;
                    case SUNDAY:
                        TextView Sun = (TextView) findViewById(R.id.Hour_Sun);
                        Sun.setText(period.getOpeningTime() +" to "+ period.getClosingTime());
                        TextView Sunday = (TextView) findViewById(R.id.Sun);
                        Sunday.setVisibility(View.VISIBLE);
                        Sun.setVisibility(View.VISIBLE);
                        break;
                }
                ++day;
                }
            List<Review> Reviews=  PlaceDetails.getReviews();
            int ReviewsSize=Reviews.size();
            int reviewno=0;
            while(ReviewsSize!=0 && reviewno < ReviewsSize && reviewno<4) {
              //  TextView ReviewHeader = (TextView) findViewById(R.id.Reviewer_name);
                //ReviewHeader.setText(Reviews.get(0).getAuthor());
                //TextView Review = (TextView) findViewById(R.id.review);
                //Review.setText(Reviews.get(0).getText());
                //
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.reviewLayout);

                // Add ReviewAuthor
                TextView ReviewAuthor = new TextView(AttractionPlaceDetails.this);
                ReviewAuthor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                        ReviewAuthor.setText(Reviews.get(reviewno).getAuthor());
                String text = "<font color=#4386F4>"+Reviews.get(reviewno).getAuthor()+"  -"+"</font> <font color=#3F7E00>"+Reviews.get(reviewno).getRating()+"</font>";
                ReviewAuthor.setText(Html.fromHtml(text));
                ReviewAuthor.setGravity(Gravity.LEFT);
                ReviewAuthor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                ReviewAuthor.setPadding(8, 8, 8, 8);// in pixels (left, top, right, bottom)
                linearLayout.addView(ReviewAuthor);

                // Add reviewText
                TextView reviewText = new TextView(AttractionPlaceDetails.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                reviewText.setLayoutParams(layoutParams);
                reviewText.setText(Reviews.get(reviewno).getText());
                reviewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                reviewText.setTextColor(Color.BLACK); // hex color 0xAARRGGBB
                linearLayout.addView(reviewText);
                //
                ++reviewno;
            }
            Log.e("Check","parsing");
        }
    }
    public void onCall(View v) {
        String number = "tel:" + PlaceDetails.getInternationalPhoneNumber();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }

    public void loadWebsite(View v) {
        String UrlString = PlaceDetails.getWebsite();
        if (UrlString==null) {
            Toast.makeText(getApplicationContext(), "Sorry, no website found for this place",
                    Toast.LENGTH_LONG).show();

        }
        else {
            Uri uriUrl = Uri.parse(UrlString);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }




    }
    public void loadMaps(View v) {
        Intent travel_intent = new Intent(AttractionPlaceDetails.this, TransportDetails.class);
        travel_intent.putExtra("PlaceId", PlaceDetails.getPlaceId());
        travel_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(travel_intent);

    }
}
