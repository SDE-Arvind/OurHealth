package com.mindfire.ourhealth.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.mindfire.ourhealth.R;

/**
 * Display the about us page for Mindfire Solutions.
 */
public class AboutUsActivity extends AppCompatActivity {

    private static final String ABOUT_US_URL = "http://www.mindfiresolutions.com/aboutus.htm";
    private static final String TOOLBAR_TITLE = "About Us";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Set toolbar title.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            myToolbar.setTitle(TOOLBAR_TITLE);
        }

        // Load the web page into the web view
        WebView myWebView = (WebView) findViewById(R.id.about_us_web_view);
        if (myWebView != null) {
            myWebView.loadUrl(ABOUT_US_URL);
        }
    }
}
