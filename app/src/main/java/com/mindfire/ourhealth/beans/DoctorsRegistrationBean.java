package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used to hold information about registration request for a doctor.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class DoctorsRegistrationBean {
    @SerializedName("UserId")
    private int mUserId;
    @SerializedName("TitleId")
    private int mTitleId;
    @SerializedName("FirstName")
    private String mFirstName;
    @SerializedName("LastName")
    private String mLastName;
    @SerializedName("GenderId")
    private int mGenderId;


    @SerializedName("Address")
    private AddressBean mAddress;
    @SerializedName("MedicalRegNo")
    private String mMedicalRegistrationNo;
    @SerializedName("AboutMe")
    private String mAboutMe;
    @SerializedName("ContactNo")
    private String mContactNumber;

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }
    public void setGenderId(int mGenderId) {
        this.mGenderId = mGenderId;
    }
    public void setTitleId(int mTitleId) {
        this.mTitleId = mTitleId;
    }

    public AddressBean getAddress() {
        return mAddress;
    }
    public void setAddress(AddressBean mAddress) {
        this.mAddress = mAddress;
    }

    public String getFirstName() {
        return mFirstName;
    }
    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }
    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getMedicalRegistrationNo() {
        return mMedicalRegistrationNo;
    }
    public void setMedicalRegistrationNo(String mMedicalRegistrationNo) {
        this.mMedicalRegistrationNo = mMedicalRegistrationNo;
    }

    public String getAboutMe() {
        return mAboutMe;
    }
    public void setAboutMe(String mAboutMe) {
        this.mAboutMe = mAboutMe;
    }

    public String getContactNumber() {
        return mContactNumber;
    }
    public void setContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }
}
