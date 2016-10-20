package com.mindfire.ourhealth.visits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.MyArrayAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.MedicalTestBean;
import com.mindfire.ourhealth.beans.MyCheckBoxBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.util.List;

/**
 * This fragment is used to show the list of medical tests and return the selected medical tests to
 * add visits screen.
 */
public class MedicalTestsFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Medical Tests";

    private MedicalTestBean[] mMedicalTestBeen;

    private MyArrayAdapter mAdapter;
    private Button mDoneButton;
    private ListView mMedicalTestListView;
    private RelativeLayout mRelativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medical_tests, container, false);
        initViews(rootView);
        new NetworkManagerTask(MyUrls.GET_MEDICAL_TESTS, null, MyConstants.HTTP_GET, this, ServerCallPurpose.GET_MEDICAL_TESTS).execute();
        return rootView;
    }

    private void initViews(View iView) {
        // Sets the toolbar title for the container activity
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.medical_test_relative_layout);
        mMedicalTestListView = (ListView) iView.findViewById(R.id.my_list_view);
        mDoneButton = (Button) iView.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(this);
    }

    /**
     * When done button is clicked sets Medical test and Medical test ids list in the DataManager.
     *
     * @param iView the view on which the click listener is set.
     */
    @Override
    public void onClick(View iView) {
        if (mAdapter != null) {
            DataManager dataManager = DataManager.getSoleInstance();
            List<String> medicalTests = mAdapter.getSelectedItems();
            dataManager.setMedicalTests(medicalTests);
            dataManager.setMedicalTestIds(getMedicalTestIds(medicalTests));
            getFragmentManager().popBackStack();
        }
    }

    /**
     * Extracts the medical test ids from the MedicalTestBean by comparing the medical tests selected
     * by the user
     *
     * @param iMedicalTests list of medical tests.
     * @return integer array of medical test ids.
     */
    private int[] getMedicalTestIds(List<String> iMedicalTests) {
        int size = iMedicalTests.size();
        int[] medicalTestIds = new int[size];
        for (int i = 0; i < size; i++) {
            for (MedicalTestBean medicalTestBean: mMedicalTestBeen) {
                if (medicalTestBean.getMedicalTest().equals(iMedicalTests.get(i))) {
                    medicalTestIds[i] = medicalTestBean.getMedicalTestId();
                }
            }
        }
        return medicalTestIds;
    }

    /**
     * Fetches the response received from the server.
     *
     * @param iResponse response received from the server.
     * @param iCallPurpose call purpose for the server request.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        mMedicalTestBeen = JsonParser.createMedicalTestBeans(iResponse);
        if (mMedicalTestBeen != null) {
            int length = mMedicalTestBeen.length;
            MyCheckBoxBean[] myCheckBoxBeen = new MyCheckBoxBean[length];
            for (int i = 0; i < length; i++) {
                myCheckBoxBeen[i] = new MyCheckBoxBean(mMedicalTestBeen[i].getMedicalTest());
            }
            mAdapter = new MyArrayAdapter(getContext(), R.id.my_list_view, myCheckBoxBeen);
            mMedicalTestListView.setAdapter(mAdapter);
        } else {
            showErrorTextView(getString(R.string.msg_server_error));
        }
    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {

    }

    /**
     * Removes the list view.
     * Add a text view and set its text.
     *
     * @param iMessage the text to be set in the text view.
     */
    private void showErrorTextView(String iMessage) {
        mMedicalTestListView.setVisibility(View.GONE);
        mDoneButton.setVisibility(View.GONE);
        mRelativeLayout.addView(OurHealthUtils.getErrorTextView(getContext(), iMessage));
    }
}
