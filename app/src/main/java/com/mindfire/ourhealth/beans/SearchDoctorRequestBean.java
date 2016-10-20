package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about request for search doctor.
 */
@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class SearchDoctorRequestBean {
    @SerializedName("Locality")
    private String mLocality;
    @SerializedName("SpecializationsIds")
    private int mSpecializationIds[];

    public SearchDoctorRequestBean(String mLocality, int[] mSpecializationIds) {
        this.mLocality = mLocality;
        this.mSpecializationIds = mSpecializationIds;
    }
}
