package com.ttu.appathon.request.Food;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.ttu.appathon.request.places.RequestHandler;
import com.ttu.appathon.request.places.exception.GooglePlacesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vinay on 01-11-2015.
 */
public class FoodZomato implements FoodInterface {
    /*
     * Argument #1: API Base URL
     * Argument #2: API Method
     * Argument #3: API Method arguments
     */
    public static String API_URL_FORMAT_STRING = "%s%s/json?%s";

    private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
     * Creates a new GooglePlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public FoodZomato(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new GooglePlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public FoodZomato(String apiKey) {
        this(apiKey, new DefaultRequestHandler());
    }

    /**
     * Creates a new GooglePlaces object using the specified API key and character encoding. Using a character encoding
     * other than UTF-8 is not advised.
     *
     * @param apiKey            that has been registered on the Google Developer Console
     * @param characterEncoding to parse data with
     */
    public FoodZomato(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Params... extraParams) {
        for (Params param : extraParams)
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        return base;
    }

    private static String buildUrl(String params, Params... extraParams) {
        String api_url = "https://api.zomato.com/v1/search.json/near?";
        String url = api_url + params;
        //.format(Locale.ENGLISH, api_url, params);
        url = addExtraParams(url, extraParams);
        url = url.replace(' ', '+');
        return url;
    }

    /**
     * Parses the specified raw json String into a list of places.
     *
     * @param places to parse into
     * @param str    raw json
     * @param limit  the maximum amount of places to return
     * @return Next page token
     */
    public static String parse(FoodZomato client, List<RestaurantDetailsJsonParser> places, String str, int limit) {
        JSONObject json = null;
        String statusCode = null;
        JSONArray results = null;
        // parse json
        try {
            json = new JSONObject(str);
        } catch (JSONException E) {

        }
        // check root elements
        try {
            results = json.getJSONArray("results");
        } catch (JSONException E) {

        }
        parseResults(client, places, results, Math.min(limit, 10));

        return json.optString("Next_page", null);
    }

    private static void parseResults(FoodZomato client, List<RestaurantDetailsJsonParser> places, JSONArray results, int limit) {
        Bitmap main_icon = null;
        String main_icon_uri=null;
        for (int i = 0; i < limit; i++) {

            // reached the end of the page
            if (i >= results.length())
                return;
            try {
                JSONObject result = results.getJSONObject(i);
                JSONObject details = result.getJSONObject("result");
                // location
                //  JSONObject location = result.getJSONObject("Geometry").getJSONObject("location");
                // double lat = location.getDouble("latitude");
                //double lon = location.getDouble("longitude");

                String placeId = details.getString("id");
                String iconUrl = details.optString("photos_url", null);
                //String iconUrl = result.optString("icon", null);
                String name = details.optString("name");
                String addr = details.optString("address", null);
                String phone = details.optString("phone");
                double rating = details.optDouble("rating_aggregate",-1);

                // see if the place is open, fail-safe if opening_hours is not present
                // the place "types"
                /*List<String> types = new ArrayList<>();
                JSONArray jsonTypes = result.optJSONArray("types");
                if (jsonTypes != null) {
                    for (int a = 0; a < jsonTypes.length(); a++) {
                        types.add(jsonTypes.getString(a));
                    }
                }*/
                // photos
                JSONArray jsonPhotos = result.optJSONArray("photos");
                List<Photo> photos = new ArrayList<>();
                if (jsonPhotos != null) {
                    for (int j = 0; j < jsonPhotos.length(); j++) {
                        JSONObject jsonPhoto = jsonPhotos.getJSONObject(j);
                        String photoReference = jsonPhoto.getString("Reference");
                        int width = jsonPhoto.getInt("width"), height = jsonPhoto.getInt("height");
                        //
                        try {
                            String uri = String.format("%sphoto?photoreference=%s&key=%s&maxheight=100&maxwidth=100", "https://api.zomato.com/v1/search.json/near/", photoReference,
                                    "189ac73a52a3ee1a46826c9f9ebe2cb8");

                            URL imageURL = new URL(main_icon_uri);
                            Bitmap bm = BitmapFactory.decodeStream(imageURL.openStream());
                            main_icon= getResizedBitmap(bm, 80, 80);
                            // main_icon= client.download(uri);
                            if (main_icon_uri != null) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                RestaurantDetailsJsonParser place = new RestaurantDetailsJsonParser();

                // build a place object

                places.add(place.setClient(client).setPlaceId(placeId).setName(name).setAddress(addr).setPhoneNumber(phone).setRating(rating).setIconUrl(iconUrl));
            } catch (JSONException E) {
                E.printStackTrace();
            }
        }
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int     width           = bm.getWidth();
        int     height          = bm.getHeight();
        float   scaleWidth      = ((float) newWidth) / width;
        float   scaleHeight     = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    @Override
    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    @Override
    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public List<RestaurantDetailsJsonParser> getNearbyPlaces(double lat, double lng, double radius, int limit, Params... extraParams) {
        try {
            String uri = buildUrl(String.format(Locale.ENGLISH, "apikey=%s&lat=%f&lon=%f",
                    apiKey, lat, lng), extraParams);
            return getPlaces(uri, "Nearby_Search", limit);
        } catch (Exception e) {
            Log.d("FoodGetPlaces","eRROR");
            e.printStackTrace();
            return null;
        }

    }







   /* @Override
    public RestaurantDetailsJsonParser getPlaceById(String placeId, Params... extraParams) {
        try {
            String uri = buildUrl("Details", String.format("key=%s&placeid=%s", apiKey, placeId), extraParams);
         //   return RestaurantDetailsJsonParser.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
        return null;
    }*/


    protected Bitmap download(String uri) {
        try {
            Bitmap icon_bitnap = requestHandler.getInputStream(uri);
            if (icon_bitnap == null)
                throw new GooglePlacesException("Could not attain input stream at " + uri);
            debug("Successfully attained InputStream at " + uri);
            return icon_bitnap;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

   /* protected Bitmap downloadPhoto(Photo photo, int maxWidth, int maxHeight, Params... extraParams) {
        try {
            String uri = String.format("%sphoto?photoreference=%s&key=%s", "https://api.zomato.com/v1/search.json/near/", photo.getReference(),
                    apiKey);

            List<RestaurantDetailsJsonParser> params = new ArrayList<>(Arrays.asList(extraParams));
            if (maxHeight != -1) params.add(Params.name("maxheight").value(maxHeight));
            if (maxWidth != -1) params.add(Params.name("maxwidth").value(maxWidth));
            extraParams = params.toArray(new Params[][params.size()]);
            uri = addExtraParams(uri, extraParams);

            return download(uri);
        } catch (Exception e) {
           e.printStackTrace();
        }
    } */


    private List<RestaurantDetailsJsonParser> getPlaces(String uri, String method, int limit) throws IOException {


        List<RestaurantDetailsJsonParser> places = new ArrayList<>();
        // new request for each page
        for (int i = 0; i < 100; i++) {
            debug("Page: " + (i + 1));
            String raw = requestHandler.get(uri);
            debug(raw);
            String nextPage = parse(this, places, raw, limit);
            // reduce the limit, update the uri and wait for token, but only if there are more pages to read
            if (nextPage != null && i < 100 - 1) {
                limit -= 100;
                uri = String.format("%s%s/json?pagetoken=%s&key=%s",
                        "", method, nextPage, apiKey);
                sleep(3000); // Page tokens have a delay before they are available
            } else {
                break;
            }
        }

        return places;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
