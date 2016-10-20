package com.mindfire.ourhealth.signin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.PasswordRequestBean;
import com.mindfire.ourhealth.beans.SecurityQuestionsBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.AlertDialogUtils;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.NetworkUtility;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.utils.DataParser;
import com.mindfire.ourhealth.utils.ProgressDialogUtils;
import com.mindfire.ourhealth.utils.SpinnerUtils;
import com.mindfire.ourhealth.utils.ValidatorUtility;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;

/**
 * This fragment is used to recover password from the server.
 */
public class PasswordRecoveryFragment extends Fragment
        implements ServerResponseListener, View.OnClickListener, DialogInterface.OnClickListener {


    private PasswordRequestBean mPasswordRequestBean;

    private SecurityQuestionsBean[] mSecurityQuestionsBeans;

    private NetworkManagerTask mRequestSecurityQuestions;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mReEnterPasswordEditText;
    private Spinner mSecurityQuestionSpinner;
    private EditText mSecurityAnswerEditText;

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password_recovery, container, false);
        initViews(rootView);
        requestSecurityQuestions();
        return rootView;
    }

    private void initViews(View iView) {
        iView.findViewById(R.id.reset_password_button).setOnClickListener(this);
        OurHealthUtils.showActionBarAndSetTitle(getActivity(), getString(R.string.title_recover_password));
        mEmailEditText = (EditText) iView.findViewById(R.id.email_edit_text);
        mPasswordEditText = (EditText) iView.findViewById(R.id.password_edit_text);
        mReEnterPasswordEditText = (EditText) iView.findViewById(R.id.re_enter_password_edit_text);
        mSecurityQuestionSpinner = (Spinner) iView.findViewById(R.id.security_question_spinner);
        mSecurityAnswerEditText = (EditText) iView.findViewById(R.id.security_question_edit_text);
    }

    private void requestSecurityQuestions() {
        mRequestSecurityQuestions = new NetworkManagerTask(
                MyUrls.SECURITY_QUESTIONS,
                null,
                MyConstants.HTTP_GET,
                this,
                ServerCallPurpose.SECURITY_QUESTIONS
        );
        mRequestSecurityQuestions.execute();
        mProgressDialog = new ProgressDialog(getContext());
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
    }

    /**
     * Validates the fields and call NetworkManagerTask to make request to the server for recovering
     * user password.
     *
     * @param iView view on which the click listener is set.
     */
    @Override
    public void onClick(View iView) {
        if (mSecurityQuestionsBeans != null) {
            fetchRequestData();
            validateFieldsAndMakeRequest();
        }
    }

    private void fetchRequestData() {
        mPasswordRequestBean = new PasswordRequestBean();
        mPasswordRequestBean.setEmailId(mEmailEditText.getText().toString());
        mPasswordRequestBean.setPassword(mPasswordEditText.getText().toString());
        mPasswordRequestBean.setSecurityQuestionId(DataParser.fetchSecurityQuestionId(
                mSecurityQuestionsBeans, mSecurityQuestionSpinner.getSelectedItem().toString()));
        mPasswordRequestBean.setSecurityAnswer(mSecurityAnswerEditText.getText().toString());

    }

    private void validateFieldsAndMakeRequest() {
        // Validates user input and calls RequestManager for network operations.
        if (ValidatorUtility.isEmailValid(mPasswordRequestBean.getEmailId())) {
            mEmailEditText.setError(getString(R.string.invalid_email_error_message));
        } else if (ValidatorUtility.isPasswordValid(mPasswordRequestBean.getPassword())) {
            mPasswordEditText.setError(getString(R.string.invalid_password_error_message));
        } else if (!mPasswordRequestBean.getPassword().equals(mReEnterPasswordEditText.getText().toString())) {
            mPasswordEditText.setError(getString(R.string.match_password));
            mReEnterPasswordEditText.setError(getString(R.string.match_password));
        } else if (mPasswordRequestBean.getSecurityAnswer().equals("")) {
            mSecurityAnswerEditText.setError(getString(R.string.empty_field));
        } else {
            String request = new Gson().toJson(mPasswordRequestBean);
            ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
            new NetworkManagerTask(MyUrls.PASSWORD_RECOVERY, request, MyConstants.HTTP_POST,
                    this, ServerCallPurpose.RECOVER_PASSWORD).execute();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    /**
     * Fetches the response received from the server.
     *
     * @param iResponse response received from the server.
     * @param iCallPurpose call purpose for the server request.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        switch (iCallPurpose) {
            case ServerCallPurpose.SECURITY_QUESTIONS:
                handleSecurityQuestionsResponse(iResponse);
                break;

            case ServerCallPurpose.RECOVER_PASSWORD:
                ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
               startActivity(new Intent(getContext(),SignInActivity.class));
                OurHealthUtils.makeToastShort(getContext(), getString(R.string.msg_recovery_success));
                break;
        }
    }

    private void handleSecurityQuestionsResponse(String iResponse) {
        mSecurityQuestionsBeans = JsonParser.createSecurityQuestionsBeans(iResponse);
        if (mSecurityQuestionsBeans != null) {
            SpinnerUtils.populateSpinner(getContext(), mSecurityQuestionSpinner,
                    DataParser.getSecurityQuestions(mSecurityQuestionsBeans));
        } else {
            mAlertDialog = AlertDialogUtils.createAlertDialog(getContext(), getString(R.string.msg_server_error), this);
        }
        ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
    }

    private void handlePasswordRecoveryResponse() {
        
    }

    /**
     * Fetches the response code from the server and if any exception occurs.
     * Handles any error that occurs.
     *
     * @param iResponseCode response code from the server.
     * @param iException exception while making request to the server.
     */
    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {
        if (iResponseCode != HttpURLConnection.HTTP_OK) {
            if (iResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_invalid_credentials));
            } else if (iResponseCode == HttpURLConnection.HTTP_PAYMENT_REQUIRED) {
                OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_try_again));
            } else if (iException instanceof UnknownHostException) {
                OurHealthUtils.showInternetNotConnectedMessage(getContext());
            } else if (!NetworkUtility.isInternetConnected(getContext())) {
                OurHealthUtils.showInternetNotConnectedMessage(getContext());
            } else {
                OurHealthUtils.showServerErrorMessage(getContext());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // empty fields
        mSecurityAnswerEditText.setText("");
        mPasswordEditText.setText("");
        mReEnterPasswordEditText.setText("");
    }
}
