package com.mindfire.ourhealth.reports;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.ImageFetcher;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.ReportBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ImageResponseListener;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * This fragment is used to display the report details for the select report item.
 */
public class ReportDetailsFragment extends Fragment implements ImageResponseListener, ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Report Details";
    private static final String REPORT_REQUEST_FORMAT = "{\"DocumentId\": %d }";
    private static final String DELETE_SUCCESS_MESSAGE = "Report deleted successfully.";

    private ReportBean mReportBean;
    private final DataManager mDataManager = DataManager.getSoleInstance();

    private Button mDeleteButton;
    private ImageView mReportImageView;
    private LinearLayout mTitleLinearLayout;
    private LinearLayout mDescriptionLinearLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_report_details, container, false);
        initViews(rootView);
        // This method is used to request server for the report.
        requestReportFromServer();
        return rootView;
    }

    @Override
    public void onClick(View iView) {
        String request = String.format(Locale.US, REPORT_REQUEST_FORMAT, mReportBean.getDocumentId());
        new NetworkManagerTask(
                MyUrls.DELETE_REPORT,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.DELETE_REPORT
        ).execute();
    }

    @Override
    public void imageFromServer(byte[] iImageByteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(iImageByteArray, 0, iImageByteArray.length);
        if (bitmap != null) {
            mReportImageView.setImageBitmap(bitmap);
            if (mDataManager.getSignInResponse().getRoleId() != MyConstants.ROLE_ID_DOCTOR) {
                mDeleteButton.setVisibility(View.VISIBLE);
            }
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Fetches the response code from the server and if any exception occurs.
     * Handles any error that occurs.
     *
     * @param iResponseCode response code from the server.
     * @param iException exception while making request to the server.
     */
    @Override
    public void errorOccurredForImageRequest(final Exception iException, final int iResponseCode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showInternetNotConnectedMessage(getContext());
                    showErrorTextView(getString(R.string.msg_no_internet));
                } else if (iResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    showErrorTextView(getString(R.string.msg_server_error));
                }
            }
        });
    }

    /**
     * Fetches response received from the server.
     *
     * @param iResponse response received from the server.
     * @param iCallPurpose call purpose for the server request.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        if (iResponse.equals(MyConstants.DELETED)) {
            OurHealthUtils.showMyMessage(getContext(), DELETE_SUCCESS_MESSAGE);
            getFragmentManager().popBackStack();
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Fetches the response code from the server and if any exception occurs.
     * Handles any error that occurs.
     *
     * @param iResponseCode response code from the server.
     * @param iException exception while making request to the server.
     */
    @Override
    public void errorOccurred(final int iResponseCode, final Exception iException, int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showInternetNotConnectedMessage(getContext());
                }
            }
        });
    }

    /**
     * Initializes the views used in this fragment.
     *
     * @param iView the root view for this fragment.
     */
    private void initViews(View iView) {
        // Sets up the toolbar title for container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mDeleteButton = (Button) iView.findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(this);
        mDeleteButton.setVisibility(View.GONE);
        mReportImageView = (ImageView) iView.findViewById(R.id.report_image_view);
        mTitleLinearLayout = (LinearLayout) iView.findViewById(R.id.title_linear_layout);
        mDescriptionLinearLayout = (LinearLayout) iView.findViewById(R.id.description_linear_layout);
        mRelativeLayout = (RelativeLayout) iView.findViewById(R.id.report_details_linear_layout);
        mReportBean = mDataManager.getReportBean();
        if (mReportBean != null) {
            ((TextView) iView.findViewById(R.id.title_text_view)).setText(mReportBean.getTitle());
            ((TextView) iView.findViewById(R.id.description_text_view)).setText(mReportBean.getDescription());
        }
    }

    private void requestReportFromServer() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MyConstants.DOCUMENT_ID)) {
            String request = String.format(Locale.US, REPORT_REQUEST_FORMAT, bundle.getInt(MyConstants.DOCUMENT_ID));
            new ImageFetcher(MyUrls.GET_PATIENT_REPORT, request, this, getContext()).execute();
        }
    }

    /**
     * Removes the list view.
     * Add a text view and set its text.
     *
     * @param iMessage the text to be set in the text view.
     */
    private void showErrorTextView(String iMessage) {
        mTitleLinearLayout.setVisibility(View.GONE);
        mDescriptionLinearLayout.setVisibility(View.GONE);
        mReportImageView.setVisibility(View.GONE);
        mRelativeLayout.addView(OurHealthUtils.getErrorTextView(getContext(), iMessage));
    }

}
