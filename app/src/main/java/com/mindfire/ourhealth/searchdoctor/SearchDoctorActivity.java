package com.mindfire.ourhealth.searchdoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.constants.MyConstants;

/**
 * This is the base activity for the search doctor module.
 * When this activity is created, it inflates DoctorsByLocalityAndSpecializationFragment inside itself.
 */
public class SearchDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        // Sets up the v7 support toolbar as the v7 support action bar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Inflates the AppointmentsListFragment when this activity is created.
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        MyConstants.FRAGMENT_CONTAINER_ID,
                        new DoctorsByLocalityAndSpecializationFragment()
                ).commit();
    }
}
