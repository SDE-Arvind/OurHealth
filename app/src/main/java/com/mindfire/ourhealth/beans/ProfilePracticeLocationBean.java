package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding practice location for doctor profile screen.
 */
@SuppressWarnings("ALL")
public class ProfilePracticeLocationBean {
    @SerializedName("PracticeId")
    private int mPracticeId;
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("Location")
    private LocationBean mLocationBean;
    @SerializedName("TimeInId")
    private int mTimeInId;
    @SerializedName("TimeIn")
    private String mTimeIn;
    @SerializedName("TimeOutId")
    private int mTimeOutId;
    @SerializedName("TimeOut")
    private String mTimeOut;
    @SerializedName("Days")
    private String mDays;

    public int getPracticeId() {
        return mPracticeId;
    }

    public int getDoctorId() {
        return mDoctorId;
    }

    public LocationBean getLocationBean() {
        return mLocationBean;
    }

    public int getTimeInId() {
        return mTimeInId;
    }

    public String getTimeIn() {
        return mTimeIn;
    }

    public int getTimeOutId() {
        return mTimeOutId;
    }

    public String getTimeOut() {
        return mTimeOut;
    }

    public String getDays() {
        return mDays;
    }
}
