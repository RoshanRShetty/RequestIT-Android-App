package com.ttu.appathon.request.Hotels;

/*import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
*/
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ttu.appathon.request.places.RequestHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefaultRequestHandler implements RequestHandler {
    /**
     * The default and recommended character encoding.
     */
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
//    private final HttpClient client = HttpClientBuilder.create().build();
    private String characterEncoding;

    /**
     * Creates a new handler with the specified character encoding.
     *
     * @param characterEncoding to use
     */
    public DefaultRequestHandler(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * Creates a new handler with UTF-8 character encoding.
     */
    public DefaultRequestHandler() {
        this(DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Returns the character encoding used by this handler.
     *
     * @return character encoding
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Sets the character encoding used by this handler.
     *
     * @param characterEncoding to use
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /*private String readString(HttpResponse response) throws IOException {
        String str = IOUtils.toString(response.getEntity().getContent(), characterEncoding);
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }*/

    @Override
    public Bitmap getInputStream(String uri) throws IOException {
        HttpURLConnection connection = null;
        InputStream in=null;
        Bitmap bm=null;
        byte[] buffer = new byte[1024];
        try {

            connection = (HttpURLConnection) (new URL(uri)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            in = new BufferedInputStream(connection.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(in,1024);
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int len=0;

                while((len = bis.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
                out.close();
                bis.close();
            buffer=null;
                byte[] data = out.toByteArray();
                 bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            data=null;
            return bm;
        }catch(Exception E) {
            //
            Log.d("DefReQ","IcondownloadError");
        }
        finally {
connection.disconnect();
        }
      return bm;
    }

    @Override
    public String get(String uri) throws IOException {
        HttpURLConnection connection = null;
        StringBuffer buffer = new StringBuffer();
        String data;
        try{

        connection = (HttpURLConnection) (new URL(uri)).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

       String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line + "\r\n");
        }
    } catch (Throwable t) {
            Log.e("Find_Places", "Unable to get places", t);

        }
    data=buffer.toString();
    return data;
    }

/*    @Override
    public String post(HttpPost data) throws IOException {
        try {
            return readString(client.execute(data));
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            data.releaseConnection();
        }
    }*/
}
