package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about .
 */
@SuppressWarnings("unused")
public class GenderBean {
    @SerializedName("GenderId")
    private int mGenderId;
    @SerializedName("Gender")
    private String mGender;

    public int getGenderId() {
        return mGenderId;
    }

    public String getGender() {
        return mGender;
    }

}
