package com.mindfire.ourhealth.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.DoctorProfileBean;
import com.mindfire.ourhealth.beans.PatientProfileBean;
import com.mindfire.ourhealth.beans.SignInResponseBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.activities.MyDashboardActivity;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.registration.RegistrationActivity;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.utils.ProgressDialogUtils;
import com.mindfire.ourhealth.utils.ValidatorUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * This is the first fragment inflated into the SignInActivity.
 * It allows the user to Sign In into the app and also jump to SignUpFragment and PasswordRecoveryFragment.
 */
public class SignInFragment extends Fragment
        implements ServerResponseListener, View.OnClickListener, DialogInterface.OnClickListener {

    private static final String SIGN_IN_REQUEST = "{\"userName\": \"%s\", \"password\": \"%s\"}";
    private static final String ID_REQUEST_FORMAT = "{\"UserId\": %d }";
    private static final String REQUEST_PATIENT_PROFILE = "{\"PatientId\": %d }";
    private static final String REQUEST_DOCTOR_PROFILE = "{\"DoctorId\": %d }";
    private static final String PREFS_EMAIL = "Email";
    private static final String PREFS_PASSWORD = "Password";
    private static final String EMPTY_STRING = "";

    private String mEmail;
    private String mPassword;

    private final DataManager mDataManager = DataManager.getSoleInstance();

    private NetworkManagerTask mSignInTask;
    private NetworkManagerTask mRequestPatientIdTask;
    private NetworkManagerTask mRequestDoctorIdTask;
    private NetworkManagerTask mRequestPatientProfileTask;
    private NetworkManagerTask mRequestDoctorProfileTask;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initViews(rootView);
        // Fetches email and password from the prefs file and set them to their respective EditText.
        SharedPreferences sharedPreferences =
                getContext().getSharedPreferences(MyConstants.PREFS, Context.MODE_PRIVATE);
        mEmailEditText.setText(sharedPreferences.getString(PREFS_EMAIL, EMPTY_STRING));
        mPasswordEditText.setText(sharedPreferences.getString(PREFS_PASSWORD, EMPTY_STRING));
        return rootView;
    }

    /**
     * Calls handleSignInButtonClickEvent to verify email, password and calls respective activity
     * on successful login.
     * Inflates SignUpFragment when sign up button is pressed.
     * Inflates PasswordRecoveryFragment when forgot password button is pressed.
     *
     * @param iView gets the respective button.
     */
    @Override
    public void onClick(View iView) {
        switch (iView.getId()) {
            case R.id.sign_in_button:
                handleSignInButtonClickEvent();
                break;

            case R.id.sign_up_button:
                OurHealthUtils.inflateFragment(new SignUpFragment(),
                        getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
                break;

            case R.id.forgot_password_button:
                OurHealthUtils.inflateFragment(new PasswordRecoveryFragment(),
                        getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
        if (mSignInTask != null && mSignInTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSignInTask.cancel(true);
        } else if (mRequestPatientIdTask != null && mRequestPatientIdTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestPatientIdTask.cancel(true);
        } else if (mRequestDoctorIdTask != null && mRequestDoctorIdTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestDoctorIdTask.cancel(true);
        } else if (mRequestPatientProfileTask != null && mRequestPatientProfileTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestPatientProfileTask.cancel(true);
        } else if (mRequestDoctorProfileTask != null && mRequestDoctorProfileTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestDoctorProfileTask.cancel(true);
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
        switch (iCallPurpose) {
            case ServerCallPurpose.SIGN_IN:
                handleSignInResponse(iResponse);
                break;

            case ServerCallPurpose.PATIENT_ID:
                handlePatientIdResponse(iResponse);
                break;

            case ServerCallPurpose.DOCTOR_ID:
                handleDoctorIdResponse(iResponse);
                break;

            case ServerCallPurpose.GET_PATIENT_PROFILE:
                handlePatientProfileResponse(iResponse);
                break;

            case ServerCallPurpose.GET_DOCTOR_PROFILE:
                handleDoctorProfileResponse(iResponse);
                break;
            default:

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
    public void errorOccurred(final int iResponseCode, final Exception iException, final int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_no_internet));
                    ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
                } else if (iException instanceof FileNotFoundException) {
                    ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
                    if (iCallPurpose == ServerCallPurpose.SIGN_IN) {
                        OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_invalid_credentials));
                    } else if (iCallPurpose == ServerCallPurpose.PATIENT_ID || iCallPurpose == ServerCallPurpose.DOCTOR_ID) {
                        launchRegistrationActivity();
                    } else if (iCallPurpose == ServerCallPurpose.GET_PATIENT_PROFILE || iCallPurpose == ServerCallPurpose.GET_DOCTOR_PROFILE) {
                        OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_profile_not_found));
                    }
                }
            }
        });
    }

    /**
     * Initializes the views for this fragment.
     * Also sets up the toolbar for the container activity.
     *
     * @param iView rootView for this fragment.
     */
    private void initViews(View iView) {
        removeActionBar();
        mEmailEditText = (EditText) iView.findViewById(R.id.email_edit_text);
        mPasswordEditText = (EditText) iView.findViewById(R.id.password_edit_text);
        iView.findViewById(R.id.sign_in_button).setOnClickListener(this);
        iView.findViewById(R.id.sign_up_button).setOnClickListener(this);
        iView.findViewById(R.id.forgot_password_button).setOnClickListener(this);
    }

    /**
     * Hides the action bar when this fragment is inflated into the activity.
     */
    private void removeActionBar() {
        // Removes the action bar when this fragment is inflated into its container activity;
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * Validate sign in id and password and on successful validation starts NetworkManagerTask to
     * make sign in request.
     */
    private void handleSignInButtonClickEvent() {
        mEmail = mEmailEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString();
        if (ValidatorUtility.isEmailValid(mEmail)) {
            mEmailEditText.setError(getString(R.string.invalid_email_error_message));
        } else if (ValidatorUtility.isPasswordValid(mPassword)) {
            mPasswordEditText.setError(getString(R.string.invalid_password_error_message));
        } else {
            requestSignIn();
        }
    }

    /**
     * Starts the progress dialog.
     * Starts the NetworkManagerTask to request sign in to the server.
     */
    private void requestSignIn() {
        String request = String.format(SIGN_IN_REQUEST, mEmail, mPassword);
        mProgressDialog = new ProgressDialog(getContext());
        mSignInTask = new NetworkManagerTask(
                MyUrls.LOGIN,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.SIGN_IN
        );
        mSignInTask.execute();
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
    }

    /**
     * Handles the response received from the server for sign in request.
     * Stores the response in the DataManager.
     * Request patient id if the value of role id is one or doctor id if value is two.
     * If role id doesn't exist, this starts the registration activity.
     *
     * @param iResponse response received from the server.
     */
    private void handleSignInResponse(String iResponse) {
        SignInResponseBean signInResponse = new Gson().fromJson(iResponse, SignInResponseBean.class);
        if (signInResponse != null) {
            mDataManager.setSignInResponse(signInResponse);
            createSharedPrefsFile();
            String request = String.format(Locale.US, ID_REQUEST_FORMAT, signInResponse.getUserId());
            requestId(signInResponse.getRoleId(), request);
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Checks the role id to find out whether the user is logged in as a patient or a doctor.
     * If the user is logged in as a patient then starts the NetworkManagerTask to request patient
     * id to the server.
     * If the user is logged in as a doctor then starts the NetworkManagerTask to request doctor
     * id to the server.
     *
     * @param iRoleId role id determining whether the user is patient of doctor.
     * @param iRequest request to be made to the server.
     */
    private void requestId(int iRoleId, String iRequest) {
        switch (iRoleId) {
            case MyConstants.ROLE_ID_PATIENT:
                mRequestPatientIdTask = new NetworkManagerTask(MyUrls.AUTHENTICATE_PATIENT,
                        iRequest, MyConstants.HTTP_POST, this, ServerCallPurpose.PATIENT_ID);
                mRequestPatientIdTask.execute();
                break;

            case MyConstants.ROLE_ID_DOCTOR:
                mRequestDoctorIdTask = new NetworkManagerTask(MyUrls.AUTHENTICATE_DOCTOR,
                        iRequest, MyConstants.HTTP_POST, this, ServerCallPurpose.DOCTOR_ID);
                mRequestDoctorIdTask.execute();
                break;
        }
    }

    /**
     * Handle the response received from the server for patient id request.
     * Sets the patient id in the DataManger.
     *
     * @param iResponse response for patient id request.
     */
    private void handlePatientIdResponse(String iResponse) {
        int patientId = MyConstants.MINUS_ONE;
        try {
            patientId = new JSONObject(iResponse).getInt(MyConstants.DATA_MANAGER_PATIENT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (patientId != MyConstants.MINUS_ONE) {
            mDataManager.setPatientId(patientId);
            requestUserProfile();
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Handle the response received from the server for doctor id request.
     * Sets the doctor id in the DataManger.
     *
     * @param iResponse response for doctor id request.
     */
    private void handleDoctorIdResponse(String iResponse) {
        int doctorId = MyConstants.MINUS_ONE;
        try {
            doctorId = new JSONObject(iResponse).getInt(MyConstants.DATA_MANAGER_DOCTOR_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (doctorId != MyConstants.MINUS_ONE) {
            mDataManager.setDoctorId(doctorId);
            requestUserProfile();
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Starts NetworkManagerTask to request user profile from the server.
     */
    private void requestUserProfile() {
        int roleId = mDataManager.getSignInResponse().getRoleId();
        String request;
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            request = String.format(Locale.US, REQUEST_PATIENT_PROFILE, mDataManager.getPatientId());
            mRequestPatientProfileTask = new NetworkManagerTask(MyUrls.GET_PATIENT_PROFILE,
                    request, MyConstants.HTTP_POST, this, ServerCallPurpose.GET_PATIENT_PROFILE);
            mRequestPatientProfileTask.execute();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            request = String.format(Locale.US, REQUEST_DOCTOR_PROFILE, mDataManager.getDoctorId());
            mRequestDoctorProfileTask = new NetworkManagerTask(MyUrls.GET_DOCTOR_PROFILE,
                    request, MyConstants.HTTP_POST, this, ServerCallPurpose.GET_DOCTOR_PROFILE);
            mRequestDoctorProfileTask.execute();
        }
    }

    /**
     * Handles the response received for patient profile request.
     *
     * @param iResponse response received from the server.
     */
    private void handlePatientProfileResponse(String iResponse) {
        PatientProfileBean bean = new Gson().fromJson(iResponse, PatientProfileBean.class);
        if (bean != null) {
            bean.setDob(OurHealthUtils.formatUTCDate(bean.getDob()));
            mDataManager.setPatientProfileBean(bean);
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            launchDashboardActivity();
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Handles the response received for doctor profile request.
     *
     * @param iResponse response received from the server.
     */
    private void handleDoctorProfileResponse(String iResponse) {
        Log.d("doctor profile responce",iResponse);
        DoctorProfileBean bean = new Gson().fromJson(iResponse, DoctorProfileBean.class);
        if (bean != null) {
            mDataManager.setDoctorProfileBean(bean);

            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            launchDashboardActivity();
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Launches the registration activity.
     */
    private void launchRegistrationActivity() {
        startActivity(new Intent(getContext(), RegistrationActivity.class));
    }

    /**
     * Launches the dashboard activity.
     */
    private void launchDashboardActivity() {
        startActivity(new Intent(getContext(), MyDashboardActivity.class));
    }

    /**
     * Creates a SharedPrefs file when the user id and password are correct.
     */
    private void createSharedPrefsFile() {
        SharedPreferences sharedPreferences
                = getContext().getSharedPreferences(MyConstants.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_EMAIL, mEmail);
        editor.putString(PREFS_PASSWORD, mPassword);
        editor.apply();
    }
}
