package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitsBean {
    @SerializedName("Date")
    private String mDate;
    @SerializedName("VisitId")
    private int mVisitId;
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
    @SerializedName("MedicalTests")
    private MedicalTestBean[] mMedicalTestBeans;

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getDate() {
        return mDate;
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

    public MedicalTestBean[] getMedicalTestBeans() {
        return mMedicalTestBeans;
    }
}
