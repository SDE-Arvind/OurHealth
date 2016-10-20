package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for information about specializations of a doctor.
 */
@SuppressWarnings("unused")
public class SpecializationsBean {
    @SerializedName("SpecializationId")
    private int mSpecializationId;
    @SerializedName("Specializations")
    private String mSpecializations;

    public int getSpecializationId() {
        return mSpecializationId;
    }

    public String getSpecialization() {
        return mSpecializations;
    }

}
