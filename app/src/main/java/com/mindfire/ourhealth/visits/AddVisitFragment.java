package com.mindfire.ourhealth.visits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.AddVisitsRequestBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.utils.ValidatorUtility;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.List;

/**
 * This fragment is used to add visits for an appointment by the doctor.
 */
public class AddVisitFragment extends Fragment implements View.OnClickListener, ServerResponseListener {

    private static final String TOOLBAR_TITLE = "Add Visit";
    private static final String SUCCESS_MESSAGE = "Visit created successfully";

    private EditText mHeightEditText;
    private EditText mWeightEditText;
    private EditText mPulseEditText;
    private EditText mTemperatureEditText;
    private EditText mMedicationEditText;
    private EditText mMedicalTestEditText;
    private EditText mDiagnosisEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_visit, container, false);
        // Initializes views.
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> medicalTests = DataManager.getSoleInstance().getMedicalTests();
        if (medicalTests != null) {
            mMedicalTestEditText.setText(medicalTests.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        DataManager.getSoleInstance().setMedicalTests(null);
    }

    /**
     * When the edit text for adding medical tests is clicked, this will inflate the MedicalTestsFragment.
     * When done button is clicked, this will call makeAddVisitServerRequest() method.
     *
     * @param iView view on which click listener is set.
     */
    @Override
    public void onClick(View iView) {
        switch (iView.getId()) {
            case R.id.medical_test_edit_text:
                OurHealthUtils.inflateFragment(new MedicalTestsFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
                break;

            case R.id.done_button:
                makeAddVisitServerRequest();
                break;
        }
    }

    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {/*Does nothing*/}

    @Override
    public void errorOccurred(final int iResponseCode, final Exception iException, int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_no_internet));
                } else if (iResponseCode == HttpURLConnection.HTTP_CREATED) {
                    OurHealthUtils.makeToastShort(getContext(), SUCCESS_MESSAGE);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    /**
     * Initializes the views used in this fragment.
     *
     * @param iView the root view for this fragment.
     */
    private void initViews(View iView) {
        // Sets the toolbar title for the container activity
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mHeightEditText = (EditText) iView.findViewById(R.id.height_edit_text);
        mWeightEditText = (EditText) iView.findViewById(R.id.weight_edit_text);
        mPulseEditText = (EditText) iView.findViewById(R.id.pulse_edit_text);
        mTemperatureEditText = (EditText) iView.findViewById(R.id.temperature_edit_text);
        mMedicationEditText = (EditText) iView.findViewById(R.id.medication_edit_text);
        mMedicalTestEditText = (EditText) iView.findViewById(R.id.medical_test_edit_text);
        mDiagnosisEditText = (EditText) iView.findViewById(R.id.diagnosis_edit_text);
        iView.findViewById(R.id.done_button).setOnClickListener(this);
        mMedicalTestEditText.setOnClickListener(this);
    }

    /**
     * Sets data in the AddVisitsRequestBean and starts NetworkManagerTask to make request to the
     * server for adding visits.
     */
    private void makeAddVisitServerRequest() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MyConstants.APPOINTMENT_ID)) {
            AddVisitsRequestBean requestBean = fetchDataFromFields(bundle);
            if (ValidatorUtility.isTextFieldEmpty(requestBean.getWeight().length())) {
                mWeightEditText.setError(getString(R.string.empty_field));
            } else if (ValidatorUtility.isTextFieldEmpty(requestBean.getHeight().length())) {
                mHeightEditText.setError(getString(R.string.empty_field));
            } else if (ValidatorUtility.isTextFieldEmpty(requestBean.getPulse().length())) {
                mPulseEditText.setError(getString(R.string.empty_field));
            } else if (ValidatorUtility.isTextFieldEmpty(requestBean.getTemperature().length())) {
                mTemperatureEditText.setError(getString(R.string.empty_field));
            } else if (ValidatorUtility.isTextFieldEmpty(requestBean.getMedication().length())) {
                mMedicationEditText.setError(getString(R.string.empty_field));
            } else if (ValidatorUtility.isTextFieldEmpty(requestBean.getDiagnosis().length())) {
                mDiagnosisEditText.setError(getString(R.string.empty_field));
            } else {
                String request = new Gson().toJson(requestBean);
                new NetworkManagerTask(
                        MyUrls.CREATE_VISIT,
                        request,
                        MyConstants.HTTP_POST,
                        this,
                        ServerCallPurpose.CREATE_VISIT
                ).execute();
            }
        }
    }

    private AddVisitsRequestBean fetchDataFromFields(Bundle iBundle) {
        DataManager dataManager = DataManager.getSoleInstance();
        AddVisitsRequestBean requestBean = new AddVisitsRequestBean();
        requestBean.setUserId(dataManager.getSignInResponse().getUserId());
        requestBean.setAppointmentId(iBundle.getInt(MyConstants.APPOINTMENT_ID));
        requestBean.setWeight(mWeightEditText.getText().toString());
        requestBean.setHeight(mHeightEditText.getText().toString());
        requestBean.setPulse(mPulseEditText.getText().toString());
        requestBean.setTemperature(mTemperatureEditText.getText().toString());
        requestBean.setMedicalTestIds(dataManager.getMedicalTestIds());
        requestBean.setMedication(mMedicationEditText.getText().toString());
        requestBean.setDiagnosis(mDiagnosisEditText.getText().toString());
        return requestBean;
    }

}
