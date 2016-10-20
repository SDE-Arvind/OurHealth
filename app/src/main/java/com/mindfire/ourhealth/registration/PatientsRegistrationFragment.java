package com.mindfire.ourhealth.registration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mindfire.ourhealth.activities.MyDashboardActivity;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.AddressBean;
import com.mindfire.ourhealth.beans.CountryBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.GenderBean;
import com.mindfire.ourhealth.beans.InsuranceCompanyBean;
import com.mindfire.ourhealth.beans.PatientProfileBean;
import com.mindfire.ourhealth.beans.PatientRegistrationBean;
import com.mindfire.ourhealth.beans.StatesBean;
import com.mindfire.ourhealth.beans.TitlesBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.utils.DataParser;
import com.mindfire.ourhealth.utils.ProgressDialogUtils;
import com.mindfire.ourhealth.utils.SpinnerUtils;
import com.mindfire.ourhealth.utils.ValidatorUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This fragment is used for the registration of Patients.
 */
public class PatientsRegistrationFragment extends Fragment
        implements ServerResponseListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener {

    private static final String REQUEST_PATIENT_PROFILE = "{\"PatientId\": %d }";
    NetworkManagerTask mRequestPatientProfileTask;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String COUNTRY_ID_REQUEST = "{\"CountryId\": %d }";

    private TitlesBean[] mTitlesBeans;
    private GenderBean[] mGenderBeans;
    private StatesBean[] mStatesBeans;
    private CountryBean[] mCountryBeans;
    private InsuranceCompanyBean[] mInsuranceCompanyBeans;

    private PatientRegistrationBean mPatientRegistrationBean;
    private AddressBean mAddressBean;
    private final DataManager mDataManager = DataManager.getSoleInstance();

    private SimpleDateFormat mDateFormatter;

    private Spinner mTitleSpinner;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private Spinner mGenderSpinner;
    private TextView mDOBTextView;
    private EditText mAddressEditText;
    private EditText mLocalityEditText;
    private EditText mCityEditText;
    private Spinner mStateSpinner;
    private Spinner mCountrySpinner;
    private EditText mPinEditText;
    private EditText mContactEditText;
    private EditText mEmergencyContactEditText;
    private EditText mEmergencyNumberEditText;
    private Spinner mInsuranceCompanySpinner;
    private EditText mInsuranceNumberEditText;

    private DatePickerDialog mDobDatePickerDialog;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patients_registration, container, false);
        // Initializes the views.
        initViews(rootView);
        // Sets the date of birth.
        setDateOfBirthField();
        // Calls AsyncTask to make network request in the background.
        requestLookUpsData();
        return rootView;
    }

    @Override
    public void onClick(View iView) {
        if (iView != null) {
            switch (iView.getId()) {
                case R.id.dob_edit_text:
                    mDobDatePickerDialog.show();
                    break;

                case R.id.patients_register_button:
                    fetchRegistrationData();
                    break;
            }
        }
    }

    /**
     * This method fetches data for the states corresponding to the country selected in the country spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent != null && view != null) {
            mCountrySpinner.getItemAtPosition(position);
            String request = String.format(
                    Locale.US,
                    COUNTRY_ID_REQUEST,
                    DataParser.fetchCountryId(mCountryBeans, mCountrySpinner.getSelectedItem().toString())
            );
            new NetworkManagerTask(MyUrls.STATES, request,
                    MyConstants.HTTP_POST, this, ServerCallPurpose.STATE).execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {/* Do nothing. */}

    /**
     * Fetches response received from the server.
     * This method also calls various methods based on the call purpose of the server request to
     * handle the server response.
     *
     * @param iResponse response received from the server.
     * @param iCallPurpose call purpose for the server request.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        switch (iCallPurpose) {
            case ServerCallPurpose.TITLE:
                handleTitlesResponse(iResponse);
                break;

            case ServerCallPurpose.GENDER:
                handleGenderResponse(iResponse);
                break;

            case ServerCallPurpose.STATE:
                handleStatesResponse(iResponse);
                break;

            case ServerCallPurpose.COUNTRY:
                handleCountryResponse(iResponse);
                break;

            case ServerCallPurpose.INSURANCE_COMPANY:
                handleInsuranceCompanyResponse(iResponse);
                break;

            case ServerCallPurpose.ADD_PATIENT_PROFILE:
                handleAddPatientProfileResponse(iResponse);
                break;
            case ServerCallPurpose.GET_PATIENT_PROFILE:
                PatientProfileBean bean = new Gson().fromJson(iResponse, PatientProfileBean.class);
                    bean.setDob(OurHealthUtils.formatUTCDate(bean.getDob()));
                    mDataManager.setPatientProfileBean(bean);
                    startActivity(new Intent(getContext(), MyDashboardActivity.class));
                break;
        }
    }

    /**
     * Fetches the response code from the server and if any exception occurs.
     * Handles any error that occurs.
     *
     * @param iResponseCode response code from the server.
     * @param iException exception while making request to the server.
     */
    @Override
    public void errorOccurred(final int iResponseCode, final Exception iException, int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showInternetNotConnectedMessage(getContext());
                    showErrorDialogForLookUps(getString(R.string.msg_no_internet));
                } else if (iException instanceof FileNotFoundException) {
                    showErrorDialogForLookUps(getString(R.string.msg_server_error));
                }
            }
        });
    }

    private void initViews(View iView) {
        //Sets the toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(),
                getResources().getString(R.string.title_profile));
        mTitleSpinner = (Spinner) iView.findViewById(R.id.title_spinner);
        mFirstNameEditText = (EditText) iView.findViewById(R.id.first_name_edit_text);
        mLastNameEditText = (EditText) iView.findViewById(R.id.last_name_edit_text);
        mGenderSpinner = (Spinner) iView.findViewById(R.id.gender_spinner);
        mDOBTextView = (TextView) iView.findViewById(R.id.dob_edit_text);
        mAddressEditText = (EditText) iView.findViewById(R.id.address_edit_text);
        mLocalityEditText = (EditText) iView.findViewById(R.id.locality_edit_text);
        mCityEditText = (EditText) iView.findViewById(R.id.city_edit_text);
        mStateSpinner = (Spinner) iView.findViewById(R.id.state_spinner);
        mCountrySpinner = (Spinner) iView.findViewById(R.id.country_spinner);
        mCountrySpinner.setOnItemSelectedListener(this);
        mPinEditText = (EditText) iView.findViewById(R.id.pin_edit_text);
        mContactEditText = (EditText) iView.findViewById(R.id.contact_number_edit_text);
        mEmergencyContactEditText = (EditText) iView.findViewById(R.id.emergency_contact_edit_text);
        mEmergencyNumberEditText = (EditText) iView.findViewById(R.id.emergency_number_edit_text);
        mInsuranceCompanySpinner = (Spinner) iView.findViewById(R.id.insurance_company_spinner);
        mInsuranceNumberEditText = (EditText) iView.findViewById(R.id.insurance_number);
        iView.findViewById(R.id.patients_register_button).setOnClickListener(this);
    }

    /**
     * This method is used to popup date picker dialog.
     * It also fetches date text from the picker and sets the text in mDOBTextView.
     */
    private void setDateOfBirthField() {
        mDOBTextView.setOnClickListener(this);
        mDOBTextView.setInputType(InputType.TYPE_NULL);
        mDateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        mDobDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDOBTextView.setText(mDateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void requestLookUpsData() {
        mProgressDialog = new ProgressDialog(getContext());
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
        new NetworkManagerTask(MyUrls.TITLE, null, MyConstants.HTTP_GET,
                this, ServerCallPurpose.TITLE).execute();
        new NetworkManagerTask(MyUrls.GENDER, null, MyConstants.HTTP_GET,
                this, ServerCallPurpose.GENDER).execute();
        new NetworkManagerTask(MyUrls.COUNTRY, null, MyConstants.HTTP_GET,
                this, ServerCallPurpose.COUNTRY).execute();
        new NetworkManagerTask(MyUrls.INSURANCE_COMPANY, null, MyConstants.HTTP_GET,
                this, ServerCallPurpose.INSURANCE_COMPANY).execute();
    }

    private void handleTitlesResponse(String iResponse) {
        mTitlesBeans = JsonParser.createTitlesBeans(iResponse);
        if (mTitlesBeans != null) {
            SpinnerUtils.populateSpinner(getContext(),
                    mTitleSpinner, DataParser.getTitles(mTitlesBeans));
        } else {
            handleLookUpsError();
        }
    }

    private void handleGenderResponse(String iResponse) {
        mGenderBeans = JsonParser.createGenderBeans(iResponse);
        if (mGenderBeans != null) {
            SpinnerUtils.populateSpinner(getContext(),
                    mGenderSpinner, DataParser.getGender(mGenderBeans));
        } else {
            handleLookUpsError();
        }
    }

    private void handleStatesResponse(String iResponse) {
        mStatesBeans = JsonParser.createStatesBeans(iResponse);
        if (mStatesBeans != null) {
            SpinnerUtils.populateSpinner(getContext(),
                    mStateSpinner, DataParser.getStates(mStatesBeans));
        } else {
            handleLookUpsError();
        }
    }

    private void handleCountryResponse(String iResponse) {
        mCountryBeans = JsonParser.createCountryBeans(iResponse);
        if (mCountryBeans != null) {
            SpinnerUtils.populateSpinner(getContext(),
                    mCountrySpinner, DataParser.getCountries(mCountryBeans));
        } else {
            mProgressDialog.dismiss();
            showErrorDialogForLookUps(getResources().getString(R.string.msg_server_error));
        }
    }

    private void handleInsuranceCompanyResponse(String iResponse) {
        mInsuranceCompanyBeans = JsonParser.createInsuranceCompanyBeans(iResponse);
        if (mInsuranceCompanyBeans != null) {
            mProgressDialog.dismiss();
            SpinnerUtils.populateSpinner(getContext(),
                    mInsuranceCompanySpinner, DataParser.getInsuranceCompanies(mInsuranceCompanyBeans));
        } else {
            handleLookUpsError();
        }
    }

    /**
     * This method is used to fetch data from the fields of this fragment.
     * This method also starts PatientRegistrationTask when all the fields are valid.
     */
    private void fetchRegistrationData() {
        if (mTitlesBeans != null && mGenderBeans != null && mStatesBeans != null
                && mCountryBeans != null && mInsuranceCompanyBeans != null) {
            mPatientRegistrationBean = new PatientRegistrationBean();
            mAddressBean = new AddressBean();
            mPatientRegistrationBean.setUserId(mDataManager.getSignInResponse().getUserId());
            mPatientRegistrationBean.setFirstName(mFirstNameEditText.getText().toString());
            mPatientRegistrationBean.setLastName(mLastNameEditText.getText().toString());
            mPatientRegistrationBean.setDOB(mDOBTextView.getText().toString());
            mAddressBean.setAddress(mAddressEditText.getText().toString());
            mAddressBean.setLocality(mLocalityEditText.getText().toString());
            mAddressBean.setCity(mCityEditText.getText().toString());
            mAddressBean.setPin(mPinEditText.getText().toString());
            mAddressBean.setStateId(getStateId());
            mAddressBean.setCountryId(getCountryId());
            mPatientRegistrationBean.setAddress(mAddressBean);
            mPatientRegistrationBean.setContact(mContactEditText.getText().toString());
            mPatientRegistrationBean.setEmergencyContact(mEmergencyContactEditText.getText().toString());
            mPatientRegistrationBean.setEmergencyNumber(mEmergencyNumberEditText.getText().toString());
            mPatientRegistrationBean.setInsuranceNumber(mInsuranceNumberEditText.getText().toString());
            mPatientRegistrationBean.setTitleId(getTitleId());
            mPatientRegistrationBean.setGenderId(getGenderId());
            mPatientRegistrationBean.setInsuranceCompanyId(getInsuranceCompanyId());
            validateFields();
        } else {
            OurHealthUtils.showRequestErrorMessage(getContext());
        }
    }

    private int getCountryId() {
        return DataParser.fetchCountryId(mCountryBeans, mCountrySpinner.getSelectedItem().toString());
    }

    private int getStateId() {
        return DataParser.fetchStateId(mStatesBeans, mStateSpinner.getSelectedItem().toString());
    }

    private int getTitleId() {
        return DataParser.fetchTitleId(mTitlesBeans, mTitleSpinner.getSelectedItem().toString());
    }

    private int getGenderId() {
        return DataParser.fetchGenderId(mGenderBeans, mGenderSpinner.getSelectedItem().toString());
    }

    private int getInsuranceCompanyId() {
        return DataParser.fetchInsuranceCompanyId(mInsuranceCompanyBeans,
                mInsuranceCompanySpinner.getSelectedItem().toString());
    }

    /**
     * Validates fields and shows error if any of the field is not valid.
     */
    private void validateFields() {
        if (ValidatorUtility.isValidName(mPatientRegistrationBean.getFirstName())) {
            mFirstNameEditText.setError(getString(R.string.valid_name));
        } else if (ValidatorUtility.isValidName(mPatientRegistrationBean.getLastName())) {
            mLastNameEditText.setError(getString(R.string.valid_name));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getAddress().length())) {
            mAddressEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getLocality().length())) {
            mLocalityEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getCity().length())) {
            mCityEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isNumber(mAddressBean.getPin())) {
            mPinEditText.setError(getString(R.string.number_only));
        } else if (ValidatorUtility.isPhoneNumber(mPatientRegistrationBean.getContact())) {
            mContactEditText.setError(getString(R.string.phone_number));
        } else if (ValidatorUtility.isValidName(mPatientRegistrationBean.getEmergencyContact())) {
            mEmergencyContactEditText.setError(getString(R.string.valid_name));
        } else if (ValidatorUtility.isPhoneNumber(mPatientRegistrationBean.getEmergencyNumber())) {
            mEmergencyNumberEditText.setError(getString(R.string.phone_number));
        } else if (ValidatorUtility.isTextFieldEmpty(mPatientRegistrationBean.getInsuranceNumber().length())) {
            mInsuranceNumberEditText.setError(getString(R.string.empty_field));
        } else {
            makeCreatePatientProfileRequest();
        }
    }

    private void makeCreatePatientProfileRequest() {
        String request = new Gson().toJson(mPatientRegistrationBean);
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
        new NetworkManagerTask(
                MyUrls.PATIENT_PROFILE,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.ADD_PATIENT_PROFILE
        ).execute();
    }

    private void handleAddPatientProfileResponse(String iResponse) {
        int patientId = -1;
        try {
            patientId = new JSONObject(iResponse).getInt(MyConstants.DATA_MANAGER_PATIENT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            OurHealthUtils.showServerErrorMessage(getContext());
        }
        mDataManager.setPatientId(patientId);
        OurHealthUtils.makeToastShort(getContext(), getResources().getString(R.string.msg_add_profile_success));

        String request = String.format(Locale.US, REQUEST_PATIENT_PROFILE, mDataManager.getPatientId());
        mRequestPatientProfileTask = new NetworkManagerTask(MyUrls.GET_PATIENT_PROFILE,
                request, MyConstants.HTTP_POST, this, ServerCallPurpose.GET_PATIENT_PROFILE);
        mRequestPatientProfileTask.execute();

        startActivity(new Intent(getContext(), MyDashboardActivity.class));
    }

    private void handleLookUpsError() {
        mProgressDialog.dismiss();
       // showErrorDialogForLookUps("duplicate");
        showErrorDialogForLookUps(getResources().getString(R.string.msg_server_error));
    }

    private void showErrorDialogForLookUps(String iMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(iMessage);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
        getActivity().finish();
    }
}
