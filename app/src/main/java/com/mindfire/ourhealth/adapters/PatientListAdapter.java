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
import com.mindfire.ourhealth.beans.PatientsListBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.reports.ReportsListFragment;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.visits.VisitDetailsFragment;

// This adapter is used to populate patient list for the doctor.
public class PatientListAdapter extends ArrayAdapter<PatientsListBean> {

    private final PatientsListBean[] mPatientListBeen;
    private final Context mContext;
    private final String mFlag;

    public PatientListAdapter(Context context, int resource, PatientsListBean[] list, String flag) {
        super(context, resource, list);
        mContext = context;
        mPatientListBeen = list;
        mFlag = flag;
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
        viewHolder.textView.setText(mPatientListBeen[position].getPatientName());

        // When this view is clicked, this inflates visits details fragment with bundle containing the respective
        // patient id.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleListItemClickEvent(mPatientListBeen[position].getPatientId());
            }
        });
        return convertView;
    }

    private void handleListItemClickEvent(int iPatientId) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DATA_MANAGER_PATIENT_ID, iPatientId);
        if (mFlag.equals(MyConstants.VISIT)) {
            bundle.putInt(MyConstants.KEY_VISIT_DETAILS_SCREEN, MyConstants.FLAG_SHOW_VISIT_DETAILS);
            VisitDetailsFragment fragment = new VisitDetailsFragment();
            fragment.setArguments(bundle);
            OurHealthUtils.inflateFragment(fragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
        } else if (mFlag.equals(MyConstants.REPORT)) {
            ReportsListFragment fragment = new ReportsListFragment();
            fragment.setArguments(bundle);
            OurHealthUtils.inflateFragment(fragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
        }
    }

    private class ViewHolder {
        final TextView textView;

        public ViewHolder(View iView) {
            textView = (TextView) iView.findViewById(R.id.name_text_view);
        }
    }
}
