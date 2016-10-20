package com.mindfire.ourhealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindfire.ourhealth.R;

/**
 * This adapter loads images into the GridView of DoctorsDashBoardFragment.
 */
public class DashboardImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final int[] mImageIds;
    private final String[] mNames;

    public DashboardImageAdapter(Context mContext, int[] mImageIds, String[] mNames) {
        this.mContext = mContext;
        this.mImageIds = mImageIds;
        this.mNames = mNames;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Create a new ImageView for each item referenced by the Adapter.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_item, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.imageView.setImageResource(mImageIds[position]);
        myViewHolder.textView.setText(mNames[position]);
        return convertView;
    }

    private class MyViewHolder {
        final ImageView imageView;
        final TextView textView;

        public MyViewHolder(View iView) {
            imageView = (ImageView) iView.findViewById(R.id.image_view);
            textView = (TextView) iView.findViewById(R.id.text_view);
        }
    }
}