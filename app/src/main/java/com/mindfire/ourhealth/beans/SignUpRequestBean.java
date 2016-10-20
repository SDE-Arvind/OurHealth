package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about sign up request.
 */
@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class SignUpRequestBean {
    @SerializedName("Name")
    private String mUserName;
    @SerializedName("EmailId")
    private String mEmailId;
    @SerializedName("Password")
    private String mPassword;
    @SerializedName("RoleId")
    private int mRoleId;
    @SerializedName("SecurityQuestionId")
    private int mSecurityQuesId;
    @SerializedName("Answer")
    private String mAnswer;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getRoleId() {
        return mRoleId;
    }

    public void setRoleId(int mRoleId) {
        this.mRoleId = mRoleId;
    }

    public int getSecurityQuesId() {
        return mSecurityQuesId;
    }

    public void setSecurityQuesId(int mSecurityQuesId) {
        this.mSecurityQuesId = mSecurityQuesId;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }
}
