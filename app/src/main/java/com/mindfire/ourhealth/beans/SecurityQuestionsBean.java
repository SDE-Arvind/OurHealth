package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for information about professional qualifications of a doctor.
 */
@SuppressWarnings("unused")
public class SecurityQuestionsBean {
    @SerializedName("SecurityQuestionId")
    private int mSecurityQuestionId;
    @SerializedName("Question")
    private String mQuestion;

    public int getSecurityQuestionId() {
        return mSecurityQuestionId;
    }

    public String getQuestion() {
        return mQuestion;
    }

}
