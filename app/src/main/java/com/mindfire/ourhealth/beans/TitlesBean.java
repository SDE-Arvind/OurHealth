package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for information about professional qualifications of a user.
 */
@SuppressWarnings("unused")
public class TitlesBean {
    @SerializedName("TitleId")
    private int mTitleId;
    @SerializedName("Title")
    private String mTitle;

    public int getTitleId() {
        return mTitleId;
    }

    public String getTitle() {
        return mTitle;
    }

}
