package com.mindfire.ourhealth.constants;

/**
 * This class contains all the URL's used in this app.
 */
public class MyUrls {

    private static final String API = "http://192.168.11.74/OurHealthApi/Api/";

    // URLs for user authentication.
    public static final String LOGIN = API + "Authenticate/Login";
    public static final String SIGN_UP = API + "Authenticate/RegisterUser";
    public static final String PASSWORD_RECOVERY = API + "Authenticate/ForgotPassword";

    public static final String AUTHENTICATE_PATIENT = API + "Patient/GetPatientId";
    public static final String AUTHENTICATE_DOCTOR = API + "Doctor/GetDoctorId";

    public static final String PATIENT_PROFILE = API + "Patient/CreateProfile";
    public static final String DOCTOR_PROFILE = API + "Doctor/CreateProfile";

//    public static final String IMAGE_URL = API + "Image/UploadPhoto";

    // URLs to fetch look ups data for spinners.
    public static final String ROLE_ID = API + "Lookup/GetRoles";
    public static final String SECURITY_QUESTIONS = API + "Lookup/GetSecurityQuestions";
    public static final String TITLE = API + "Lookup/GetTitle";
    public static final String GENDER = API + "Lookup/GetGender";
    public static final String STATES = API + "Lookup/GetStates";
    public static final String COUNTRY = API + "Lookup/GetCountry";
    public static final String INSURANCE_COMPANY = API + "Lookup/GetInsuranceCompany";
    public static final String GET_QUALIFICATIONS = API + "Lookup/GetProfessionalQualifications";
    public static final String GET_SPECIALIZATIONS = API + "Lookup/GetSpecializations";
    public static final String GET_MEDICAL_TESTS = API + "Lookup/GetMedicalTests";
    public static final String GET_TIME = API + "Lookup/GetTime";

    public static final String POST_QUALIFICATIONS = API + "Doctor/DoctorQualifications";
    public static final String POST_SPECIALIZATIONS = API + "Doctor/DoctorSpecialization";

    public static final String GET_DOCTOR_PROFILE = API + "Doctor/GetProfile";
    public static final String GET_PROFILE_PIC = API + "Image/ViewPhoto";
    public static final String DOCTOR_GET_APPOINTMENTS = API + "Doctor/GetAllAppointments";
    public static final String GET_PATIENT_LIST = API + "Doctor/GetPatientList";
    public static final String DOCTOR_GET_VISIT_LIST = API + "Doctor/GetVisitList";
    public static final String CREATE_VISIT = API + "Doctor/CreateVisit";

    public static final String GET_PATIENT_PROFILE = API + "Patient/GetProfile";
    public static final String BOOK_APPOINTMENT = API + "Patient/BookAppointment";
    public static final String GET_PATIENT_APPOINTMENTS = API + "Patient/GetAllAppointments";
    public static final String PATIENT_DELETE_APPOINTMENT = API + "Patient/DeleteAppointment";
    public static final String GET_DOCTOR_BY_LOCALITY_AND_SPECIALIZATION = API + "Patient/GetDoctorByLocationAndSpecialization";
    public static final String GET_DOCTOR_LIST = API + "Patient/GetDoctorList";
    public static final String GET_PATIENT_REPORTS = API + "Patient/GetReportList";
    public static final String GET_PATIENT_REPORT = API + "Patient/GetReport";
    public static final String PATIENT_UPLOAD_REPORT = API + "Patient/UploadReport";
    public static final String DELETE_REPORT = API + "Patient/DeleteReport";

}
