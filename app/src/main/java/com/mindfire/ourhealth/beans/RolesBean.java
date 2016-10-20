package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about countries.
 */
@SuppressWarnings("unused")
public class RolesBean {
    @SerializedName("RoleId")
    private int mRoleId;
    @SerializedName("Role")
    private String mRole;

    public int getRoleId() {
        return mRoleId;
    }

    public String getRole() {
        return mRole;
    }

}
