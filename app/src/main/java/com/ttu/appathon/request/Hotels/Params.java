package com.ttu.appathon.request.Hotels;

/**
 * Represents an extra, optional parameter that can be specified.
 */
public class Params {
    protected final String name;
    protected String value;

    public Params(String name) {
        this.name = name;
    }

    /**
     * Returns a new param with the specified name.
     *
     * @param name to create Param from
     * @return new param
     */
    public static Params name(String name) {
        return new Params(name);
    }

    /**
     * Sets the value of the Param.
     *
     * @param value of param
     * @return this param
     */
    public Params value(Object value) {
        this.value = value.toString();
        return this;
    }
}
