package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used to hold information for a doctor.
 */
@SuppressWarnings("unused")
public class DoctorListBean {
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("Name")
    private String mDoctorName;

    public int getDoctorId() {
        return mDoctorId;
    }

    public String getDoctorName() {
        return mDoctorName;
    }
}
