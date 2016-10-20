package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This bean is used to hold doctor details filtered by location and specialization.
 */
@SuppressWarnings("unused")
public class FilteredDoctorsBean {
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("Name")
    private String mName;
    @SerializedName("Gender")
    private String mGender;
    @SerializedName("Qualification")
    private String[] mQualifications;
    @SerializedName("Specialization")
    private String[] mSpecializations;
    @SerializedName("Practice")
    private DoctorPracticeLocationBean[] mPracticeLocations;
    @SerializedName("AboutMe")
    private String mAboutMe;
    @SerializedName("Image")
    private String mImage;

    public int getDoctorId() {
        return mDoctorId;
    }

    public String getName() {
        return mName;
    }

    public DoctorPracticeLocationBean[] getPracticeLocations() {
        return mPracticeLocations;
    }

    public String[] getSpecializations() {
        return mSpecializations;
    }
}
