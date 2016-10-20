package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding the data for request for adding visits.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class AddVisitsRequestBean {
    @SerializedName("UserId")
    private int mUserId;
    @SerializedName("AppointmentId")
    private int mAppointmentId;
    @SerializedName("Weight")
    private String mWeight;
    @SerializedName("Height")
    private String mHeight;
    @SerializedName("Pulse")
    private String mPulse;
    @SerializedName("Temperature")
    private String mTemperature;
    @SerializedName("Medication")
    private String mMedication;
    @SerializedName("Diagnosis")
    private String mDiagnosis;
    @SerializedName("MedicalTestIds")
    private int[] mMedicalTestIds;

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public void setAppointmentId(int mAppointmentId) {
        this.mAppointmentId = mAppointmentId;
    }

    public void setWeight(String mWeight) {
        this.mWeight = mWeight;
    }

    public void setHeight(String mHeight) {
        this.mHeight = mHeight;
    }

    public void setPulse(String mPulse) {
        this.mPulse = mPulse;
    }

    public void setTemperature(String mTemperature) {
        this.mTemperature = mTemperature;
    }

    public void setMedication(String mMedication) {
        this.mMedication = mMedication;
    }

    public void setDiagnosis(String mDiagnosis) {
        this.mDiagnosis = mDiagnosis;
    }

    public void setMedicalTestIds(int[] mMedicalTestIds) {
        this.mMedicalTestIds = mMedicalTestIds;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getHeight() {
        return mHeight;
    }

    public String getPulse() {
        return mPulse;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getMedication() {
        return mMedication;
    }

    public String getDiagnosis() {
        return mDiagnosis;
    }
}
