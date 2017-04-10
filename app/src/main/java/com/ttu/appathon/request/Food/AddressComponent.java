package com.ttu.appathon.request.Food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vinay on 29-10-2015.
 */
public class AddressComponent {
    private final List<String> types = new ArrayList<>();
    private String longName, shortName;

    protected AddressComponent() {
    }

    /**
     * Returns the long name of the component.
     *
     * @return long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets the long name of the component.
     *
     * @param longName of component
     * @return this
     */
    protected AddressComponent setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    /**
     * Returns the short name of the component. For example, "New York" might be abbreviated as "NY".
     *
     * @return short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name of the component. For example, "New York" might be abbreviated as "NY".
     *
     * @param shortName of component
     * @return this
     */
    protected AddressComponent setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    /**
     * Adds a type to this components list of types
     *
     * @param type to add
     * @return this
     */
    protected AddressComponent addType(String type) {
        types.add(type);
        return this;
    }

    /**
     * Returns an unmodifiable list of this components types.
     *
     * @return types
     */
    public List<String> getTypes() {
        return Collections.unmodifiableList(types);
    }
}
