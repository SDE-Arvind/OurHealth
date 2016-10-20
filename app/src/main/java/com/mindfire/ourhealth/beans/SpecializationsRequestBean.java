package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding data for the request made for entering the qualifications
 * for a doctor.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SpecializationsRequestBean {
    @SerializedName("UserId")
    private int mUserId;
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("SpecializationsIds")
    private int[] mSpecializationIds;

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public void setDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    public void setSpecializationIds(int[] mQualificationIds) {
        this.mSpecializationIds = mQualificationIds;
    }
}
