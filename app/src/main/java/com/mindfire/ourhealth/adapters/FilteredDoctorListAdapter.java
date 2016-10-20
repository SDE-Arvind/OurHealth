package com.mindfire.ourhealth.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.FilteredDoctorsBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.searchdoctor.DoctorPracticeLocationFragment;
import com.mindfire.ourhealth.utils.OurHealthUtils;

/**
 * This is an adapter to display the list of doctors.
 * When a list item is clicked this inflates the fragment containing the practice locations for
 * the doctor.
 */
public class FilteredDoctorListAdapter extends ArrayAdapter<FilteredDoctorsBean> {

    private final Context mContext;
    private final FilteredDoctorsBean[] mList;

    public FilteredDoctorListAdapter(Context context, int resource, FilteredDoctorsBean[] list) {
        super(context, resource, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.filtered_doctor_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameTextView.setText(mList[position].getName());

        DataManager dataManager = DataManager.getSoleInstance();
        dataManager.setDoctorPracticeLocationBeen(mList[position].getPracticeLocations());
        dataManager.setSpecializations(mList[position].getSpecializations());
        dataManager.setDoctorId(mList[position].getDoctorId());

        if (dataManager.getSignInResponse().getRoleId() == MyConstants.ROLE_ID_PATIENT) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View iView) {
                    OurHealthUtils.inflateFragment(new DoctorPracticeLocationFragment(), (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
                }
            });
        }

        return convertView;
    }

    /**
     * This class is used to hold the view used in the getView method.
     */
    private class ViewHolder {
        final TextView nameTextView;

        public ViewHolder(View iView) {
            nameTextView = (TextView) iView.findViewById(R.id.name_text_view);
        }
    }
}
