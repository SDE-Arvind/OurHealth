package com.mindfire.ourhealth.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.DashboardImageAdapter;
import com.mindfire.ourhealth.appointments.MyAppointmentsActivity;
import com.mindfire.ourhealth.profile.MyProfileActivity;
import com.mindfire.ourhealth.reports.MyReportsActivity;
import com.mindfire.ourhealth.searchdoctor.SearchDoctorActivity;
import com.mindfire.ourhealth.signin.SignInActivity;
import com.mindfire.ourhealth.visits.MyVisitsActivity;

/**
 * This is the base activity for the profile module.
 */
public class MyDashboardActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private final int[] mThumbIds = {
            R.drawable.doctor, R.drawable.appointments,
            R.drawable.visits, R.drawable.report,
    };

    private final String[] mThumbNames = {"FIND DOCTOR", "APPOINTMENTS", "VISITS", "REPORT"};

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dashboard);
        initViews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // Find doctor module.
                startActivity(new Intent(this, SearchDoctorActivity.class));
                break;

            case 1:
                // Appointments module
                startActivity(new Intent(this, MyAppointmentsActivity.class));
                break;

            case 2:
                // Visits module.
                startActivity(new Intent(this, MyVisitsActivity.class));
                break;

            case 3:
                // Reports module.
                startActivity(new Intent(this, MyReportsActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Initializes the views for this activity.
     */
    private void initViews() {
        // Sets up the v7 support toolbar as the v7 support action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.title_dashboard));
        }
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Sets up action bar toggle for nav drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView navigationView = (NavigationView) findViewById(R.id.my_nav_bar);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        // Initializes the grid view
        final GridView dashBoardOperationsGridView = (GridView) findViewById(R.id.user_options_grid_view);
        if (dashBoardOperationsGridView != null) {
            dashBoardOperationsGridView.setAdapter(new DashboardImageAdapter(this, mThumbIds, mThumbNames));
            dashBoardOperationsGridView.setOnItemClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;

            case R.id.nav_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.nav_log_out:
                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();

        return true;
    }

}
