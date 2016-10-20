package com.mindfire.ourhealth.beans;

public class MyCheckBoxBean {
    private final String mName;
    private boolean mSelected;

    public MyCheckBoxBean(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
