package com.mindfire.ourhealth.appointments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.AppointmentsAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.AppointmentsResponseBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.searchdoctor.DoctorsByLocalityAndSpecializationFragment;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * This fragment is used to display the list of appointments for a user.
 * It provides access to the screen AppointmentDetailsFragment when the user clicks of a particular
 * appointment in the list.
 * When the user is logged in as a patient, it also provides the button to access AddAppointmentsFragment.
 */
public class AppointmentListFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Appointments";
    private static final String PATIENT_REQUEST_APPOINTMENTS_FORMAT = "{\"PatientId\": %d }";
    private static final String DOCTOR_REQUEST_APPOINTMENTS_FORMAT = "{\"DoctorId\": \"%s\"}";
    private static final String NO_APPOINTMENTS_MESSAGE = "No appointments found..";

    private final DataManager mDataManager = DataManager.getSoleInstance();

    private RelativeLayout mRelativeLayout;
    private ListView mListView;
    private FloatingActionButton mMyFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointments_list, container, false);
        initViews(rootView);
        // Sets up fragment for patient or doctor by checking the role id.
        setUpFragmentByRoleId();
        return rootView;
    }

    /**
     * Inflates doctor search screen when the add appointments fab is clicked
     *
     * @param iView the view for which the click event is to handled.
     */
    @Override
    public void onClick(View iView) {
        OurHealthUtils.inflateFragment(
                new DoctorsByLocalityAndSpecializationFragment(),
                getActivity(),
                MyConstants.FRAGMENT_CONTAINER_ID
        );
    }

    /**
     * Fetches the response received from the server.
     * If the request is made by the patient it call the method used for handling patient appointments
     * list response otherwise call the respective method for the doctor.
     *
     * @param iResponse response from the server.
     * @param iCallPurpose call purpose of the network call.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        if (iCallPurpose == ServerCallPurpose.GET_PATIENT_APPOINTMENTS) {
            handleResponseForPatient(iResponse);
        } else if (iCallPurpose == ServerCallPurpose.DOCTOR_GET_APPOINTMENTS) {
            handleResponseForDoctor(iResponse);
        }
    }

    /**
     * Fetches the error which occurred in the network call.
     *
     * @param iResponseCode response code received from the server.
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
                    showErrorTextView(NO_APPOINTMENTS_MESSAGE);
                }
            }
        });
    }

    private void initViews(View iView) {
        // Sets up the toolbar title.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.appointments_list_relative_layout);
        mListView = (ListView) iView.findViewById(R.id.my_list_view);
        mMyFab = (FloatingActionButton) iView.findViewById(R.id.my_fab);
    }

    /**
     * Sets up the fragment according to the role id.
     * If the user is logged in as a patient then the fab allows him/her to jump to add appointment
     * screen.
     * If the user is logged in as a doctor then the fab is removed from the UI.
     * This also requests the list of appointments for the user based on the role id.
     */
    private void setUpFragmentByRoleId() {
        int roleId = DataManager.getSoleInstance().getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            setUpFragmentForPatient();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            setUpFragmentForDoctor();
        }
    }

    /**
     * Sets up the fragment for patient.
     */
    private void setUpFragmentForPatient() {
        mMyFab.setOnClickListener(this);
        String request = String.format(Locale.US, PATIENT_REQUEST_APPOINTMENTS_FORMAT, mDataManager.getPatientId());
        new NetworkManagerTask(
                MyUrls.GET_PATIENT_APPOINTMENTS,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.GET_PATIENT_APPOINTMENTS
        ).execute();
    }

    /*
     * Sets up the fragment for doctor.
     */
    private void setUpFragmentForDoctor() {
        mMyFab.setVisibility(View.GONE);
        String request = String.format(DOCTOR_REQUEST_APPOINTMENTS_FORMAT, mDataManager.getDoctorId());
        new NetworkManagerTask(
                MyUrls.DOCTOR_GET_APPOINTMENTS,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.DOCTOR_GET_APPOINTMENTS
        ).execute();
    }

    private void handleResponseForPatient(String iResponse) {
        AppointmentsResponseBean[] been = JsonParser.createPatientsAppointmentsResponseBeans(iResponse);
        mDataManager.setAppointmentsList(been);
        if (been != null) {
            mListView.setAdapter(
                    new AppointmentsAdapter(getContext(), R.layout.appointments_list_item, been, MyConstants.FLAG_APPOINTMENT_DETAILS)
            );
        } else {
            showErrorTextView(getString(R.string.msg_server_error));
        }
    }

    private void handleResponseForDoctor(String iResponse) {
        int flag = MyConstants.FLAG_APPOINTMENT_DETAILS;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MyConstants.KEY_APPOINTMENTS_LIST)) {
            flag = bundle.getInt(MyConstants.KEY_APPOINTMENTS_LIST);
        }
        AppointmentsResponseBean[] been = JsonParser.createPatientsAppointmentsResponseBeans(iResponse);
        mDataManager.setAppointmentsList(been);
        if (been != null) {
            mListView.setAdapter(
                    new AppointmentsAdapter(getContext(), R.layout.appointments_list_item, been, flag)
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
