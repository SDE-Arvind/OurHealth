package com.mindfire.ourhealth.utils;

import com.mindfire.ourhealth.beans.CountryBean;
import com.mindfire.ourhealth.beans.GenderBean;
import com.mindfire.ourhealth.beans.InsuranceCompanyBean;
import com.mindfire.ourhealth.beans.LocationBean;
import com.mindfire.ourhealth.beans.QualificationsBean;
import com.mindfire.ourhealth.beans.RolesBean;
import com.mindfire.ourhealth.beans.SecurityQuestionsBean;
import com.mindfire.ourhealth.beans.SpecializationsBean;
import com.mindfire.ourhealth.beans.StatesBean;
import com.mindfire.ourhealth.beans.TimeBean;
import com.mindfire.ourhealth.beans.TitlesBean;

/**
 * Utility for operations made on the spinners.
 */
public final class DataParser {

    public static String[] getRoles(RolesBean[] iRolesBeans) {
        int length = iRolesBeans.length;
        String[] roles = new String[length];
        for (int i = 0; i < length; i++) {
            roles[i] = iRolesBeans[i].getRole();
        }
        return roles;
    }

    public static String[] getTitles(TitlesBean[] iTitlesBeans) {
        int length = iTitlesBeans.length;
        String[] titles = new String[length];
        for (int i = 0; i < length; i++) {
            titles[i] = iTitlesBeans[i].getTitle();
        }
        return titles;
    }

    public static String[] getSecurityQuestions(SecurityQuestionsBean[] iSecurityQuestionsBean) {
        int length = iSecurityQuestionsBean.length;
        String[] securityQuestions = new String[length];
        for (int i = 0; i < length; i++) {
            securityQuestions[i] = iSecurityQuestionsBean[i].getQuestion();
        }
        return securityQuestions;
    }

    public static String[] getGender(GenderBean[] iGenderBeans) {
        int length = iGenderBeans.length;
        String[] genders = new String[length];
        for (int i = 0; i < length; i++) {
            genders[i] = iGenderBeans[i].getGender();
        }
        return genders;
    }

    public static String[] getStates(StatesBean[] iStatesBeans) {
        int length = iStatesBeans.length;
        String[] states = new String[length];
        for (int i = 0; i < length; i++) {
            states[i] = iStatesBeans[i].getStateName();
        }
        return states;
    }

    public static String[] getCountries(CountryBean[] iCountryBeans) {
        int length = iCountryBeans.length;
        String[] countries = new String[length];
        for (int i = 0; i < length; i++) {
            countries[i] = iCountryBeans[i].getCountry();
        }
        return countries;
    }

    public static String[] getInsuranceCompanies(InsuranceCompanyBean[] iInsuranceCompanyBeans) {
        int length = iInsuranceCompanyBeans.length;
        String[] companies = new String[length];
        for (int i = 0; i < length; i++) {
            companies[i] = iInsuranceCompanyBeans[i].getCompanyName();
        }
        return companies;
    }

    public static String[] getQualifications(QualificationsBean[] iQualificationsBeen) {
        int length = iQualificationsBeen.length;
        String[] qualifications = new String[length];
        for (int i = 0; i < length; i++) {
            qualifications[i] = iQualificationsBeen[i].getQualification();
        }
        return qualifications;
    }

    public static String[] getSpecializations(SpecializationsBean[] iSpecializationsBeen) {
        int length = iSpecializationsBeen.length;
        String[] specializations = new String[length];
        for (int i = 0; i < length; i++) {
            specializations[i] = iSpecializationsBeen[i].getSpecialization();
        }
        return specializations;
    }

    //TODO: To be used with location fragment.
    public static String[] getLocations(LocationBean[] iLocationBeans) {
        int length = iLocationBeans.length;
        String[] locations = new String[length];
        for (int i = 0; i < length; i++) {
            locations[i] = iLocationBeans[i].getLocationName();
        }
        return locations;
    }

    public static String[] getTimeStrings(TimeBean[] iTimeBeen) {
        int length = iTimeBeen.length;
        String[] timeStrings = new String[length];
        for (int i = 0; i < length; i++) {
            timeStrings[i] = iTimeBeen[i].getTime();
        }
        return timeStrings;
    }

    /**
     * Fetches id from the role id spinner.
     */
    public static int fetchRoleId(RolesBean[] iRolesBeans, String iSelectedItem) {
        for (RolesBean iRolesBean : iRolesBeans) {
            if (iRolesBean.getRole().equals(iSelectedItem)) {
                return iRolesBean.getRoleId();
            }
        }
        return -1;
    }

    /**
     * Fetches id from the title spinner.
     */
    public static int fetchTitleId(TitlesBean[] iTitlesBean, String iSelectedItem) {
        for (TitlesBean titlesBean: iTitlesBean) {
            if (titlesBean.getTitle().equals(iSelectedItem)) {
                return titlesBean.getTitleId();
            }
        }
        return -1;
    }

    /**
     * Fetches id from the security questions spinner.
     */
    public static int fetchSecurityQuestionId(SecurityQuestionsBean[] iSecurityQuestionsBeans, String iSelectedItem) {
        for (SecurityQuestionsBean securityQuestionsBean: iSecurityQuestionsBeans) {
            if (securityQuestionsBean.getQuestion().equals(iSelectedItem)) {
                return securityQuestionsBean.getSecurityQuestionId();
            }
        }
        return -1;
    }

    /**
     * Fetches id from the gender spinner.
     */
    public static int fetchGenderId(GenderBean[] iGenderBeans, String iSelectedItem) {
        for (GenderBean genderBean: iGenderBeans) {
            if (genderBean.getGender().equals(iSelectedItem)) {
                return genderBean.getGenderId();
            }
        }
        return -1;
    }

    /**
     * Fetches id from the state spinner.
     */
    public static int fetchStateId(StatesBean[] iStatesBeans, String iSelectedItem) {
        for (StatesBean statesBean: iStatesBeans) {
            if (statesBean.getStateName().equals(iSelectedItem)) {
                return statesBean.getStateId();
            }
        }
        return -1;
    }

    public static int fetchCountryId(CountryBean[] iCountryBeans, String iSelectedItem) {
        for (CountryBean iCountryBean : iCountryBeans) {
            if (iCountryBean.getCountry().equals(iSelectedItem)) {
                return iCountryBean.getCountryId();
            }
        }
        return -1;
    }

    /**
     * Fetches id from the insurance company spinner.
     */
    public static int fetchInsuranceCompanyId(InsuranceCompanyBean[] iInsuranceCompanyBeans, String iSelectedItem) {
        for (InsuranceCompanyBean insuranceCompanyBean: iInsuranceCompanyBeans) {
            if (insuranceCompanyBean.getCompanyName().equals(iSelectedItem)) {
                return insuranceCompanyBean.getCompanyId();
            }
        }
        return -1;
    }

}
