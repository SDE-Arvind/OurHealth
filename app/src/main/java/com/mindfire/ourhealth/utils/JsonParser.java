package com.mindfire.ourhealth.utils;

import com.google.gson.Gson;
import com.mindfire.ourhealth.beans.FilteredDoctorsBean;
import com.mindfire.ourhealth.beans.MedicalTestBean;
import com.mindfire.ourhealth.beans.AppointmentsResponseBean;
import com.mindfire.ourhealth.beans.CountryBean;
import com.mindfire.ourhealth.beans.DoctorListBean;
import com.mindfire.ourhealth.beans.GenderBean;
import com.mindfire.ourhealth.beans.InsuranceCompanyBean;
import com.mindfire.ourhealth.beans.LocationBean;
import com.mindfire.ourhealth.beans.PatientsListBean;
import com.mindfire.ourhealth.beans.QualificationsBean;
import com.mindfire.ourhealth.beans.ReportBean;
import com.mindfire.ourhealth.beans.RolesBean;
import com.mindfire.ourhealth.beans.SecurityQuestionsBean;
import com.mindfire.ourhealth.beans.SpecializationsBean;
import com.mindfire.ourhealth.beans.StatesBean;
import com.mindfire.ourhealth.beans.TimeBean;
import com.mindfire.ourhealth.beans.TitlesBean;
import com.mindfire.ourhealth.beans.VisitsBean;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class is used to parse json for various look ups.
 */
public final class JsonParser {

    private static final Gson mGson = new Gson();

    public static TitlesBean[] createTitlesBeans(String iResponse) {
        TitlesBean[] titlesBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            titlesBeans = mGson.fromJson(jsonArray.toString(), TitlesBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titlesBeans;
    }

    public static RolesBean[] createRolesBeans(String iResponse) {
        RolesBean[] rolesBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            rolesBeans = mGson.fromJson(jsonArray.toString(), RolesBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rolesBeans;
    }

    public static SecurityQuestionsBean[] createSecurityQuestionsBeans(String iResponse) {
        SecurityQuestionsBean[] securityQuestionsBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            securityQuestionsBeans = mGson.fromJson(jsonArray.toString(), SecurityQuestionsBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return securityQuestionsBeans;
    }

    public static GenderBean[] createGenderBeans(String iResponse) {
        GenderBean[] genderBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            genderBeans = mGson.fromJson(jsonArray.toString(), GenderBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genderBeans;
    }

    public static StatesBean[] createStatesBeans(String iResponse) {
        StatesBean[] statesBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            statesBeans = mGson.fromJson(jsonArray.toString(), StatesBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return statesBeans;
    }

    public static CountryBean[] createCountryBeans(String iResponse) {
        CountryBean[] countryBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            countryBeans = mGson.fromJson(jsonArray.toString(), CountryBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryBeans;
    }

    public static InsuranceCompanyBean[] createInsuranceCompanyBeans(String iResponse) {
        InsuranceCompanyBean[] insuranceCompanyBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            insuranceCompanyBeans = mGson.fromJson(jsonArray.toString(), InsuranceCompanyBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return insuranceCompanyBeans;
    }

    public static QualificationsBean[] createQualificationsBeans(String iResponse) {
        QualificationsBean[] qualificationsBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            qualificationsBeans = mGson.fromJson(jsonArray.toString(), QualificationsBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qualificationsBeans;
    }

    public static SpecializationsBean[] createSpecializationsBeans(String iResponse) {
        SpecializationsBean[] specializationsBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            specializationsBeans = mGson.fromJson(jsonArray.toString(), SpecializationsBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return specializationsBeans;
    }

    public static MedicalTestBean[] createMedicalTestBeans(String iResponse) {
        MedicalTestBean[] medicalTestBeen = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            medicalTestBeen = mGson.fromJson(jsonArray.toString(), MedicalTestBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return medicalTestBeen;
    }

    public static TimeBean[] createTimeBeen(String iResponse) {
        TimeBean[] timeBeen = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            timeBeen = mGson.fromJson(jsonArray.toString(), TimeBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeBeen;
    }

    public static VisitsBean[] createVisitsBeans(String iResponse) {
        VisitsBean[] visitsBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            visitsBeans = mGson.fromJson(jsonArray.toString(), VisitsBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return visitsBeans;
    }

    public static AppointmentsResponseBean[] createPatientsAppointmentsResponseBeans(String iResponse) {
        AppointmentsResponseBean[] appointmentsResponseBeen = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            appointmentsResponseBeen = mGson.fromJson(jsonArray.toString(), AppointmentsResponseBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return appointmentsResponseBeen;
    }

    public static FilteredDoctorsBean[] createFilteredDoctorsBeans(String iResponse) {
        FilteredDoctorsBean[] patientsListBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            patientsListBeans = mGson.fromJson(jsonArray.toString(), FilteredDoctorsBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientsListBeans;
    }

    public static PatientsListBean[] createPatientsListBeans(String iResponse) {
        PatientsListBean[] patientsListBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            patientsListBeans = mGson.fromJson(jsonArray.toString(), PatientsListBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientsListBeans;
    }

    public static DoctorListBean[] createDoctorListBeans(String iResponse) {
        DoctorListBean[] doctorListBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            doctorListBeans = mGson.fromJson(jsonArray.toString(), DoctorListBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doctorListBeans;
    }

    //TODO: To be used with location fragment.
    public static LocationBean[] createLocationBeans(String iResponse) {
        LocationBean[] locationBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            locationBeans = mGson.fromJson(jsonArray.toString(), LocationBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationBeans;
    }

    public static ReportBean[] createReportBeanList(String iResponse) {
        ReportBean[] reportBeans = null;
        try {
            JSONArray jsonArray = new JSONArray(iResponse);
            reportBeans = mGson.fromJson(jsonArray.toString(), ReportBean[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reportBeans;
    }

}
