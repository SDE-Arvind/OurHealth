package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about doctor profile.
 */
@SuppressWarnings("unused")
public class DoctorProfileBean {
    @SerializedName("DoctorId")
    private int mPatientId;
    @SerializedName("TitleId")
    private int mTitleId;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("FirstName")
    private String mFirstName;
    @SerializedName("LastName")
    private String mLastName;
    @SerializedName("GenderId")
    private int mGenderId;
    @SerializedName("Gender")
    private String mGender;
    @SerializedName("DOB")
    private String mDob;
    @SerializedName("Address")
    private ProfileAddressBean mAddressBean;
    @SerializedName("MedicalRegNo")
    private String mMedicalRegNo;
    @SerializedName("AboutMe")
    private String mAboutMe;
    @SerializedName("ContactNo")
    private String mContactNo;
    @SerializedName("Qualifications")
    private QualificationsBean[] mQualificationBeen;
    @SerializedName("Specializations")
    private SpecializationsBean[] mSpecializationsBeen;
    @SerializedName("PracticeLocations")
    private ProfilePracticeLocationBean[] mPracticeLocationBeen;

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public ProfileAddressBean getAddressBean() {
        return mAddressBean;
    }

    public String getMedicalRegNo() {
        return mMedicalRegNo;
    }

    public String getAboutMe() {
        return mAboutMe;
    }

    public String getContactNo() {
        return mContactNo;
    }

    public QualificationsBean[] getQualificationBeen() {
        return mQualificationBeen;
    }

    public SpecializationsBean[] getSpecializationsBeen() {
        return mSpecializationsBeen;
    }

    public ProfilePracticeLocationBean[] getPracticeLocationBeen() {
        return mPracticeLocationBeen;
    }
}