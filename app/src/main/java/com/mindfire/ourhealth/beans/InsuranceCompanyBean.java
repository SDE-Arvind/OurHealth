package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about health insurance companies.
 */
@SuppressWarnings("unused")
public class InsuranceCompanyBean {
    @SerializedName("CompanyId")
    private int mCompanyId;
    @SerializedName("CompanyName")
    private String mCompanyName;

    public int getCompanyId() {
        return mCompanyId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

}
