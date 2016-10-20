package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding patient profile data.
 */
@SuppressWarnings("unused")
public class PatientProfileBean {
    @SerializedName("PatientId")
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
    @SerializedName("ContactNo")
    private String mContactNo;
    @SerializedName("EmergencyContact")
    private String mEmergencyContact;
    @SerializedName("EmergencyNo")
    private String mEmergencyNumber;
    @SerializedName("CompanyId")
    private int mCompanyId;
    @SerializedName("InsuranceCompany")
    private String mInsuranceCompany;
    @SerializedName("InsuranceNo")
    private String mInsuranceNo;

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public String getDob() {
        return mDob;
    }

    public void setDob(String mDob) {
        this.mDob = mDob;
    }

    public ProfileAddressBean getAddressBean() {
        return mAddressBean;
    }

    public String getContactNo() {
        return mContactNo;
    }

    public String getEmergencyContact() {
        return mEmergencyContact;
    }

    public String getEmergencyNumber() {
        return mEmergencyNumber;
    }

    public String getInsuranceCompany() {
        return mInsuranceCompany;
    }

    public String getInsuranceNo() {
        return mInsuranceNo;
    }
}
