package com.mindfire.ourhealth.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.NetworkUtility;

/**
 * This asynctask is used to upload image through multipart in the background.
 */
public class UploadReportTask extends AsyncTask<Void, Void, String> {

    private final ServerResponseListener mListener;
    private final String mImageFilePath;
    private final String mReportTitle;
    private final String mReportDescription;
    private final Context mContext;

    private ProgressDialog mProgressDialog;

    public UploadReportTask(ServerResponseListener mListener, String mImageFilePath, String mReportTitle, String mReportDescription, Context mContext) {
        this.mListener = mListener;
        this.mImageFilePath = mImageFilePath;
        this.mReportTitle = mReportTitle;
        this.mReportDescription = mReportDescription;
        this.mContext = mContext;
    }

    /**
     * Sets up progress dialog and shows it.
     * When cancel button is pressed on the progress dialog, it stops this async task.
     */
    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadReportTask.this.cancel(true);
                mProgressDialog.dismiss();
            }
        });
        mProgressDialog.setMessage(mContext.getString(R.string.msg_progress));
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return NetworkUtility.multipartHttpRequest(mImageFilePath, mReportTitle, mReportDescription);
    }

    @Override
    protected void onPostExecute(String iResponse) {
        mProgressDialog.dismiss();
        if (iResponse != null) {
            mListener.responseFromServer(iResponse, ServerCallPurpose.UPLOAD_REPORT);
        }
    }
}