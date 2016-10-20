package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about the address for user profile.
 */
//@SuppressWarnings("unused")
public class ProfileAddressBean {
    @SerializedName("Address")
    private String mAddress;
    @SerializedName("Locality")
    private String mLocality;
    @SerializedName("City")
    private String mCity;
    @SerializedName("StateId")
    private int mStateId;
    @SerializedName("State")
    private String mState;
    @SerializedName("CountryId")
    private int mCountryId;
    @SerializedName("Country")
    private String mCountry;
    @SerializedName("PIN")
    private String mPin;

    public String getAddress() {
        return mAddress;
    }

    public String getLocality() {
        return mLocality;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getPin() {
        return mPin;
    }

}
