package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Primitives;

/**
 * This is a bean used for holding information about location of a doctor.
 */
//@SuppressWarnings("unused")
public class DoctorPracticeLocationBean {
        @SerializedName("LocationId")
        private int mLocationId;

        @SerializedName("LocationName")
        private String mLocationName;
        @SerializedName("Description")
        private String mDescription;
        @SerializedName("Address")
        private String mAddress;
        @SerializedName("Locality")
        private String mLocality;
        @SerializedName("City")
        private String mCity;
        @SerializedName("State")
        private String mState;
        @SerializedName("Country")
        private String mCountry;
        @SerializedName("PIN")
        private String mPin;
    /*

//    @SerializedName("UseId")
//    private  int mUserId;
//    @SerializedName("DoctorId")
//    private int mDoctorId;
//
//    @SerializedName("Location")
//    LocationBean mLocationBean;
*/

    @SerializedName("ContactPerson")
    private String mContactPerson;
    @SerializedName("ContactNo")
    private String mContactNo;


    @SerializedName("TimeIn")
    private String mTimeIn;
    @SerializedName("TimeOut")
    private String mTimeOut;
    @SerializedName("Days")
    private String mDays;

    public int getLocationId() {
        return mLocationId;
    }

    public String getLocationName() {
        return mLocationName;
    }

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

    public String getDays() {
        return mDays;
    }

    public void setTimeIn(){this.mTimeIn=mTimeIn;}
    public String getTimeIn() {
        return mTimeIn;
    }

    public void setTimeOut(String mTimeOut){this.mTimeOut=mTimeOut;}
    public String getTimeOut() {
        return mTimeOut;
    }

  }
