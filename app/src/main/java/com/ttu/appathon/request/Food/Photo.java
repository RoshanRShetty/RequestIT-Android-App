package com.ttu.appathon.request.Food;

import android.graphics.Bitmap;

import java.io.InputStream;

//import static com.ttu.appathon.request.places.GooglePlacesInterface.MAX_PHOTO_SIZE;

/**
 * Created by Vinay on 29-10-2015.
 */
public class Photo {
    private final RestaurantDetailsJsonParser rest;
    private final String reference;
    private final int width, height;
    private InputStream image;
    private Bitmap image_bitmap;

    public Photo(RestaurantDetailsJsonParser place, String reference, int width, int height) {
        this.rest = place;
        this.reference = reference;
        this.width = width;
        this.height = height;
        //this.image_bitmap= download(MAX_PHOTO_SIZE, MAX_PHOTO_SIZE);
    }

    /**
     * Downloads the photo and caches it within the photo.
     *
     * @param maxWidth    of photo
     * @param maxHeight   of photo
     * @param extraParams to append to request url
     * @return this
     */
    /*public Bitmap download(int maxWidth, int maxHeight, Params... extraParams) {

        return getClient().downloadPhoto(this, maxWidth, maxHeight, extraParams);

    } */



    /**
     * Downloads the photo and caches it within the photo.
     *
     * @param extraParams to append to request url
     * @return this
     */
  /* public Photo download(Param... extraParams) {
        return download(MAX_PHOTO_SIZE, MAX_PHOTO_SIZE, extraParams);
    }*/

    /**
     * Returns the input stream of the image. {@link #download(int, int, Param...)}
     * must be called prior to calling this.
     *
     * @return input stream
     */
    public InputStream getInputStream() {
        return image;
    }
    public Bitmap getBitmap() {
        return image_bitmap;
    }

    /**
     * Returns an Image from the specified photo reference.
     *
     * @return image
     */
  /*  public BufferedImage getImage() {
        try {
            return ImageIO.read(image);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }*/

    /**
     * Returns the reference token to the photo.
     *
     * @return reference token
     */
    public String getReference() {
        return reference;
    }

    /**
     * Returns the width of the photo.
     *
     * @return photo width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the photo.
     *
     * @return photo height
     */
    public int getHeight() {
        return height;
    }

}
