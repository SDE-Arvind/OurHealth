package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about password request.
 */
@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "unused"})
public class PasswordRequestBean {
    @SerializedName("emailId")
    private String mEmailId;
    @SerializedName("newPassword")
    private String mPassword;
    @SerializedName("securityQuestionId")
    private int mSecurityQuestionId;
    @SerializedName("answer")
    private String mSecurityAnswer;

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

    public int getSecurityQuestionId() {
        return mSecurityQuestionId;
    }

    public void setSecurityQuestionId(int mSecurityQuestionId) {
        this.mSecurityQuestionId = mSecurityQuestionId;
    }

    public String getSecurityAnswer() {
        return mSecurityAnswer;
    }

    public void setSecurityAnswer(String mSecurityAnswer) {
        this.mSecurityAnswer = mSecurityAnswer;
    }
}
