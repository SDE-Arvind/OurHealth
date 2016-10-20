package com.mindfire.ourhealth.reports;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.ReportsAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.ReportBean;
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
 * This fragment is used to display the list of report for a patient.
 * When the user is logged in as a patient, it also has a button which allows to access add reports
 * screen.
 */
public class ReportsListFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String REPORTS_LIST_REQUEST = "{\"PatientId\": \"%s\"}";
    private static final String TOOLBAR_TITLE = "Reports";
    private static final String NO_REPORTS_MESSAGE = "No reports found..";

    private RelativeLayout mRelativeLayout;
    private ListView mListView;
    private FloatingActionButton mAddReportFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_reports, container, false);
        // Initializes the views.
        initViews(rootView);
        // Sets up fragment for the user by their role id.
        setUpFragmentByRoleId();
        return rootView;
    }

    /**
     * When add reports fab is click, inflate the AddReportFragment.
     *
     * @param iView view on which the OnClickListener is set.
     */
    @Override
    public void onClick(View iView) {
        OurHealthUtils.inflateFragment(new AddReportFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
    }

    /**
     * Fetches the response received from the server.
     *
     * @param iResponse response from server.
     * @param iCallPurpose purpose of the network call.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        ReportBean[] reportBeans = JsonParser.createReportBeanList(iResponse);
        if (reportBeans != null) {
            mListView.setAdapter(new ReportsAdapter(getContext(), R.layout.report_list_item, reportBeans));
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
    public void errorOccurred(final int iResponseCode, final Exception iException, final int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_no_internet));
                    showErrorTextView(getString(R.string.msg_no_internet));
                } else if (iResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    showErrorTextView(NO_REPORTS_MESSAGE);
                }
            }
        });
    }

    /**
     * Initializes the views used in this fragment.
     *
     * @param iView the root view to be inflated.
     */
    private void initViews(View iView) {
        // Sets up the toolbar title.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.patient_reports_relative_layout);
        mListView = (ListView) iView.findViewById(R.id.my_list_view);
        mAddReportFab = (FloatingActionButton) iView.findViewById(R.id.add_report_fab);
        mAddReportFab.setOnClickListener(this);
    }

    /**
     * If the used is logged in as the doctor, removes the floating action button to add reports.
     */
    private void setUpFragmentByRoleId() {
        DataManager dataManager = DataManager.getSoleInstance();
        int roleId = dataManager.getSignInResponse().getRoleId();
        String request = null;
        if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            mAddReportFab.setVisibility(View.GONE);
            request = String.format(REPORTS_LIST_REQUEST, getArguments().getInt(MyConstants.DATA_MANAGER_PATIENT_ID));
        } else if (roleId == MyConstants.ROLE_ID_PATIENT) {
            request = String.format(REPORTS_LIST_REQUEST, dataManager.getPatientId());
        }
        new NetworkManagerTask(
                MyUrls.GET_PATIENT_REPORTS,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.GET_REPORTS
        ).execute();
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
