package com.mindfire.ourhealth.visits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.MedicalTestBean;
import com.mindfire.ourhealth.beans.VisitsBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.utils.SpinnerUtils;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * This fragment is used to show the visit details.
 */
public class VisitDetailsFragment extends Fragment implements ServerResponseListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String REQUEST_VISITS = "{\"DoctorId\": \"%s\", \"PatientId\": \"%s\"}";
    private static final String TOOLBAR_TITLE = "Visit Details";
    private static final String NO_VISITS_MESSAGE = "No visits exist..";

    private final DataManager mDataManager = DataManager.getSoleInstance();
    private VisitsBean[] mVisitsBeans;

    private Spinner mDateSpinner;
    private TextView mHeightTextView;
    private TextView mWeightTextView;
    private TextView mPulseTextView;
    private TextView mTemperatureTextView;
    private TextView mMedicationTextView;
    private TextView mMedicalTestTextView;
    private TextView mDiagnosisTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visit_details, container, false);
        initViews(rootView);
        setUpFragmentByRoleId();
        return rootView;
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
        mVisitsBeans = JsonParser.createVisitsBeans(iResponse);
        if (mVisitsBeans != null) {
            SpinnerUtils.populateSpinner(getContext(), mDateSpinner, fetchDate());
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
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
                    getFragmentManager().popBackStack();
                } else if (iException instanceof FileNotFoundException) {
                    OurHealthUtils.showMyMessage(getContext(), NO_VISITS_MESSAGE);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        for (VisitsBean visitsBean : mVisitsBeans) {
            if (visitsBean.getDate().equals(parent.getSelectedItem())) {
                setTextFields(visitsBean);
                mDataManager.setVisitsBean(visitsBean);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {/*Do nothing*/}

    @Override
    public void onClick(View iView) {
        OurHealthUtils.inflateFragment(new UpdateVisitDetailsFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
    }

    /**
     * Initialises the views used in this fragment.
     *
     * @param iView root view inflated into the inflater.
     */
    private void initViews(View iView) {
        // Sets up the toolbar title for container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mDateSpinner = (Spinner) iView.findViewById(R.id.date_spinner);
        mDateSpinner.setOnItemSelectedListener(this);
        mHeightTextView = (TextView) iView.findViewById(R.id.height_edit_text);
        mWeightTextView = (TextView) iView.findViewById(R.id.weight_edit_text);
        mPulseTextView = (TextView) iView.findViewById(R.id.pulse_edit_text);
        mTemperatureTextView = (TextView) iView.findViewById(R.id.temperature_edit_text);
        mMedicationTextView = (TextView) iView.findViewById(R.id.medication_edit_text);
        mMedicalTestTextView = (TextView) iView.findViewById(R.id.medical_test_edit_text);
        mDiagnosisTextView = (TextView) iView.findViewById(R.id.diagnosis_edit_text);
        iView.findViewById(R.id.edit_button).setOnClickListener(this);
    }

    /**
     * Sets up the fragment by the role id.
     */
    private void setUpFragmentByRoleId() {
        int roleId = mDataManager.getSignInResponse().getRoleId();
        if (getArguments() != null) {
            if (roleId == MyConstants.ROLE_ID_PATIENT) {
                setUpFragmentForPatient();
            } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
                setUpFragmentForDoctor();
            }
        }
    }

    /**
     * Sets up the fragment for patient.
     */
    private void setUpFragmentForPatient() {
        if (getArguments().containsKey(MyConstants.DATA_MANAGER_DOCTOR_ID)) {
            String request = String.format(
                    REQUEST_VISITS,
                    getArguments().getInt(MyConstants.DATA_MANAGER_DOCTOR_ID),
                    mDataManager.getPatientId()
            );
            new NetworkManagerTask(
                    MyUrls.DOCTOR_GET_VISIT_LIST,
                    request,
                    MyConstants.HTTP_POST,
                    this,
                    ServerCallPurpose.DOCTOR_GET_VISIT_LIST
            ).execute();
        }
    }

    /**
     * Sets up the fragment for doctor.
     */
    private void setUpFragmentForDoctor() {
        if (getArguments().containsKey(MyConstants.DATA_MANAGER_PATIENT_ID)) {
            String request = String.format(
                    REQUEST_VISITS,
                    mDataManager.getDoctorId(),
                    getArguments().getInt(MyConstants.DATA_MANAGER_PATIENT_ID)
            );
            new NetworkManagerTask(
                    MyUrls.DOCTOR_GET_VISIT_LIST,
                    request,
                    MyConstants.HTTP_POST,
                    this,
                    ServerCallPurpose.DOCTOR_GET_VISIT_LIST
            ).execute();
        }
    }

    /**
     * Extracts date for the array of VisitBeans.
     *
     * @return array of dates.
     */
    private String[] fetchDate() {
        // Extracts the dates from the list of visit beans and populate the date spinner.
        int size = mVisitsBeans.length;
        String[] dates = new String[size];
        for (int i = 0; i < size; i++) {
            dates[i] = OurHealthUtils.formatUTCDate(mVisitsBeans[i].getDate());
            mVisitsBeans[i].setDate(dates[i]);
        }
        return dates;
    }

    private void setTextFields(VisitsBean iVisitsBean) {
        mHeightTextView.setText(iVisitsBean.getHeight());
        mWeightTextView.setText(iVisitsBean.getWeight());
        mPulseTextView.setText(iVisitsBean.getPulse());
        mTemperatureTextView.setText(iVisitsBean.getTemperature());
        mMedicationTextView.setText(iVisitsBean.getMedication());
        mDiagnosisTextView.setText(iVisitsBean.getDiagnosis());
        setMedicalTestField(iVisitsBean);
    }

    private void setMedicalTestField(VisitsBean iVisitsBean) {
        StringBuilder medicalTestsBuilder = new StringBuilder();
        MedicalTestBean[] medicalTestBeans = iVisitsBean.getMedicalTestBeans();
        if (medicalTestBeans != null) {
            for (MedicalTestBean medicalTestBean : medicalTestBeans) {
                if (medicalTestBean.getMedicalTest() != null) {
                    medicalTestsBuilder.append(medicalTestBean.getMedicalTest());
                    mMedicalTestTextView.setText(medicalTestsBuilder.toString());
                }
            }
        }
    }
}
