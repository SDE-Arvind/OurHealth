package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about doctors qualifications.
 */
@SuppressWarnings("unused")
public class QualificationsBean {
    @SerializedName("QualificationId")
    private int mQualificationId;
    @SerializedName("Qualification")
    private String mQualification;

    public int getQualificationId() {
        return mQualificationId;
    }

    public String getQualification() {
        return mQualification;
    }

}
