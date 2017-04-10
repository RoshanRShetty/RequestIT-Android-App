package com.ttu.appathon.request.CitySelection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ttu.appathon.request.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manohar on 10/5/2015.
 */
public class CitySelectionLayoutEmergency extends AppCompatActivity {

    private CityAdapter adp;
    public String data;
    ListView cityListView;
    List<City> cityList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_selection_layout);
        //
        //
        cityListView = (ListView) findViewById(R.id.cityList);
        adp = new CityAdapter(CitySelectionLayoutEmergency.this, new ArrayList<City>());
        cityListView.setAdapter(adp);

        final EditText edt = (EditText) findViewById(R.id.cityEdtText);
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Your query to fetch Data
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    search(s.toString());
                    //Your query to fetch Data
                }
            }
        });
        /*edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("autoplace", "Gotfocus1");
               // if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("autoplace", "Gotfocus2");
                    search(v.getText().toString());
                    return true;
             //   }

               // return false;
            }
        });*/

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                City city = cityList.get(position);
                if (position > -1) {
                    SelectedCitySerializable obj_selected_city = new SelectedCitySerializable(city.getName(),city.getCountry(),city.getLatitude(),city.getLongitude());
                    getIntent().putExtra("Selected_City",obj_selected_city);
                    setResult(RESULT_OK,getIntent());
                finish();
                }


        }});
    }
    private void search(String pattern) {

        data = null;
        String url ="http://api.openweathermap.org/data/2.5/find?mode=json&type=like&q="+pattern+"&cnt=10&APPID=ce6aa688507ccafca6ee59964d7195da";
        Log.d("autoplace", "Gotfocus3");

        try {
            new GetCities().execute(url);
            Log.d("autoplace","Connection success");

        } catch (Throwable t) {
            return;
        }

    }

    private class GetCities extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            String data;
            String line;
            try {
////
                String url= new String(urls[0]);

                connection = (HttpURLConnection) (new URL(url)).openConnection();
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
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Postexecute","OK");
            if (result.length()>0) {
                try {
                    cityList = getCityList(result);
                } catch (JSONException J) {
                }
                if (cityList != null){
                    adp.setCityList(cityList);
                    adp.notifyDataSetChanged();
                }
            }
        }
        public List<City> getCityList(String data) throws JSONException {
            JSONObject jObj = new JSONObject(data);
            JSONArray jArr = jObj.getJSONArray("list");

            List<City> cityList = new ArrayList<City>();

            for (int i=0; i < jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);

                String name = obj.getString("name");
                String id = obj.getString("id");
                JSONObject coord=obj.getJSONObject("coord");
                double lat= coord.getDouble("lat");
                double lon= coord.getDouble("lon");
                JSONObject sys = obj.getJSONObject("sys");
                String country = sys.getString("country");

                City c = new City(id,name,country,lat,lon);

                cityList.add(c);
            }


            return cityList;
        }

    }}



