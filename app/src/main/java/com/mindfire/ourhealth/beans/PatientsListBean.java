package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about patients list.
 */
@SuppressWarnings("unused")
public class PatientsListBean {
    @SerializedName("PatientId")
    private Integer mPatientId;
    @SerializedName("Name")
    private String mPatientName;
    @SerializedName("Age")
    private Integer mAge;
    @SerializedName("Locality")
    private String mLocality;
    @SerializedName("City")
    private String mCity;

    public Integer getPatientId() {
        return mPatientId;
    }

    public String getPatientName() {
        return mPatientName;
    }

}
