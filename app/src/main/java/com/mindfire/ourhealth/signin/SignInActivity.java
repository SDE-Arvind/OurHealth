package com.mindfire.ourhealth.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.constants.MyConstants;

/**
 * This is the landing page of the application.
 * When this activity is created, SignInFragment is inflated into this activity.
 */
public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Sets up the v7 support toolbar as the v7 support action bar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Inflates the SignInFragment when this activity is created.
        getSupportFragmentManager().beginTransaction().add(MyConstants.FRAGMENT_CONTAINER_ID, new SignInFragment()).commit();
    }
}