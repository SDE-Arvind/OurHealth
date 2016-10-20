package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This a bean used for holding information for a user.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class AddressBean {
    @SerializedName("Address")
    private String mAddress;
    @SerializedName("Locality")
    private String mLocality;
    @SerializedName("City")
    private String mCity;
    @SerializedName("StateId")
    private int mStateId;
    @SerializedName("CountryId")
    private int mCountryId;
    @SerializedName("PIN")
    private String mPin;

    public AddressBean() {
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String mLocality) {
        this.mLocality = mLocality;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public void setStateId(int mStateId) {
        this.mStateId = mStateId;
    }

    public void setCountryId(int mCountryId) {
        this.mCountryId = mCountryId;
    }

    public String getPin() {
        return mPin;
    }

    public void setPin(String mPin) {
        this.mPin = mPin;
    }
}
