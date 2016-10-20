package com.mindfire.ourhealth.registration;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.utils.MultiSelectionDialog;

import java.util.Calendar;

/**
 * This fragment is used for updating a doctor practice location on the server.
 */
public class RegisterPracticeLocationFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    EditText mLocationName,
            mLocationDescription,
            mTimeIn,
            mTimeOut,
            mLocationAddress,
            mLocationLocality,
            mLocationCity,
            mLocationCountry,
            mLocationState,
            mLocationPin,
            mLocationContactPerson;

     EditText mdays;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_practice_location, container, false);
        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        mLocationName = (EditText) rootView.findViewById(R.id.location_name);
        mLocationDescription = (EditText) rootView.findViewById(R.id.location_description);
        mTimeIn = (EditText) rootView.findViewById(R.id.time_in_edit_text);
        mTimeOut = (EditText) rootView.findViewById(R.id.time_out_edit_text);
        mdays=(EditText) rootView.findViewById(R.id.selected_days_editText);
        mLocationAddress = (EditText) rootView.findViewById(R.id.location_address);
        mLocationLocality = (EditText) rootView.findViewById(R.id.location_locality);
        mLocationCity = (EditText) rootView.findViewById(R.id.location_city);
        mLocationCountry = (EditText) rootView.findViewById(R.id.location_country);
        mLocationState = (EditText) rootView.findViewById(R.id.location_state);
        mLocationPin = (EditText) rootView.findViewById(R.id.location_pin);
        mLocationContactPerson = (EditText) rootView.findViewById(R.id.location_contact_person);
        Button mResetButton = (Button) rootView.findViewById(R.id.reset_button);
        Button mSaveButton = (Button) rootView.findViewById(R.id.save_button);

        mTimeIn.setOnClickListener(this);
        mTimeOut.setOnClickListener(this);
        mdays.setOnClickListener(this);
    }

    private  void getTime(final EditText editText)
    {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button:
                Toast.makeText(getActivity(), "Reset Fields", Toast.LENGTH_SHORT).show();
                mLocationAddress.setText("");
                mLocationName.setText("");
                mLocationDescription.setText("");
                mLocationLocality.setText("");
                mLocationCity.setText("");
                mLocationCountry.setText("");
                mLocationState.setText("");
                mLocationPin.setText("");
                mLocationContactPerson.setText("");
                break;
            case R.id.save_button:

                break;
            case R.id.time_in_edit_text:
                      getTime(mTimeIn);
                break;
            case R.id.time_out_edit_text:
                      getTime(mTimeOut);
                break;
            case R.id.selected_days_editText:
                MultiSelectionDialog selectDays=new MultiSelectionDialog();
                selectDays.show(getFragmentManager(),"Days");

                break;
        }
    }

    private void initDays()
    {


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

