package com.mindfire.ourhealth.visits;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.DoctorListAdapter;
import com.mindfire.ourhealth.adapters.PatientListAdapter;
import com.mindfire.ourhealth.appointments.AppointmentListFragment;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.DoctorListBean;
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
import java.util.Locale;

/**
 * This fragment is used to display the list of visits for the user.
 * When the user is logged in as a doctor then this fragment also provides a fab to access the add
 * visits screen.
 */
public class VisitListFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Patient Visits";
    private static final String REQUEST_PATIENT_LIST = "{\"DoctorId\": \"%s\"}";
    private static final String REQUEST_DOCTOR_LIST = "{\"PatientId\": %d }";
    private static final String NO_PATIENTS_MESSAGE = "No patients found..";
    private static final String NO_DOCTORS_MESSAGE = "No doctors found..";

    private final DataManager mDataManager = DataManager.getSoleInstance();

    private FloatingActionButton mAddVisitsFab;
    private ListView mListView;
    private RelativeLayout mRelativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        initViews(rootView);
        setUpFragmentByRoleId();
        return rootView;
    }

    /**
     * When the add visits fab is clicked, inflates the AppointmentListFragment.
     * Creates a bundle and stores a flag in it that tells the AppointmentListFragment to set up
     * to add visits.
     *
     * @param iView the view on which the click listener is set.
     */
    @Override
    public void onClick(View iView) {
        AppointmentListFragment fragment = new AppointmentListFragment();
        // This bundle is used to inform that on appointment item click open add visits screen.
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.KEY_APPOINTMENTS_LIST, MyConstants.FLAG_ADD_VISITS);
        fragment.setArguments(bundle);
        OurHealthUtils.inflateFragment(fragment, getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
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
        if (iCallPurpose == ServerCallPurpose.GET_DOCTOR_LIST) {
            handleResponseForDoctor(iResponse);
        } else if (iCallPurpose == ServerCallPurpose.GET_PATIENT_LIST) {
            handleResponseForPatient(iResponse);
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
                    if (iCallPurpose == ServerCallPurpose.GET_PATIENT_LIST) {
                        showErrorTextView(NO_PATIENTS_MESSAGE);
                    } else if (iCallPurpose == ServerCallPurpose.GET_DOCTOR_LIST) {
                        showErrorTextView(NO_DOCTORS_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Initialises the views used in this fragment.
     *
     * @param iView root view inflated into the inflater.
     */
    private void initViews(View iView) {
        // Sets the toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.user_list_relative_layout);
        mListView = (ListView) iView.findViewById(R.id.my_list_view);
        mAddVisitsFab = (FloatingActionButton) iView.findViewById(R.id.add_visits_fab);
    }

    /**
     * Sets up the fragment by the role id.
     */
    private void setUpFragmentByRoleId() {
        int roleId = mDataManager.getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            setUpFragmentForPatient();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            setUpFragmentForDoctor();
        }
    }

    /**
     * Sets up the fragment for doctor.
     */
    private void setUpFragmentForPatient() {
        // Disables fab for adding visits.
        mAddVisitsFab.setVisibility(View.GONE);
        // Request for visits.
        String request = String.format(Locale.US, REQUEST_DOCTOR_LIST, mDataManager.getPatientId());
        new NetworkManagerTask(
                MyUrls.GET_DOCTOR_LIST,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.GET_DOCTOR_LIST
        ).execute();
    }

    /**
     * Sets up the fragment for patient.
     */
    private void setUpFragmentForDoctor() {
        // Inflates the DoctorAppointmentsListFragment when this button is clicked.
        mAddVisitsFab.setOnClickListener(this);
        // Requests for visits.
        String request = String.format(REQUEST_PATIENT_LIST, mDataManager.getDoctorId());
        new NetworkManagerTask(
                MyUrls.GET_PATIENT_LIST,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.GET_PATIENT_LIST
        ).execute();
    }

    /**
     * Handles the response received when the user is logged in as a patient.
     * Sets the adapter for the list view.
     *
     * @param iResponse list of doctors for the patient.
     */
    private void handleResponseForPatient(String iResponse) {
        PatientsListBean[] patientsListBeen = JsonParser.createPatientsListBeans(iResponse);
        if (patientsListBeen != null) {
            mDataManager.setPatientsList(patientsListBeen);
            mListView.setAdapter(
                    new PatientListAdapter(getContext(), R.id.my_list_view, patientsListBeen, MyConstants.VISIT)
            );
        } else {
            showErrorTextView(getString(R.string.msg_server_error));
        }
    }

    /**
     * Handles the response received when the user is logged in as a doctor.
     * Sets the adapter for the list view.
     *
     * @param iResponse list of patients for the doctor.
     */
    private void handleResponseForDoctor(String iResponse) {
        DoctorListBean[] doctorListBeen = JsonParser.createDoctorListBeans(iResponse);
        if (doctorListBeen != null) {
            mDataManager.setDoctorsList(doctorListBeen);
            mListView.setAdapter(
                    new DoctorListAdapter(getContext(), doctorListBeen)
            );
        } else {
            showErrorTextView(getString(R.string.msg_server_error));
        }
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
