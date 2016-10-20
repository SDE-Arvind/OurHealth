package com.mindfire.ourhealth.reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.PatientListAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.PatientsListBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

/**
 * This fragment displays the visits list of patients for a doctor.
 */
public class PatientListFragment extends Fragment implements ServerResponseListener {

    private static final String TOOLBAR_TITLE = "Patients List";
    private static final String REQUEST_PATIENT_LIST = "{\"DoctorId\": \"%s\"}";
    private static final String NO_PATIENTS_MESSAGE = "No patients found..";

    private DataManager mDataManager = DataManager.getSoleInstance();

    private RelativeLayout mRelativeLayout;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        initViews(rootView);
        // Requests the list of patients who have visited this doctor.
        String request = String.format(REQUEST_PATIENT_LIST, mDataManager.getDoctorId());
        new NetworkManagerTask(
                MyUrls.GET_PATIENT_LIST,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.GET_PATIENT_LIST
        ).execute();
        return rootView;
    }

    /**
     * Fetches the response received from the server.
     *
     * @param iResponse response from server.
     * @param iCallPurpose purpose of the network call.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        PatientsListBean[] patientsListBeen = JsonParser.createPatientsListBeans(iResponse);
        mDataManager.setPatientsList(patientsListBeen);
        if (patientsListBeen != null) {
            mListView.setAdapter(new PatientListAdapter(getContext(), R.id.my_list_view, patientsListBeen, MyConstants.REPORT));
        } else {
            showErrorTextView(getString(R.string.msg_server_error));
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
                    OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_no_internet));
                    showErrorTextView(getString(R.string.msg_no_internet));
                } else if (iResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    showErrorTextView(NO_PATIENTS_MESSAGE);
                }
            }
        });
    }

    /**
     * Initializes the views for this fragment.
     *
     * @param iView root view of this fragment.
     */
    private void initViews(View iView) {
        // Sets the toolbar title for the container activity
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.user_list_relative_layout);
        mListView = (ListView) iView.findViewById(R.id.my_list_view);
        iView.findViewById(R.id.add_visits_fab).setVisibility(View.GONE);
    }

    /**
     * Removes the list view.
     * Add a text view and set its text.
     *
     * @param iMessage the text to be set in the text view.
     */
    private void showErrorTextView(String iMessage) {
        mListView.setVisibility(View.GONE);
        mRelativeLayout.addView(OurHealthUtils.getErrorTextView(getContext(), iMessage));
    }
}
