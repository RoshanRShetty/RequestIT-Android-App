package com.ttu.appathon.request.places;

/**
 * Created by Manohar on 27-Oct-15.
 */

/**
 * Represents an alternate entry for a specified {@link Place}
 */
public class AltId {
    private final FindPlaces client;
    private final String placeId;
    private final Scope scope;

    protected AltId(FindPlaces client, String placeId, Scope scope) {
        this.client = client;
        this.placeId = placeId;
        this.scope = scope;
    }

    /**
     * Returns the placeId of the alternate entry.
     *
     * @return id of alternate entry
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Returns the scope of the alternate entry.
     *
     * @return scope of alternate entry
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns the actual alternate place.
     *
     * @return alternate place object
     */
    public Place getPlace() {
        return client.getPlaceById(placeId);
    }
}
