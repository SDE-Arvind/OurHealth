package com.mindfire.ourhealth.signin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.RolesBean;
import com.mindfire.ourhealth.beans.SecurityQuestionsBean;
import com.mindfire.ourhealth.beans.SignUpRequestBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.AlertDialogUtils;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.utils.DataParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.utils.ProgressDialogUtils;
import com.mindfire.ourhealth.utils.SpinnerUtils;
import com.mindfire.ourhealth.utils.ValidatorUtility;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * This fragment is used for the purpose of user Sign Up.
 */
public class SignUpFragment extends Fragment
        implements ServerResponseListener, View.OnClickListener, DialogInterface.OnClickListener {

    private SignUpRequestBean mRequestBean;
    private RolesBean[] mRolesBeans;
    private SecurityQuestionsBean[] mSecurityQuestionsBeans;

    private NetworkManagerTask mRequestRoleIdTask;
    private NetworkManagerTask mRequestSecurityQuestionsTask;
    private NetworkManagerTask mRequestSignUpTask;

    private Spinner mRoleIdSpinner;
    private EditText mUserNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mRePasswordEditText;
    private Spinner mSecurityQuestionSpinner;
    private EditText mSecurityAnswerEditText;

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initViews(rootView);
        requestLookUpsDataFromServer();
        return rootView;
    }

    /**
     * Initializes the views for this fragment and sets up the toolbar.
     *
     * @param iView root view to be inflated.
     */
    private void initViews(View iView) {
        // Sets toolbar title.
        OurHealthUtils.showActionBarAndSetTitle(getActivity(), getString(R.string.title_sign_up));
        mRoleIdSpinner = (Spinner) iView.findViewById(R.id.role_id_spinner);
        mUserNameEditText = (EditText) iView.findViewById(R.id.user_name_edit_text);
        mEmailEditText = ((EditText) iView.findViewById(R.id.email_edit_text));
        mPasswordEditText = ((EditText) iView.findViewById(R.id.password_edit_text));
        mRePasswordEditText = ((EditText) iView.findViewById(R.id.re_enter_password_edit_text));
        mSecurityQuestionSpinner = (Spinner) iView.findViewById(R.id.security_question_spinner);
        mSecurityAnswerEditText = (EditText) iView.findViewById(R.id.security_question_edit_text);
        iView.findViewById(R.id.sign_up_button).setOnClickListener(this);
    }

    private void requestLookUpsDataFromServer() {
        mProgressDialog = new ProgressDialog(getContext());
        ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
        mRequestRoleIdTask  = new NetworkManagerTask(MyUrls.ROLE_ID, null, MyConstants.HTTP_GET, this,
                ServerCallPurpose.ROLE_ID);
        mRequestRoleIdTask.execute();
        mRequestSecurityQuestionsTask = new NetworkManagerTask(MyUrls.SECURITY_QUESTIONS, null,
                MyConstants.HTTP_GET, this, ServerCallPurpose.SECURITY_QUESTIONS);
        mRequestSecurityQuestionsTask.execute();
    }

    /**
     * When sign up button is clicked, this validates the fields entered by the user and starts
     * NetworkManagerTask to make request to the server for user sign up.
     *
     * @param iView sign up button.
     */
    @Override
    public void onClick(View iView) {
        if (mRolesBeans != null && mSecurityQuestionsBeans != null) {
            mRequestBean = new SignUpRequestBean();
            fetchRequestData();
            validateFieldsAndMakeRequest();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (dialog.equals(mProgressDialog) ) {
            handleProgressDialogClickEvent();
        } else if (dialog.equals(mAlertDialog)) {
            getFragmentManager().popBackStack();
        }
    }

    private void handleProgressDialogClickEvent() {
        if (mRequestRoleIdTask != null && mRequestRoleIdTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestRoleIdTask.cancel(true);
        } else if (mRequestSecurityQuestionsTask != null && mRequestSecurityQuestionsTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestSecurityQuestionsTask.cancel(true);
        } else if (mRequestSignUpTask != null && mRequestSignUpTask.getStatus() == AsyncTask.Status.RUNNING) {
            mRequestSignUpTask.cancel(true);
        }
        mProgressDialog.dismiss();
        mAlertDialog = AlertDialogUtils.createAlertDialog(getContext(),
                getString(R.string.msg_unknown_error), this);
    }

    private void fetchRequestData() {
        mRequestBean.setRoleId(DataParser.fetchRoleId(mRolesBeans, mRoleIdSpinner.getSelectedItem().toString()));
        mRequestBean.setUserName(mUserNameEditText.getText().toString());
        mRequestBean.setEmailId(mEmailEditText.getText().toString().trim());
        mRequestBean.setPassword(mPasswordEditText.getText().toString());
        mRequestBean.setSecurityQuesId(DataParser.fetchSecurityQuestionId(mSecurityQuestionsBeans,
                mSecurityQuestionSpinner.getSelectedItem().toString()));
        mRequestBean.setAnswer(mSecurityAnswerEditText.getText().toString());
    }

    private void validateFieldsAndMakeRequest() {
        // Validates user input and starts RequestManager to make sign up request to the server.
        if (ValidatorUtility.isEmailValid(mRequestBean.getEmailId())) {
            mEmailEditText.setError(getString(R.string.invalid_email_error_message));
        } else if (ValidatorUtility.isPasswordValid(mRequestBean.getPassword())) {
            mPasswordEditText.setError(getString(R.string.invalid_password_error_message));
        } else if (!mRequestBean.getPassword().equals(mRePasswordEditText.getText().toString())) {
            mRePasswordEditText.setError(getString(R.string.match_password));
            mPasswordEditText.setError(getString(R.string.match_password));
        } else {
            String request = new Gson().toJson(mRequestBean);
            ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
            mRequestSignUpTask = new NetworkManagerTask(MyUrls.SIGN_UP, request, MyConstants.HTTP_POST, this,
                    ServerCallPurpose.SIGN_UP);
            mRequestSignUpTask.execute();
        }
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
            case ServerCallPurpose.ROLE_ID:
                handleRoleIdResponse(iResponse);
                break;

            case ServerCallPurpose.SECURITY_QUESTIONS:
                handleSecurityQuestionsResponse(iResponse);
                break;

            case ServerCallPurpose.SIGN_UP:
                handleSignUpResponse(iResponse);
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
    public void errorOccurred(int iResponseCode, final Exception iException, int iCallPurpose) {
        final DialogInterface.OnClickListener listener = this;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_no_internet));
                    ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
                } else if (iException instanceof FileNotFoundException) {
                    ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
                    mAlertDialog = AlertDialogUtils.createAlertDialog(getContext(),
                           " This email already exist please try again", listener);
                }
            }
        });
    }

    private void handleRoleIdResponse(String iResponse) {
        mRolesBeans = JsonParser.createRolesBeans(iResponse);
        if (mRolesBeans != null) {
            SpinnerUtils.populateSpinner(getContext(), mRoleIdSpinner, DataParser.getRoles(mRolesBeans));
        } else {
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    private void handleSecurityQuestionsResponse(String iResponse) {
        mSecurityQuestionsBeans = JsonParser.createSecurityQuestionsBeans(iResponse);
        if (mSecurityQuestionsBeans != null) {
            SpinnerUtils.populateSpinner(getContext(), mSecurityQuestionSpinner,
                    DataParser.getSecurityQuestions(mSecurityQuestionsBeans));
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
        }
        ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
    }

    private void handleSignUpResponse(String iResponse) {
        if (iResponse.equals(MyConstants.CREATED)) {
            Log.d("Signup Responce ",iResponse);
            ProgressDialogUtils.dismissProgressDialog(mProgressDialog);
            mAlertDialog = AlertDialogUtils.createAlertDialog(getContext(),
                    getString(R.string.msg_sign_up_success), this);
        }
    }
}
