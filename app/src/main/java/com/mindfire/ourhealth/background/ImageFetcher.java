package com.mindfire.ourhealth.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.listeners.ImageResponseListener;
import com.mindfire.ourhealth.utils.NetworkUtility;

/**
 * This AsyncTask is used to fetch image from the server.
 */
public class ImageFetcher extends AsyncTask<Void, Void, byte[]> {

    private static final String PROGRESS_MESSAGE = "Loading, please wait...";

    private final String mUrl;
    private final String mRequest;
    private final ImageResponseListener mListener;
    private final Context mContext;

    private ProgressDialog mProgressDialog;

    public ImageFetcher(String mUrl, String mRequest, ImageResponseListener mListener, Context mContext) {
        this.mUrl = mUrl;
        this.mRequest = mRequest;
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImageFetcher.this.cancel(true);
                mProgressDialog.dismiss();
            }
        });
        mProgressDialog.setMessage(PROGRESS_MESSAGE);
        mProgressDialog.show();
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        return NetworkUtility.fetchImageFromServer(mUrl, mRequest, mListener);
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        mProgressDialog.dismiss();
        if (bytes != null) {
            mListener.imageFromServer(bytes);
        }
    }
}
