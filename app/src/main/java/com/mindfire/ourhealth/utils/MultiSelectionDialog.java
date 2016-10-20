package com.mindfire.ourhealth.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import com.mindfire.ourhealth.R;
import java.util.ArrayList;

/**
 * Created by Arvind Kumar on 13-Sep-16.
 */
public class MultiSelectionDialog extends DialogFragment {
    ArrayList<String> list = new ArrayList<String>();
    String items[] = null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        items = getResources().getStringArray(R.array.my_days_selection);

        builder.setTitle("Select days...").setMultiChoiceItems(R.array.my_days_selection, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    list.add(items[which]);
                } else if (list.contains(items[which])) {
                    list.remove(items[which]);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selections = "";
                for (String ms : list) {
                    selections += ms;
                }
                Toast.makeText(getActivity(), selections, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
