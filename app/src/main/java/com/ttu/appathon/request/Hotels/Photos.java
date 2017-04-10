package com.ttu.appathon.request.Hotels;

import android.graphics.Bitmap;

import com.ttu.appathon.request.places.FindPlacesInterface;

import java.io.InputStream;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;

/**
 * Represents a referenced photo.
 */
public class Photos {
    private final HotelDetails hotel;
    private final String reference;
    private final int width, height;
    private InputStream image;
    private Bitmap image_bitmap;

    protected Photos(HotelDetails hotel, String reference, int width, int height) {
        this.hotel = hotel;
        this.reference = reference;
        this.width = width;
        this.height = height;
       this.image_bitmap= download(FindPlacesInterface.MAX_PHOTO_SIZE, FindPlacesInterface.MAX_PHOTO_SIZE);
    }

    /**
     * Downloads the photo and caches it within the photo.
     *
     * @param maxWidth    of photo
     * @param maxHeight   of photo
     * @param extraParams to append to request url
     * @return this
     */
    public Bitmap download(int maxWidth, int maxHeight, Params... extraParams) {
        return hotel.getClient().downloadPhoto(this, maxWidth, maxHeight, extraParams);

    }

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
     * Returns the input stream of the image. {@link #download(int, int, Params...)}
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
