package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about add appointment request to the server.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class AddAppointmentBean {
    @SerializedName("UserId")
    private int mUserId;
    @SerializedName("PatientId")
    private int mPatientId;
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("LocationId")
    private int mLocationId;
    @SerializedName("DateOfAppointment")
    private String mDateOfAppointment;
    @SerializedName("Timing")
    private String mTiming;

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public void setPatientId(int mPatientId) {
        this.mPatientId = mPatientId;
    }

    public void setDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    public void setLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public void setDateOfAppointment(String mDateOfAppointment) {
        this.mDateOfAppointment = mDateOfAppointment;
    }

    public void setTiming(String mTiming) {
        this.mTiming = mTiming;
    }
}
