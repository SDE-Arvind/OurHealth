package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about Medical Test.
 */
@SuppressWarnings("unused")
public class MedicalTestBean {
    @SerializedName("MedicalTestId")
    private int mMedicalTestId;
    @SerializedName("MedicalTest")
    private String mMedicalTest;

    public int getMedicalTestId() {
        return mMedicalTestId;
    }

    public String getMedicalTest() {
        return mMedicalTest;
    }

}
