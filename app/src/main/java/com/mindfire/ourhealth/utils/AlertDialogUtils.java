package com.mindfire.ourhealth.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mindfire.ourhealth.R;

/**
 * This is the utility for alert dialogs.
 */
public final class AlertDialogUtils {

    public AlertDialogUtils() {}

    public static AlertDialog createAlertDialog(Context iContext, String iMessage, DialogInterface.OnClickListener iListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(iContext);
        builder.setMessage(iMessage);
        builder.setPositiveButton(R.string.ok, iListener);
        builder.setCancelable(false);
        AlertDialog iDialog = builder.create();
        iDialog.show();
        return iDialog;
    }
}
