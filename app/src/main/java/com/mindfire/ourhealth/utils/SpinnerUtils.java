package com.mindfire.ourhealth.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * This is a utility class for parsing data for spinners and populating the spinners.
 */
public final class SpinnerUtils {
    /**
     * Populates spinners for the application.
     */
    public static void populateSpinner(Context iContext, Spinner iSpinner, String[] iStrings) {
        if (iContext != null && iSpinner != null && iStrings != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    iContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    iStrings
            );
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            iSpinner.setAdapter(arrayAdapter);
        }
    }
}
