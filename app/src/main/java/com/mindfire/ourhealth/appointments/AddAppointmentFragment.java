package com.mindfire.ourhealth.appointments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.activities.MyDashboardActivity;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.AddAppointmentBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.DoctorPracticeLocationBean;
import com.mindfire.ourhealth.beans.TimeBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.utils.DataParser;
import com.mindfire.ourhealth.utils.SpinnerUtils;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * This fragment provides the functionality for adding the appointment.
 */
public class AddAppointmentFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Book Appointment";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String SUCCESS = "Appointment created successfully..";

    private AddAppointmentBean mAddAppointmentBean;

    private SimpleDateFormat mDateFormatter;

    private TextView mDateTextView;
    private Spinner mTimingSpinner;
    private TextView mSpecializationTextView;
    private TextView mLocationTextView;
    private DatePickerDialog mAppointmentDatePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_appointment, container, false);
        initViews(rootView);
        new NetworkManagerTask(MyUrls.GET_TIME, null, MyConstants.HTTP_GET, this, ServerCallPurpose.GET_TIME).execute();
        return rootView;
    }

    /**
     * Initializes the views used in this fragment.
     *
     * @param iView root view.
     */
    private void initViews(View iView) {
        // Set toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mDateTextView = (TextView) iView.findViewById(R.id.appointment_date_text_view);
        mDateTextView.setOnClickListener(this);
        mDateTextView.setInputType(InputType.TYPE_NULL);
        // Sets the date of appointment.
        setDateOfAppointmentField();
        mTimingSpinner = (Spinner) iView.findViewById(R.id.timing_spinner);
        mSpecializationTextView = (TextView) iView.findViewById(R.id.specialization_text_view);
        mLocationTextView = (TextView) iView.findViewById(R.id.location_text_view);
        iView.findViewById(R.id.book_appointment_button).setOnClickListener(this);
        // Sets up the data previously fetched inside the views.
        setDataInViews();
    }

    /**
     * This method is used to popup date picker dialog.
     * It also fetches date text from the picker and sets the text in mDOBTextView.
     */
    private void setDateOfAppointmentField() {
        mDateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        mAppointmentDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateTextView.setText(mDateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDataInViews() {
        // Fetches data from the DataManager to be set into the text views.
        DataManager dataManager = DataManager.getSoleInstance();

        String specializations = Arrays.toString(dataManager.getSpecializations());
        mSpecializationTextView.setText(specializations);

        DoctorPracticeLocationBean locationBean = dataManager.getDoctorPracticeLocationBean();
        String location = locationBean.getLocationName() + ", "
                + locationBean.getAddress() + ", "
                + locationBean.getCity() + ", "
                + locationBean.getState() + ", "
                + locationBean.getPin() + ", "
                + locationBean.getCountry();
        mLocationTextView.setText(location);
    }

    @Override
    public void onClick(View iView) {
        switch (iView.getId()) {
            case R.id.appointment_date_text_view:
                mAppointmentDatePickerDialog.show();
                break;

            case R.id.book_appointment_button:
                fetchAppointmentRequestData();
                String request = new Gson().toJson(mAddAppointmentBean, AddAppointmentBean.class);
                new NetworkManagerTask(MyUrls.BOOK_APPOINTMENT, request, MyConstants.HTTP_POST, this, ServerCallPurpose.BOOK_APPOINTMENT).execute();
                break;
        }
    }

    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        if (iResponse != null && iCallPurpose == ServerCallPurpose.GET_TIME) {
            TimeBean[] timeBeen = JsonParser.createTimeBeen(iResponse);
            if (timeBeen != null) {
                SpinnerUtils.populateSpinner(getContext(), mTimingSpinner, DataParser.getTimeStrings(timeBeen));
            }
        }
    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {
        if (iCallPurpose == ServerCallPurpose.BOOK_APPOINTMENT && iResponseCode == HttpURLConnection.HTTP_CREATED) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    OurHealthUtils.makeToastShort(getContext(), SUCCESS);
                }
            });
            Intent intent = new Intent(getContext(), MyDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void fetchAppointmentRequestData() {
        mAddAppointmentBean = new AddAppointmentBean();
        DataManager dataManager = DataManager.getSoleInstance();
        mAddAppointmentBean.setUserId(dataManager.getSignInResponse().getUserId());
        mAddAppointmentBean.setPatientId(dataManager.getPatientId());
        mAddAppointmentBean.setDoctorId(dataManager.getDoctorId());
        mAddAppointmentBean.setLocationId(dataManager.getDoctorPracticeLocationBean().getLocationId());
        mAddAppointmentBean.setDateOfAppointment(mDateTextView.getText().toString());
        mAddAppointmentBean.setTiming(mTimingSpinner.getSelectedItem().toString());
    }
}
