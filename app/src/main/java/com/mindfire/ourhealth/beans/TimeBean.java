package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for storing time lookups.
 */
@SuppressWarnings("unused")
public class TimeBean {
    @SerializedName("TimeId")
    private int mTimeId;
    @SerializedName("Time")
    private String mTime;

    public String getTime() {
        return mTime;
    }

}
