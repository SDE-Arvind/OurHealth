package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about countries.
 */
@SuppressWarnings("unused")
public class CountryBean {
    @SerializedName("CountryId")
    private int mCountryId;
    @SerializedName("Country")
    private String mCountry;

    public int getCountryId() {
        return mCountryId;
    }

    public String getCountry() {
        return mCountry;
    }

}
