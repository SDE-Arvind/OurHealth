package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is a bean used for holding information about location a user.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class LocationBean {
    @SerializedName("LocationId")
    private int mLocationId;
    @SerializedName("LocationName")
    private String mLocationName;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("Address")
    private AddressBean mAddress;
    @SerializedName("ContactPerson")
    private String mContactPerson;
    @SerializedName("ContactNo")
    private String mContactNo;


    public void setAddress(AddressBean mAddress) {
        this.mAddress = mAddress;
    }

    public AddressBean getAddress() { return mAddress;}


    public int getLocationId() {
        return mLocationId;
    }
    public void setLocationId(int mLocationId) { this.mLocationId= mLocationId;}

    public String getLocationName() {
        return mLocationName;
    }
    public void setLocationName(String mLocationName) { this.mLocationName= mLocationName;}

    public String getContactPerson() {
        return mContactPerson;
    }
    public void setContactPerson(String mContactPerson) { this.mContactPerson= mContactPerson;}

    public String getContactNo() {
        return mContactNo;
    }
    public void setContactNo(String mContactNo) { this.mContactNo= mContactNo;}
}
