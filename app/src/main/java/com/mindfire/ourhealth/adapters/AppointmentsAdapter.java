package com.mindfire.ourhealth.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.appointments.AppointmentDetailsFragment;
import com.mindfire.ourhealth.beans.AppointmentsResponseBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.visits.AddVisitFragment;

/**
 * This is an adapter used for displaying the list of appointments for both doctor or patient whoever
 * is signed in.
 * When a list item is clicked it for the flag received and decides whether to call the screen for
 * displaying the appointment details or screen for adding visits.
 */
public class AppointmentsAdapter extends ArrayAdapter<AppointmentsResponseBean> {

    private final Context mContext;
    private final AppointmentsResponseBean[] mList;
    private final int mFlag;

    public AppointmentsAdapter(Context context, int resource, AppointmentsResponseBean[] list, int mFlag) {
        super(context, resource, list);
        mContext = context;
        this.mList = list;
        this.mFlag = mFlag;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.appointments_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Checks the role id and decides whether to display the list for patient or doctor.
        final int roleId = DataManager.getSoleInstance().getSignInResponse().getRoleId();
        String name = "";
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            name = mList[position].getDoctorName();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            name = mList[position].getPatientName();
        }
        viewHolder.mNameTextView.setText(name);
        String date = OurHealthUtils.formatUTCDate(mList[position].getDateOfAppointment());
        viewHolder.mDateTextView.setText(date);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store appointment id in bundle to be passed with the fragment.
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.APPOINTMENT_ID, mList[position].getAppointmentId());
                handleListItemClickEvent(bundle);
            }
        });
        return convertView;
    }

    private void handleListItemClickEvent(Bundle iBundle) {
        /**
         * If flag is set for adding visit it inflates the fragment for adding visits,
         * otherwise inflates the fragment for displaying the appointment details.
         */
        if (mFlag == MyConstants.FLAG_ADD_VISITS) {
            AddVisitFragment addVisitFragment = new AddVisitFragment();
            addVisitFragment.setArguments(iBundle);
            OurHealthUtils.inflateFragment(addVisitFragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
        } else if (mFlag == MyConstants.FLAG_APPOINTMENT_DETAILS){
            AppointmentDetailsFragment detailsFragment = new AppointmentDetailsFragment();
            detailsFragment.setArguments(iBundle);
            OurHealthUtils.inflateFragment(detailsFragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
        }
    }

    /**
     * Class to hold the references to the views of the list view for which this adapter is set.
     */
    private class ViewHolder {
        private final TextView mNameTextView;
        private final TextView mDateTextView;

        public ViewHolder(View iView) {
            mNameTextView = (TextView) iView.findViewById(R.id.name_text_view);
            mDateTextView = (TextView) iView.findViewById(R.id.date_text_view_2);
        }
    }
}
