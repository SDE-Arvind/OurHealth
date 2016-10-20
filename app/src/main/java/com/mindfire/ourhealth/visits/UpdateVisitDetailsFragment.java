package com.mindfire.ourhealth.visits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.util.List;

/**
 * This fragment is used to update the visit details.
 */
public class UpdateVisitDetailsFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private EditText mHeightEditText;
    private EditText mWeightEditText;
    private EditText mPulseEditText;
    private EditText mTemperatureEditText;
    private EditText mMedicationEditText;
    private EditText mMedicalTestEditText;
    private EditText mDiagnosisEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_visit_details, container, false);
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

    private void initViews(View iView) {
        mHeightEditText = (EditText) iView.findViewById(R.id.height_edit_text);
        mWeightEditText = (EditText) iView.findViewById(R.id.weight_edit_text);
        mPulseEditText = (EditText) iView.findViewById(R.id.pulse_edit_text);
        mTemperatureEditText = (EditText) iView.findViewById(R.id.temperature_edit_text);
        mMedicationEditText = (EditText) iView.findViewById(R.id.medication_edit_text);
        mMedicalTestEditText = (EditText) iView.findViewById(R.id.medical_test_edit_text);
        mMedicalTestEditText.setOnClickListener(this);
        mMedicalTestEditText.setEnabled(false);
        mDiagnosisEditText = (EditText) iView.findViewById(R.id.diagnosis_edit_text);
        iView.findViewById(R.id.save_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View iView) {
        switch (iView.getId()) {
            case R.id.medical_test_edit_text:
                OurHealthUtils.inflateFragment(new MedicalTestsFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
                break;

            case R.id.save_button:
                break;
        }

    }

    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {

    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {

    }
}
