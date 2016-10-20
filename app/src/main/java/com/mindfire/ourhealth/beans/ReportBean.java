package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used to hold information about report of a patient.
 */
@SuppressWarnings("unused")
public class ReportBean {
    @SerializedName("DocumentId")
    private int mDocumentId;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("DateUploaded")
    private String mDate;

    public int getDocumentId() {
        return mDocumentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDate() {
        return mDate;
    }
}
