package com.mindfire.ourhealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.MyCheckBoxBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter is used to populate the list views which contains an item of single check box only.
 */
public class MyArrayAdapter extends ArrayAdapter<MyCheckBoxBean> {
    private final MyCheckBoxBean[] mList;
    private final Context mContext;
    private final List<String> mSelectedItems = new ArrayList<>();

    public MyArrayAdapter(Context context, int resource, MyCheckBoxBean[] mList) {
        super(context, resource, mList);
        mContext = context;
        this.mList = mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_check_box, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    boolean checked = buttonView.isChecked();
                    mList[getPosition].setSelected(checked);
                    if (checked) {
                        mSelectedItems.add(mList[getPosition].getName());
                    } else {
                        mSelectedItems.remove(mList[getPosition].getName());
                    }
                }
            });
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.checkBox.setTag(position);
        myViewHolder.checkBox.setText(mList[position].getName());
        myViewHolder.checkBox.setChecked(mList[position].ismSelected());
        return convertView;
    }

    public List<String> getSelectedItems() {
        return mSelectedItems;
    }

    private class MyViewHolder {
        final CheckBox checkBox;

        public MyViewHolder(View iView) {
            checkBox = (CheckBox) iView.findViewById(R.id.my_checkbox);
        }
    }
}
