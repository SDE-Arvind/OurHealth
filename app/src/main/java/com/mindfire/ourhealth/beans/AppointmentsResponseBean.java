package com.mindfire.ourhealth.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This is bean used for holding information about appointments.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class AppointmentsResponseBean {
    @SerializedName("AppointmentId")
    private int mAppointmentId;
    @SerializedName("PatientId")
    private int mPatientId;
    @SerializedName("PatientName")
    private String mPatientName;
    @SerializedName("DoctorId")
    private int mDoctorId;
    @SerializedName("DoctorName")
    private String mDoctorName;
    @SerializedName("Location")
    private LocationBean mLocation;
    @SerializedName("DateOfAppointment")
    private String mDateOfAppointment;
    @SerializedName("Timing")
    private String mTiming;
    @SerializedName("VisitStatus")
    private boolean mVisitStatus;

    public int getAppointmentId() {
        return mAppointmentId;
    }

    public String getPatientName() {
        return mPatientName;
    }

    public String getDoctorName() {
        return mDoctorName;
    }

    public LocationBean getLocation() {
        return mLocation;
    }

    public String getDateOfAppointment() {
        return mDateOfAppointment;
    }

    public String getTiming() {
        return mTiming;
    }

    public boolean isVisitStatus() {
        return mVisitStatus;
    }
}
