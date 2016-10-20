package com.mindfire.ourhealth.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindfire.ourhealth.beans.MessageBean;
import com.mindfire.ourhealth.beans.PatientProfileBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.NetworkUtility;

/**
 * This is the base class for all the requests made to the server.
 * It is used for doing network operations in the background.
 */
public class NetworkManagerTask extends AsyncTask<Void, Void, String> {

    private final String mUrl;
    private final String mRequest;
    private final String mRequestMethod;
    private final ServerResponseListener mListener;
    private final int mServerCallPurpose;

    public NetworkManagerTask(String mUrl, String mRequest, String mRequestMethod,
                              ServerResponseListener mListener, int mServerCallPurpose) {
        this.mUrl = mUrl;
        this.mRequest = mRequest;
        this.mRequestMethod = mRequestMethod;
        this.mListener = mListener;
        this.mServerCallPurpose = mServerCallPurpose;
    }

    @Override
    protected String doInBackground(Void... params) {
        return NetworkUtility.makeHttpRequest(mUrl, mRequestMethod, mRequest, mListener, mServerCallPurpose);
    }
    @Override
    protected void onPostExecute(String iResponse) {
        Log.d(MyConstants.TAG_TEST,"post execute called");
        if (iResponse != null) {
            mListener.responseFromServer(iResponse, mServerCallPurpose);
        }

    }
}
