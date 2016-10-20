package com.mindfire.ourhealth.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.DoctorPracticeLocationBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.appointments.AddAppointmentFragment;
import com.mindfire.ourhealth.utils.OurHealthUtils;

/**
 * This is an adapter used for populating the list view of practice locations for a doctor.
 */
public class PracticeLocationsAdapter extends ArrayAdapter<DoctorPracticeLocationBean> {

    private final DoctorPracticeLocationBean[] mLocationBeen;
    private final Context mContext;
    private final int mResource;

    public PracticeLocationsAdapter(Context context, int resource, DoctorPracticeLocationBean[] locationBeen) {
        super(context, resource, locationBeen);
        mContext = context;
        mResource = resource;
        mLocationBeen = locationBeen;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder myViewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(mResource, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.mDayTextView.setText(mLocationBeen[position].getDays());
        myViewHolder.mTimeIn.setText(mLocationBeen[position].getTimeIn());
        myViewHolder.mTimeOut.setText(mLocationBeen[position].getTimeOut());
        myViewHolder.mLocality.setText(mLocationBeen[position].getLocality());

        final DataManager dataManager = DataManager.getSoleInstance();
        if (dataManager.getSignInResponse().getRoleId() == MyConstants.ROLE_ID_DOCTOR) {
            myViewHolder.mBookAppointmentButton.setVisibility(View.GONE);
        }

        myViewHolder.mBookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.setDoctorPracticeLocationBean(mLocationBeen[position]);
                OurHealthUtils.inflateFragment(new AddAppointmentFragment(), (FragmentActivity)mContext, MyConstants.FRAGMENT_CONTAINER_ID);
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        final TextView mDayTextView;
        final TextView mTimeIn;
        final TextView mTimeOut;
        final TextView mLocality;
        final Button mBookAppointmentButton;

        public MyViewHolder(View iView) {
            mDayTextView = (TextView) iView.findViewById(R.id.day_text_view);
            mTimeIn = (TextView) iView.findViewById(R.id.time_in_text_view);
            mTimeOut = (TextView) iView.findViewById(R.id.time_out_text_view);
            mLocality = (TextView) iView.findViewById(R.id.locality_text_view);
            mBookAppointmentButton = (Button) iView.findViewById(R.id.book_appointment_button);
        }
    }
}
