package com.mindfire.ourhealth.reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;

/**
 * This is the base activity for the reports module.
 * When the user is logged in as a patient, this inflates the fragment used for displaying the list of
 * reports.
 * When the user is logged in as a doctor, this inflates the fragment used for displaying the list
 * of patients for the doctor.
 */
public class MyReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        // Sets up the v7 support toolbar as the v7 support action bar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        inflateFragmentByRoleId();
    }

    /**
     * Checks role id and inflates fragments accordingly.
     * If the user is logged in as a patient then ReportsListFragment is inflated.
     * If the user is logged in as a patient then PatientListFragment is inflated.
     */
    private void inflateFragmentByRoleId() {
        int roleId = DataManager.getSoleInstance().getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new ReportsListFragment()).commit();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new PatientListFragment()).commit();
        }
    }
}
