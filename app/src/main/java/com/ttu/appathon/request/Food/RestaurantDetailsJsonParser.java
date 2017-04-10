package com.ttu.appathon.request.Food;

import android.graphics.Bitmap;

import com.ttu.appathon.request.places.AddressComponent;
import com.ttu.appathon.request.places.Hours;
import com.ttu.appathon.request.places.Photo;
import com.ttu.appathon.request.places.Review;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//import static com.ttu.appathon.request.places.GooglePlacesInterface.ARRAY_PHOTOS;
//import static com.ttu.appathon.request.places.GooglePlacesInterface.INTEGER_HEIGHT;
//import static com.ttu.appathon.request.places.GooglePlacesInterface.INTEGER_WIDTH;
//import static com.ttu.appathon.request.places.GooglePlacesInterface.STRING_PHOTO_REFERENCE;

/**
 * Created by Vinay on 29-10-2015.
 */
public class RestaurantDetailsJsonParser {
    private final List<String> types = new ArrayList<>();
    private final List<Photo> photos = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();
    private final List<AddressComponent> addressComponents = new ArrayList<>();
    private double lat = -1, lng = -1;
    private String placeId;
    private JSONObject json;
    private String iconUrl;

    private InputStream icon;
    private Bitmap icon_bitmap;
    private String name;
    private String addr;
    private String phone, internationalPhone;
    private Hours hours;
    private FoodZomato client;
    private double rating = -1;

    protected RestaurantDetailsJsonParser() {

    }

   /* public static RestaurantDetailsJsonParser parseDetails(FoodZomato client, String rawJson) {
        JSONObject result=null;
        JSONObject json=null;
        try {
            json = new JSONObject(rawJson);

            result = json.getJSONObject("result");

            String name = result.getString("name");
            String id = result.getString("ID");
            String address = result.optString("Address", null);
            String phone = result.optString("Phone_number", null);
            String iconUrl = result.optString("icon", null);
            String internationalPhone = result.optString("phone_number", null);

            JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
            double lat = location.getDouble("latitude"), lng = location.getDouble("longitude");


            RestaurantDetailsJsonParser res = new RestaurantDetailsJsonParser();
            RestaurantDetailsJsonParser place = new RestaurantDetailsJsonParser();
            JSONArray jsonPhotos = result.optJSONArray(ARRAY_PHOTOS);
            List<Photo> photos = new ArrayList<>();
            if (jsonPhotos != null) {
                for (int i = 0; i < jsonPhotos.length(); i++) {
                    JSONObject jsonPhoto = jsonPhotos.getJSONObject(i);
                    String photoReference = jsonPhoto.getString(STRING_PHOTO_REFERENCE);
                    int width = jsonPhoto.getInt(INTEGER_WIDTH), height = jsonPhoto.getInt(INTEGER_HEIGHT);
                    photos.add(new Photo(place, photoReference, width, height));
                }
            }

        JSONArray addrComponents = result.optJSONArray("Array_address");
        List<AddressComponent> addressComponents = new ArrayList<>();
        if (addrComponents != null) {
            for (int i = 0; i < addrComponents.length(); i++) {
                JSONObject ac = addrComponents.getJSONObject(i);
                AddressComponent addr = new AddressComponent();

                String longName = ac.optString("long_name", null);
                String shortName = ac.optString("Short_name", null);

                addr.setLongName(longName);
                addr.setShortName(shortName);

                // address components have types too
                JSONArray types = ac.optJSONArray("array_types");
                if (types != null) {
                    for (int a = 0; a < types.length(); a++) {
                        addr.addType(types.getString(a));
                    }
                }
                addressComponents.add(addr);
            }
        }
        JSONArray jsonTypes = result.optJSONArray("Array_types");
        List<String> types = new ArrayList<>();
        if (jsonTypes != null) {
            for (int i = 0; i < jsonTypes.length(); i++) {
                types.add(jsonTypes.getString(i));
            }
        }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }*/

    public Bitmap getIcon_Bitmap() {
        return icon_bitmap;
    }
    protected RestaurantDetailsJsonParser setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }


    public FoodZomato getClient()
    {
        return client;
    }

    protected RestaurantDetailsJsonParser setClient(FoodZomato food) {
        this.client = food;
        return this;
    }

    public  double getRating(){return rating;}

    protected RestaurantDetailsJsonParser setRating(double rating) {
        this.rating = rating;
        return  this;
    }
    public String getPlaceId() {
        return placeId;
    }

    protected RestaurantDetailsJsonParser setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    public double getLatitude() {
        return lat;
    }


    protected RestaurantDetailsJsonParser setLatitude(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLongitude() {
        return lng;
    }


    protected RestaurantDetailsJsonParser setLongitude(double lon) {
        this.lng = lon;
        return this;
    }

    public String getPhoneNumber() {
        return phone;
    }


    protected RestaurantDetailsJsonParser setPhoneNumber(String phone) {
        this.phone = phone;
        return this;
    }

    protected  Bitmap getMainIcon() {
        return icon_bitmap;

    }

    public String getName() {
        return name;
    }


    protected RestaurantDetailsJsonParser setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return addr;
    }

    protected RestaurantDetailsJsonParser setAddress(String addr) {
        this.addr = addr;
        return this;
    }

    protected RestaurantDetailsJsonParser addAddressComponents(Collection<AddressComponent> c) {
        this.addressComponents.addAll(c);
        return this;
    }

    public List<AddressComponent> getAddressComponents() {
        return Collections.unmodifiableList(addressComponents);
    }

    protected RestaurantDetailsJsonParser addPhotos(Collection<Photo> photos) {
        this.photos.addAll(photos);
        return this;
    }

    public List<Photo> getPhotos() {
        return Collections.unmodifiableList(photos);
    }



    public JSONObject getJson() {
        return json;
    }


    protected RestaurantDetailsJsonParser setJson(JSONObject json) {
        this.json = json;
        return this;
    }


}



