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
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.ReportBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.reports.ReportDetailsFragment;
import com.mindfire.ourhealth.utils.OurHealthUtils;

/**
 * This adapter is used to populate the list view with reports.
 */
public class ReportsAdapter extends ArrayAdapter<ReportBean> {

    private final Context mContext;
    private final ReportBean[] mList;

    public ReportsAdapter(Context context, int resource, ReportBean[] list) {
        super(context, resource, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.report_list_item, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        final ReportBean reportBean = mList[position];
        myViewHolder.mReportTitleTextView.setText(reportBean.getTitle());
        myViewHolder.mDescriptionTextView.setText(reportBean.getDescription());
        myViewHolder.mDateTextView.setText(OurHealthUtils.formatUTCDate(reportBean.getDate()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleListItemClickEvent(mList[position]);
            }
        });

        return convertView;
    }

    private void handleListItemClickEvent(ReportBean iReportBean) {
        DataManager.getSoleInstance().setReportBean(iReportBean);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DOCUMENT_ID, iReportBean.getDocumentId());
        ReportDetailsFragment fragment = new ReportDetailsFragment();
        fragment.setArguments(bundle);
        OurHealthUtils.inflateFragment(fragment, (FragmentActivity) mContext, MyConstants.FRAGMENT_CONTAINER_ID);
    }

    /**
     * This class is used to hold the views.
     */
    private class MyViewHolder {
        final TextView mReportTitleTextView;
        final TextView mDescriptionTextView;
        final TextView mDateTextView;

        public MyViewHolder(View iView) {
            mReportTitleTextView = (TextView) iView.findViewById(R.id.report_text_view);
            mDescriptionTextView = (TextView) iView.findViewById(R.id.description_text_view);
            mDateTextView = (TextView) iView.findViewById(R.id.date_text_view);
        }
    }
}
