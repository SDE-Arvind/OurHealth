package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about states of a country.
 */
@SuppressWarnings("unused")
public class StatesBean{
    @SerializedName("StateId")
    private int mStateId;
    @SerializedName("CountryID")
    private int mCountryId;
    @SerializedName("State")
    private String mState;

    public int getStateId() {
        return mStateId;
    }

    public String getStateName() {
        return mState;
    }
}
