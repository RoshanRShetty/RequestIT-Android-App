package com.ttu.appathon.request.places.exception;

import com.ttu.appathon.request.places.Statuses;

public class InvalidRequestException extends GooglePlacesException {
    public InvalidRequestException(String errorMessage) {
        super(Statuses.STATUS_INVALID_REQUEST, errorMessage);
    }

    public InvalidRequestException() {
        this(null);
    }
}
