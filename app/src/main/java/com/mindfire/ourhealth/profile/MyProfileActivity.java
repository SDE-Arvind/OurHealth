package com.mindfire.ourhealth.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;

/**
 * This is the base activity for the profile module.
 */
public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        // Set toolbar title.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        inflateProfileByRoleId();
    }

    /**
     * Inflates the profile fragment by checking the role id.
     */
    private void inflateProfileByRoleId() {
        int roleId = DataManager.getSoleInstance().getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new PatientProfileFragment()).commit();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new DoctorProfileFragment()).commit();
        }
    }
}
