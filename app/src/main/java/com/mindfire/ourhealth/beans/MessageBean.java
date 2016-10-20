package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arvind Kumar on 12-Sep-16.
 */
public class MessageBean {
    @SerializedName("Message")
    private String mMessage;
    public String getMessage() {
        return mMessage;
    }
}
