package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used to hold information about registration request for a patient.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class PatientRegistrationBean {
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
    @SerializedName("DOB")
    private String mDOB;
    @SerializedName("Address")
    private AddressBean mAddress;
    @SerializedName("ContactNo")
    private String mContact;
    @SerializedName("EmergencyContact")
    private String mEmergencyContact;
    @SerializedName("EmergencyNo")
    private String mEmergencyNumber;
    @SerializedName("CompanyId")
    private int mInsuranceCompanyId;
    @SerializedName("InsuranceNo")
    private String mInsuranceNumber;

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public void setTitleId(int mTitleId) {
        this.mTitleId = mTitleId;
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

    public void setGenderId(int mGenderId) {
        this.mGenderId = mGenderId;
    }

    public void setDOB(String mDOB) {
        this.mDOB = mDOB;
    }

    public void setAddress(AddressBean mAddress) {
        this.mAddress = mAddress;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String mContact) {
        this.mContact = mContact;
    }

    public String getEmergencyContact() {
        return mEmergencyContact;
    }

    public void setEmergencyContact(String mEmergencyContact) {
        this.mEmergencyContact = mEmergencyContact;
    }

    public String getEmergencyNumber() {
        return mEmergencyNumber;
    }

    public void setEmergencyNumber(String mEmergencyNumber) {
        this.mEmergencyNumber = mEmergencyNumber;
    }

    public void setInsuranceCompanyId(int mInsuranceCompanyId) {
        this.mInsuranceCompanyId = mInsuranceCompanyId;
    }

    public String getInsuranceNumber() {
        return mInsuranceNumber;
    }

    public void setInsuranceNumber(String mInsuranceNumber) {
        this.mInsuranceNumber = mInsuranceNumber;
    }
}
