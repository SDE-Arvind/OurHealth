package com.mindfire.ourhealth.appointments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.AddressBean;
import com.mindfire.ourhealth.beans.AppointmentsResponseBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

/**
 * This fragment is used to display the details of the appointment.
 * When the user is logged in as a patient it also provides a cancel button to cancel the appointment.
 */
public class AppointmentDetailsFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Appointment Details";
    private static final String DELETE_APPOINTMENT_FORMAT = "{\"AppointmentId\":%s}";

    private int mAppointmentId;
    private TextView mNameTextView;
    private TextView mTimingTextView;
    private TextView mStatusTextView;
    private TextView mDateTextView;
    private TextView mAddressTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        // Initializes the views.
        initViews(rootView);
        // Fetches appointment id from the bundle.
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MyConstants.APPOINTMENT_ID)) {
            mAppointmentId = bundle.getInt(MyConstants.APPOINTMENT_ID);
            showDetailsOnScreen();
        } else {
            OurHealthUtils.showMyMessage(getContext(), getString(R.string.msg_unknown_error));
        }
        return rootView;
    }

    @Override
    public void onClick(View iView) {
        String request = String.format(DELETE_APPOINTMENT_FORMAT, mAppointmentId);
        new NetworkManagerTask(
                MyUrls.PATIENT_DELETE_APPOINTMENT,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.DELETE_APPOINTMENTS
        ).execute();
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
        Log.d("test","response received from appointment delete"+iResponse);
        if (iResponse.equals(MyConstants.DELETED)) {
            OurHealthUtils.makeToastShort(getContext(), iResponse);
            getFragmentManager().popBackStack();
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
                } else if (iResponseCode != HttpURLConnection.HTTP_OK) {
                    OurHealthUtils.showServerErrorMessage(getContext());
                }
            }
        });
    }

    private void initViews(View iView) {
        // Sets up the toolbar title.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mNameTextView = (TextView) iView.findViewById(R.id.name_text_view);
        mTimingTextView = (TextView) iView.findViewById(R.id.timing_text_view);
        mStatusTextView = (TextView) iView.findViewById(R.id.status_text_view);
        mDateTextView = (TextView) iView.findViewById(R.id.date_text_view);
        mAddressTextView = (TextView) iView.findViewById(R.id.address_text_view);
        Button cancelButton = (Button) iView.findViewById(R.id.cancel_appointment_button);
        cancelButton.setOnClickListener(this);
        if (DataManager.getSoleInstance().getSignInResponse().getRoleId() == MyConstants.ROLE_ID_DOCTOR) {
            cancelButton.setVisibility(View.GONE);
        }
    }

    /**
     * Compares the appointment id supplied and search for the respective appointment.
     * Displays the appointment in the UI.
     */
    private void showDetailsOnScreen() {
        AppointmentsResponseBean[] appointmentsResponseBeen = DataManager.getSoleInstance().getAppointmentsList();
        for (AppointmentsResponseBean bean: appointmentsResponseBeen) {
            if (bean.getAppointmentId() == mAppointmentId) {
                mNameTextView.setText(bean.getDoctorName());
                mTimingTextView.setText(bean.getTiming());
                mStatusTextView.setText(String.valueOf(bean.isVisitStatus()));
                mDateTextView.setText(OurHealthUtils.formatUTCDate(bean.getDateOfAppointment()));
                AddressBean addressBean = bean.getLocation().getAddress();
                String location = addressBean.getLocality() + " " + addressBean.getCity();
                mAddressTextView.setText(location);
                break;
            }
        }
    }
}
