package com.mindfire.ourhealth.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindfire.ourhealth.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides utilities for this app.
 */
public final class OurHealthUtils {

    private OurHealthUtils(){}

    /**
     * Adds the fragment to the fragment_container.
     * Adds the fragment to the backstack.
     * @param iFragment this fragment is inflated to the layout.
     * @param iFragmentActivity instantiates the FragmentActivity.
     */
    public static void inflateFragment(Fragment iFragment, FragmentActivity iFragmentActivity, int iFragmentContainerId) {
        if (iFragment != null && iFragmentActivity != null) {
            FragmentTransaction transaction = iFragmentActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(iFragmentContainerId, iFragment);
            transaction.addToBackStack(iFragment.getClass().getSimpleName());
            transaction.commit();
        }
    }

    /**
     * Sets the toolbar for the container activity of the fragment which calls this method.
     *
     * @param iActivity container activity.
     * @param iTitle title to be set for the toolbar.
     */
    public static void setToolbarTitleForContainerActivity(Activity iActivity, String iTitle) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) iActivity;
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(iTitle);
        }
    }

    public static void showActionBarAndSetTitle(Activity iActivity, String iTitle) {
        // Sets toolbar title.
        AppCompatActivity appCompatActivity = (AppCompatActivity) iActivity;
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(iTitle);
        }
    }

    /**
     * Show toast for short length of time.
     * @param iString message to be shown in the toast.
     */
    public static void makeToastShort(Context iContext, String iString) {
        if (iContext != null && iString != null) {
            Toast.makeText(iContext, iString, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showMyMessage(Context iContext, String iMessage) {
        OurHealthUtils.makeToastShort(iContext, iMessage);
    }

    /**
     * Shows error message if internet is not connected.
     */
    public static void showInternetNotConnectedMessage(Context iContext) {
        showMyMessage(iContext, iContext.getString(R.string.msg_no_internet));
    }

    /**
     * Shows error message if there is internal server error.
     */
    public static void showServerErrorMessage(Context iContext) {
        showMyMessage(iContext, iContext.getString(R.string.msg_server_error));
    }

    /**
     * Shows error message if there is request is not valid.
     */
    public static void showRequestErrorMessage(Context iContext) {
        showMyMessage(iContext, iContext.getString(R.string.msg_request_invalid));
    }

    public static TextView getErrorTextView(Context iContext, String iMessage) {
        TextView textView = new TextView(iContext);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(iMessage);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    /**
     * By Vyom Bhajpai on 25/05/2016
     */
    public static String formatUTCDate(String iUTCString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder formattedDate = null;
        try {
            Date result1 = dateFormat.parse(iUTCString);
            String[] result = String.valueOf(result1).split(" ");
            formattedDate = new StringBuilder()
                    .append(result[0]).append(" ")
                    .append(result[1]).append(" ")
                    .append(result[2]).append(" ")
                    .append(result[result.length - 1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate != null ? formattedDate.toString() : null;
    }
}
