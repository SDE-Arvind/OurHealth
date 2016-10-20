package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information of response received when the user tries to sign in.
 */
@SuppressWarnings("unused")
public class SignInResponseBean {
    @SerializedName("UserId")
    private int mUserId;
    @SerializedName("RoleId")
    private int mRoleId;
    @SerializedName("AuthToken")
    private String mAuthorizationToken;
    @SerializedName("IssuedOn")
    private String mIssuedOn;
    @SerializedName("ExpiresOn")
    private String mExpiresOn;

    public int getUserId() {
        return mUserId;
    }

    public int getRoleId() {
        return mRoleId;
    }

    public String getAuthorizationToken() {
        return mAuthorizationToken;
    }

}
