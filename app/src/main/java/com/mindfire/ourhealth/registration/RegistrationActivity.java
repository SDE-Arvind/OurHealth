package com.mindfire.ourhealth.registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.constants.MyConstants;

/**
 * This is the base activity for the registration module.
 */
public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitvity);
        // Sets up the v7 support toolbar as the v7 support action bar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        inflateFragmentByRoleId();
    }

    /**
     * If the user is logged in as a patient then PatientsRegistrationFragment is inflated.
     * If the user is logged in as a doctor then DoctorsRegistrationFragment is inflated.
     */
    private void inflateFragmentByRoleId() {
        int roleId = DataManager.getSoleInstance().getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new PatientsRegistrationFragment()).commit();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new DoctorsRegistrationFragment()).commit();
        }
    }

}
