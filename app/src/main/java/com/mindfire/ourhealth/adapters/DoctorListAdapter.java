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
import com.mindfire.ourhealth.beans.DoctorListBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.visits.VisitDetailsFragment;

/**
 * Populates the list view of doctor list fragment with the list of doctors.
 */
public class DoctorListAdapter extends ArrayAdapter<DoctorListBean> {

    private final DoctorListBean[] mDoctorList;

    private final Context mContext;

    public DoctorListAdapter(Context context, DoctorListBean[] list) {
        super(context, R.id.my_list_view, list);
        mContext = context;
        mDoctorList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.patients_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mDoctorList[position].getDoctorName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleListItemClickEvent(mDoctorList[position].getDoctorId());
            }
        });
        return convertView;
    }

    private void handleListItemClickEvent(int iDoctorId) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DATA_MANAGER_DOCTOR_ID, iDoctorId);
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        fragment.setArguments(bundle);
        OurHealthUtils.inflateFragment(fragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
    }

    private class ViewHolder {
        final TextView textView;

        public ViewHolder(View iView) {
            textView = (TextView) iView.findViewById(R.id.name_text_view);
        }
    }
}
