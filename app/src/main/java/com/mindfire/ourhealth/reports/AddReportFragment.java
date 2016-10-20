package com.mindfire.ourhealth.reports;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.UploadReportTask;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * This fragment is used to add reports by the patient.
 */
public class AddReportFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Add Report";
    private static final String UPLOAD_SUCCESSFULL = "Report uploaded successfully";
    private static final int REQUEST_LOAD_IMAGE = 1;
    private static final String PICK_IMAGE_MESSAGE = "You haven't picked an image.";
    private static final String UNKNOWN_ERROR = "Something went wrong..";

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private ImageView mReportImageView;
    private String mImageFilePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_add_report, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * When the user clicks the choose file button, it will open any image app which provides the
     * required services.
     * When the user click the upload button, this starts NetworkManagerTask to make request to the
     * server for uploading the report.
     *
     * @param iView view which is clicked.
     */
    @Override
    public void onClick(View iView) {
        switch (iView.getId()) {
            case R.id.choose_file_button:
                // Launches the gallery to fetch image.
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_LOAD_IMAGE);
                break;

            case R.id.upload_button:
                new UploadReportTask(
                        this,
                        mImageFilePath,
                        mTitleEditText.getText().toString(),
                        mDescriptionEditText.getText().toString(),
                        getContext()
                ).execute();
                break;
        }
    }

    /**
     * Fetches the image returned by the app chosen by the user to pick image.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == MyReportsActivity.RESULT_OK && requestCode == REQUEST_LOAD_IMAGE && data != null) {
                fetchImageFromIntent(data);
            } else {
                OurHealthUtils.showMyMessage(getContext(), PICK_IMAGE_MESSAGE);
            }
        } catch (Exception e) {
            OurHealthUtils.showMyMessage(getContext(), UNKNOWN_ERROR);
        }

    }

    /**
     * Fetches the response received from the server.
     *
     * @param iResponse response from server.
     * @param iCallPurpose purpose of the network call.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        if (iResponse.equals(MyConstants.UPLOADED)) {
            OurHealthUtils.makeToastShort(getContext(), UPLOAD_SUCCESSFULL);
            getFragmentManager().popBackStack();
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

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

    private void initViews(View iView) {
        // Sets up the toolbar title.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mTitleEditText = (EditText) iView.findViewById(R.id.title_edit_text);
        mDescriptionEditText = (EditText) iView.findViewById(R.id.description_edit_text);
        mReportImageView = (ImageView) iView.findViewById(R.id.report_image_view);
        iView.findViewById(R.id.choose_file_button).setOnClickListener(this);
        iView.findViewById(R.id.upload_button).setOnClickListener(this);
    }

    /**
     * Fetches image from the intent.
     */
    private void fetchImageFromIntent(Intent iData) {
        // Gets the image from data.
        Uri selectedImage = iData.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Gets the cursor.
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mImageFilePath = cursor.getString(columnIndex);
            cursor.close();
            mReportImageView.setBackground(null);
            mReportImageView.setImageBitmap(BitmapFactory.decodeFile(mImageFilePath));
        }
    }

}
