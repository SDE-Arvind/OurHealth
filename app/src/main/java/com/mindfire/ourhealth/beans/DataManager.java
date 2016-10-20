package com.mindfire.ourhealth.beans;

import java.util.List;

/**
 * This is a singleton used for storing temporary data.
 */
public class DataManager {

    private static final DataManager soleInstance = new DataManager();

    private SignInResponseBean mSignInResponse;
    private int mPatientId;
    private int mDoctorId;
    private PatientProfileBean mPatientProfileBean;
    private DoctorProfileBean mDoctorProfileBean;
    private SearchDoctorRequestBean mSearchDoctorRequestBean;
    private ReportBean mReportBean;

    private PatientsListBean[] mPatientsList;
    private DoctorListBean[] mDoctorsList;
    private AppointmentsResponseBean[] mAppointmentsList;
    private DoctorPracticeLocationBean[] mDoctorPracticeLocationBeen;
    private DoctorPracticeLocationBean mDoctorPracticeLocationBean;
    private String[] mSpecializations;
    private List<String> mMedicalTests;
    private int[] mMedicalTestIds;
    private VisitsBean mVisitsBean;

    private DataManager() {}

    public static DataManager getSoleInstance() {
        return soleInstance;
    }

    public VisitsBean getVisitsBean() {
        return mVisitsBean;
    }

    public void setVisitsBean(VisitsBean mVisitsBean) {
        this.mVisitsBean = mVisitsBean;
    }

    public List<String> getMedicalTests() {
        return mMedicalTests;
    }

    public void setMedicalTests(List<String> mMedicalTests) {
        this.mMedicalTests = mMedicalTests;
    }

    public int[] getMedicalTestIds() {
        return mMedicalTestIds;
    }

    public void setMedicalTestIds(int[] mMedicalTestIds) {
        this.mMedicalTestIds = mMedicalTestIds;
    }

    public String[] getSpecializations() {
        return mSpecializations;
    }

    public void setSpecializations(String[] mSpecializations) {
        this.mSpecializations = mSpecializations;
    }

    public PatientProfileBean getPatientProfileBean() {
        return mPatientProfileBean;
    }

    public void setPatientProfileBean(PatientProfileBean mPatientProfileBean) {
        this.mPatientProfileBean = mPatientProfileBean;
    }

    public DoctorProfileBean getDoctorProfileBean() {
        return mDoctorProfileBean;
    }

    public void setDoctorProfileBean(DoctorProfileBean mDoctorProfileBean) {
        this.mDoctorProfileBean = mDoctorProfileBean;
    }

    public ReportBean getReportBean() {
        return mReportBean;
    }

    public void setReportBean(ReportBean mReportBean) {
        this.mReportBean = mReportBean;
    }

    public SignInResponseBean getSignInResponse() {
        return mSignInResponse;
    }

    public void setSignInResponse(SignInResponseBean mSignInResponse) {
        this.mSignInResponse = mSignInResponse;
    }

    public int getPatientId() {
        return mPatientId;
    }

    public void setPatientId(int mPatientId) {
        this.mPatientId = mPatientId;
    }

    public int getDoctorId() {
        return mDoctorId;
    }

    public void setDoctorId(int mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    public AppointmentsResponseBean[] getAppointmentsList() {
        return mAppointmentsList;
    }

    public void setAppointmentsList(AppointmentsResponseBean[] mAppointmentsList) {
        this.mAppointmentsList = mAppointmentsList;
    }

    public void setPatientsList(PatientsListBean[] mPatientsList) {
        this.mPatientsList = mPatientsList;
    }

    public void setDoctorsList(DoctorListBean[] mDoctorsList) {
        this.mDoctorsList = mDoctorsList;
    }

    public SearchDoctorRequestBean getSearchDoctorRequestBean() {
        return mSearchDoctorRequestBean;
    }

    public void setSearchDoctorRequestBean(SearchDoctorRequestBean mSearchDoctorRequestBean) {
        this.mSearchDoctorRequestBean = mSearchDoctorRequestBean;
    }

    public DoctorPracticeLocationBean[] getDoctorPracticeLocationBeen() {
        return mDoctorPracticeLocationBeen;
    }

    public void setDoctorPracticeLocationBeen(DoctorPracticeLocationBean[] mDoctorPracticeLocationBeen) {
        this.mDoctorPracticeLocationBeen = mDoctorPracticeLocationBeen;
    }

    public DoctorPracticeLocationBean getDoctorPracticeLocationBean() {
        return mDoctorPracticeLocationBean;
    }

    public void setDoctorPracticeLocationBean(DoctorPracticeLocationBean mDoctorPracticeLocationBean) {
        this.mDoctorPracticeLocationBean = mDoctorPracticeLocationBean;
    }
}
