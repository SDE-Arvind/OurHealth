package com.mindfire.ourhealth.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.AddressBean;
import com.mindfire.ourhealth.beans.CountryBean;
import com.mindfire.ourhealth.beans.DoctorsRegistrationBean;
import com.mindfire.ourhealth.beans.GenderBean;
import com.mindfire.ourhealth.beans.DataManager;
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
import java.util.Locale;

/**
 * This fragment is used for the registration of Doctors.
 */
public class DoctorsRegistrationFragment extends Fragment
        implements ServerResponseListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener {

    private static final String TAG = DoctorsRegistrationFragment.class.getSimpleName();

    private static final String COUNTRY_ID_REQUEST = "{\"CountryId\": %d }";

    private DoctorsRegistrationBean mRegistrationBean;
    private AddressBean mAddressBean;
    private final DataManager mDataManager = DataManager.getSoleInstance();

    private TitlesBean[] mTitlesBeans;
    private GenderBean[] mGenderBeans;
    private StatesBean[] mStatesBeans;
    private CountryBean[] mCountryBeans;

    private Spinner mTitleSpinner;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private Spinner mGenderSpinner;
    private EditText mAddressEditText;
    private EditText mLocalityEditText;
    private EditText mCityEditText;
    private Spinner mStateSpinner;
    private Spinner mCountrySpinner;
    private EditText mPinEditText;
    private EditText mMedicalRegistrationNoEditText;
    private EditText mAboutMeEditText;
    private EditText mContactNumberEditText;

    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_registration, container, false);
        initViews(rootView);
        requestLookUpsData();
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent != null && view != null) {
            mCountrySpinner.getItemAtPosition(position);
            int countryId = DataParser.fetchCountryId(mCountryBeans, mCountrySpinner.getSelectedItem().toString());
            String request = String.format(Locale.US, COUNTRY_ID_REQUEST, countryId);
            Log.d(TAG, MyConstants.REQUEST + request);
            new NetworkManagerTask(MyUrls.STATES, request, MyConstants.HTTP_POST, this,
                    ServerCallPurpose.STATE).execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {/*Do nothing.*/}

    @Override
    public void onClick(View iView) {
        if (mTitlesBeans != null && mGenderBeans != null && mStatesBeans != null && mCountryBeans != null) {
            mRegistrationBean = new DoctorsRegistrationBean();
            mAddressBean = new AddressBean();
            fetchRegistrationData();
            validateFields();
        } else {
            OurHealthUtils.showRequestErrorMessage(getContext());
        }
    }

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
        if (iResponse != null) {
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

                case ServerCallPurpose.ADD_DOCTOR_PROFILE:
                    ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
                    handleDoctorProfileUpdateResponse(iResponse);
                    break;
            }
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
    public void errorOccurred(int iResponseCode, final Exception iException, int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showInternetNotConnectedMessage(getContext());
                    showErrorDialogForLookUps(getResources().getString(R.string.msg_no_internet));
                } else if (iException instanceof FileNotFoundException) {
                    showErrorDialogForLookUps(getResources().getString(R.string.msg_server_error));
                }
            }
        });
    }

    /**
     * Initialises the views used in this fragment.
     *
     * @param iView root view for this fragment.
     */
    private void initViews(View iView) {
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), getResources().getString(R.string.title_profile));
        mTitleSpinner = (Spinner) iView.findViewById(R.id.title_spinner);
        mFirstNameEditText = (EditText) iView.findViewById(R.id.first_name_edit_text);
        mLastNameEditText = (EditText) iView.findViewById(R.id.last_name_edit_text);
        mGenderSpinner = (Spinner) iView.findViewById(R.id.gender_spinner);
        mAddressEditText = (EditText) iView.findViewById(R.id.address_edit_text);
        mLocalityEditText = (EditText) iView.findViewById(R.id.locality_edit_text);
        mCityEditText = (EditText) iView.findViewById(R.id.city_edit_text);
        mStateSpinner = (Spinner) iView.findViewById(R.id.state_spinner);
        mCountrySpinner = (Spinner) iView.findViewById(R.id.country_spinner);
        mPinEditText = (EditText) iView.findViewById(R.id.pin_edit_text);
        mMedicalRegistrationNoEditText = (EditText) iView.findViewById(R.id.registration_num_edit_text);
        mAboutMeEditText = (EditText) iView.findViewById(R.id.about_me_edit_text);
        mContactNumberEditText = (EditText) iView.findViewById(R.id.contact_number_edit_text);
        iView.findViewById(R.id.register_button).setOnClickListener(this);
        mCountrySpinner.setOnItemSelectedListener(this);
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
            mProgressDialog.dismiss();
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

    private void fetchRegistrationData() {
        mRegistrationBean.setUserId(mDataManager.getSignInResponse().getUserId());

        mRegistrationBean.setFirstName(mFirstNameEditText.getText().toString());

        mRegistrationBean.setLastName(mLastNameEditText.getText().toString());

        mAddressBean.setAddress(mAddressEditText.getText().toString());
        mAddressBean.setLocality( mLocalityEditText.getText().toString());
        mAddressBean.setCity(mCityEditText.getText().toString());
        mAddressBean.setPin(mPinEditText.getText().toString());
        mAddressBean.setStateId(DataParser.fetchStateId(mStatesBeans, mStateSpinner.getSelectedItem().toString()));
        mAddressBean.setCountryId(DataParser.fetchCountryId(mCountryBeans, mCountrySpinner.getSelectedItem().toString()));
        mRegistrationBean.setAddress(mAddressBean);
        mRegistrationBean.setTitleId(DataParser.fetchTitleId(mTitlesBeans, mTitleSpinner.getSelectedItem().toString()));
        mRegistrationBean.setGenderId(DataParser.fetchGenderId(mGenderBeans, mGenderSpinner.getSelectedItem().toString()));
        mRegistrationBean.setMedicalRegistrationNo(mMedicalRegistrationNoEditText.getText().toString());
        mRegistrationBean.setAboutMe(mAboutMeEditText.getText().toString());
        mRegistrationBean.setContactNumber(mContactNumberEditText.getText().toString());
    }

    private void validateFields() {
        if (ValidatorUtility.isValidName(mRegistrationBean.getFirstName())) {
            mFirstNameEditText.setError(getString(R.string.valid_name));
        } else if (ValidatorUtility.isValidName(mRegistrationBean.getLastName())) {
            mLastNameEditText.setError(getString(R.string.valid_name));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getAddress().length())) {
            mAddressEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getLocality().length())) {
            mLocalityEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isTextFieldEmpty(mAddressBean.getCity().length())) {
            mCityEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isNumber(mAddressBean.getPin())) {
            mPinEditText.setError(getString(R.string.number_only));
        } else if (ValidatorUtility.isTextFieldEmpty(mRegistrationBean.getMedicalRegistrationNo().length())) {
            mMedicalRegistrationNoEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isTextFieldEmpty(mRegistrationBean.getAboutMe().length())) {
            mAboutMeEditText.setError(getString(R.string.empty_field));
        } else if (ValidatorUtility.isPhoneNumber(mRegistrationBean.getContactNumber())) {
            mContactNumberEditText.setError(getString(R.string.phone_number));
        } else {
            makeCreateDoctorProfileRequest();
        }
    }

    private void makeCreateDoctorProfileRequest() {
        String request = new Gson().toJson(mRegistrationBean);
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
        new NetworkManagerTask(
                MyUrls.DOCTOR_PROFILE,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.ADD_DOCTOR_PROFILE
        ).execute();
    }

    private void handleDoctorProfileUpdateResponse(String iResponse) {

        int doctorId = -1;
        try {
            doctorId = new JSONObject(iResponse).getInt(MyConstants.DATA_MANAGER_DOCTOR_ID);
        } catch (JSONException e) {
            e.printStackTrace();
            OurHealthUtils.showServerErrorMessage(getContext());
        }
        mDataManager.setDoctorId(doctorId);
        OurHealthUtils.inflateFragment(new DoctorsQualificationFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
    }

    private void handleLookUpsError() {
        mProgressDialog.dismiss();
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
        getActivity().finish();
    }
}
