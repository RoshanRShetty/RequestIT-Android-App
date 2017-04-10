package com.ttu.appathon.request.Hotels;

//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.ttu.appathon.request.places.DefaultRequestHandler;
import com.ttu.appathon.request.places.Price;
import com.ttu.appathon.request.places.RequestHandler;
import com.ttu.appathon.request.places.Status;
import com.ttu.appathon.request.places.exception.GooglePlacesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Main class of API. Used for all entry web-api operations.
 */
public class HotelsAPI implements HotelsAPIInterface {

    public static String API_URL_FORMAT_STRING = "%s%s/json?%s";

    private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
     * Creates a new FindPlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public HotelsAPI(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new FindPlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public HotelsAPI(String apiKey) {
        this(apiKey, new com.ttu.appathon.request.places.DefaultRequestHandler());
    }


    public HotelsAPI(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Params... extraParams) {
        for (Params param : extraParams)
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        return base;
    }

    private static String buildUrl(String method, String params, Params... extraParams) {
        String url = String.format(Locale.ENGLISH, API_URL_FORMAT_STRING, API_URL, method, params);
        url = addExtraParams(url, extraParams);
        url = url.replace(' ', '+');
        return url ;
    }

    protected static void checkStatus(String statusCode, String errorMessage) {
        GooglePlacesException e = GooglePlacesException.parse(statusCode, errorMessage);
        if (e != null)
            throw e;
    }

    /**
     * Parses the specified raw json String into a list of places.
     *
     * @param hotels to parse into
     * @param str    raw json
     * @param limit  the maximum amount of places to return
     * @return Next page token
     */
    public static String parse(HotelsAPI client, List<HotelDetails> hotels, String str, int limit) {
        JSONObject json=null;
        String statusCode=null;
        JSONArray results=null;
        // parse json
        try {
            json = new JSONObject(str);
        }catch (JSONException E){

        }
        // check root elements
        try{
        statusCode = json.getString(STRING_STATUS);
    }catch (JSONException E){

    }
        checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
        if (statusCode.equals(STATUS_ZERO_RESULTS))
            return null;
try{
        results = json.getJSONArray(ARRAY_RESULTS);
    }catch (JSONException E){

    }
        parseResults(client, hotels, results, Math.min(limit, MAXIMUM_PAGE_RESULTS));

        return json.optString(STRING_NEXT_PAGE_TOKEN, null);
    }

    private static void parseResults(HotelsAPI client, List<HotelDetails> places, JSONArray results, int limit) {
        Bitmap main_icon=null;
        String main_icon_uri=null;
        for (int i = 0; i < limit; i++) {

            // reached the end of the page
            if (i >= results.length())
                return;
try{
            JSONObject result = results.getJSONObject(i);

            // location
            JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
            double lat = location.getDouble(DOUBLE_LATITUDE);
            double lon = location.getDouble(DOUBLE_LONGITUDE);

            String placeId = result.getString(STRING_PLACE_ID);
            String iconUrl = result.optString(STRING_ICON, null);
            String name = result.optString(STRING_NAME);
            String addr = result.optString(STRING_ADDRESS, null);
            double rating = result.optDouble(DOUBLE_RATING, -1);
            String vicinity = result.optString(STRING_VICINITY, null);

            // see if the place is open, fail-safe if opening_hours is not present
            JSONObject hours = result.optJSONObject(OBJECT_HOURS);
            boolean hoursDefined = hours != null && hours.has(BOOLEAN_OPENED);
            Status status = Status.NONE;
            if (hoursDefined) {
                boolean opened = hours.getBoolean(BOOLEAN_OPENED);
                status = opened ? Status.OPENED : Status.CLOSED;
            }

            // get the price level for the place, fail-safe if not defined
            boolean priceDefined = result.has(INTEGER_PRICE_LEVEL);
            Price price = Price.NONE;
            if (priceDefined) {
                price = Price.values()[result.getInt(INTEGER_PRICE_LEVEL)];
            }

            // the place "types"
            List<String> types = new ArrayList<>();
            JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
            if (jsonTypes != null) {
                for (int a = 0; a < jsonTypes.length(); a++) {
                    types.add(jsonTypes.getString(a));
                }
            }
    // photos
    JSONArray jsonPhotos = result.optJSONArray(ARRAY_PHOTOS);
    List<Photos> photos = new ArrayList<>();
    if (jsonPhotos != null) {
        for (int j = 0; j < jsonPhotos.length(); j++) {
            JSONObject jsonPhoto = jsonPhotos.getJSONObject(j);
            String photoReference = jsonPhoto.getString(STRING_PHOTO_REFERENCE);
            int width = jsonPhoto.getInt(INTEGER_WIDTH), height = jsonPhoto.getInt(INTEGER_HEIGHT);
            //
            try {
                 main_icon_uri = String.format("%sphoto?photoreference=%s&key=%s&maxheight=100&maxwidth=100", API_URL, photoReference,
                         API_KEY);
                URL imageURL = new URL(main_icon_uri);
                Bitmap bm = BitmapFactory.decodeStream(imageURL.openStream());
                main_icon= getResizedBitmap(bm, 80,80);
                // main_icon= client.download(uri);
                if (main_icon_uri != null) {
                    break;
                }


            } catch (Exception e) {
                throw new GooglePlacesException(e);
            }
        }
    }
    else
    {
        try {
            URL imageURL = new URL(iconUrl);
            Bitmap bm = BitmapFactory.decodeStream(imageURL.openStream());
            main_icon = getResizedBitmap(bm, 80, 80);
        }catch (Exception E){

        }

    }
            HotelDetails place = new HotelDetails();

            // build a place object
            places.add(place.setClient(client).setPlaceId(placeId).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
                    .setAddress(addr).setRating(rating).setStatus(status).setPrice(price)
                    .addTypes(types).setVicinity(vicinity).setJson(result).setMainIcon(main_icon));
}catch (JSONException E){
    Log.d("Google places","parse");
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
    public List<HotelDetails> getNearbyPlaces(double lat, double lng, double radius, int limit, Accomadations.HotelResults hotel_results,Params... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f&type=lodging",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit, hotel_results);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    public List<HotelDetails> getNearbyPlaces(Accomadations.HotelResults hotel_results, int limit, Params... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "pagetoken=%s&key=%s"
                    , hotel_results.next_page_token, apiKey), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit, hotel_results);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }





    @Override
    public HotelDetails getPlaceById(String placeId, Params... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&placeid=%s", apiKey, placeId), extraParams);
            return HotelDetails.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }


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

    protected Bitmap downloadPhoto(Photos photo, int maxWidth, int maxHeight, Params... extraParams) {
        try {
            String uri = String.format("%sphoto?photoreference=%s&key=%s", API_URL, photo.getReference(),
                    apiKey);

            List<Params> params = new ArrayList<>(Arrays.asList(extraParams));
            if (maxHeight != -1) params.add(Params.name("maxheight").value(maxHeight));
            if (maxWidth != -1) params.add(Params.name("maxwidth").value(maxWidth));
            extraParams = params.toArray(new Params[params.size()]);
            uri = addExtraParams(uri, extraParams);

            return download(uri);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }


    private List<HotelDetails> getPlaces(String uri, String method, int limit,Accomadations.HotelResults place_results) throws IOException {
        limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
        int pages = (int) Math.ceil(limit / (double) MAXIMUM_PAGE_RESULTS);

       List<HotelDetails> places = new ArrayList<>();
            String raw = requestHandler.get(uri);
            debug(raw);
            String nextPage = parse(this, places, raw, limit);
        place_results.next_page_token=nextPage;
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
