package com.mindfire.ourhealth.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.MyArrayAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.MyCheckBoxBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.QualificationsBean;
import com.mindfire.ourhealth.beans.QualificationsRequestBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.utils.ProgressDialogUtils;

import java.util.List;

/**
 * This fragment is used for registering the qualifications of a doctor.
 */
public class DoctorsQualificationFragment extends Fragment implements ServerResponseListener, View.OnClickListener, DialogInterface.OnClickListener {

    private List<String> mSelectedQualificationsList;
    private QualificationsBean[] mQualificationsBeans;

    private ListView mListView;
    private ProgressDialog mProgressDialog;

    private MyArrayAdapter mMyArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_qualification, container, false);
        initViews(rootView);
        requestQualifications();
        return rootView;
    }

    /**
     * Initializes the views used in this fragment.
     *
     * @param iView the root view for this fragment.
     */
    private void initViews(View iView) {
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(),
                getResources().getString(R.string.title_profile));
        mListView = (ListView) iView.findViewById(R.id.my_list_view);
        iView.findViewById(R.id.qualification_next_button).setOnClickListener(this);
    }

    /**
     * Calls utility method to start the progress dialog.
     * Starts NetworkManagerTask for request qualifications from the network in the background.
     */
    private void requestQualifications() {
        mProgressDialog = new ProgressDialog(getContext());
       ProgressDialogUtils.startProgressDialog(mProgressDialog, getContext(), this);
        // Requests qualification in the background.
        Log.d("test","request for get doctors qualification req- null ");
        new NetworkManagerTask(
                MyUrls.GET_QUALIFICATIONS,
                null,
                MyConstants.HTTP_GET,
                this,
                ServerCallPurpose.QUALIFICATIONS
        ).execute();
    }

    /**
     * Handles the click event for the calling view.
     *
     * @param iView view on which this click listener is set (add qualification button).
     */
    @Override
    public void onClick(View iView) {
        if (mMyArrayAdapter != null) {
            int size = mSelectedQualificationsList.size();
            int[] qualificationIds = new int[size];
            for (int i = 0; i < size; i++) {
                for (QualificationsBean qualificationsBean : mQualificationsBeans) {
                    if (qualificationsBean.getQualification().equals(mSelectedQualificationsList.get(i))) {
                        qualificationIds[i] = qualificationsBean.getQualificationId();
                    }
                }
            }
            updateDoctorsQualifications(qualificationIds);
        }
    }

    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        switch (iCallPurpose) {
            case ServerCallPurpose.QUALIFICATIONS:
                Log.d("test","qualification loaded res:"+iResponse);
                handleQualificationsResponse(iResponse);
                break;

            case ServerCallPurpose.UPDATE_QUALIFICATIONS:
                Log.d("test" , "qualification updated tes:"+iResponse);
                handleAddQualificationResponse(iResponse);
                break;
        }
    }

    private void handleQualificationsResponse(String iResponse) {
        mQualificationsBeans = JsonParser.createQualificationsBeans(iResponse);
        if (mQualificationsBeans != null) {
            Log.d("test "," list view populated");
            populateQualificationsListView();
        } else {
            handleLookUpsError();
        }
    }

    private void populateQualificationsListView() {
        int length = mQualificationsBeans.length;
        MyCheckBoxBean[] myCheckBoxBeen = new MyCheckBoxBean[length];
        for (int i = 0; i < length; i++) {
            myCheckBoxBeen[i] = new MyCheckBoxBean(mQualificationsBeans[i].getQualification());
        }
        mMyArrayAdapter = new MyArrayAdapter(getContext(), mListView.getId(), myCheckBoxBeen);
        mSelectedQualificationsList = mMyArrayAdapter.getSelectedItems();
        mListView.setAdapter(mMyArrayAdapter);
        mProgressDialog.dismiss();
    }

    private void handleAddQualificationResponse(String iResponse) {
        //TODO: Add the condition to check the response
        Log.e("test","updated qualification responce:"+iResponse);
        if (iResponse.equals(MyConstants.UPLOADED)) {
            OurHealthUtils.inflateFragment(new DoctorsSpecializationFragment(),
                    getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
        } else {
            OurHealthUtils.showServerErrorMessage(getContext());
        }
    }

    /**
     * Make request to the server for updating doctor qualifications.
     *
     * @param iQualificationIds qualification ids.
     */
    private void updateDoctorsQualifications(int[] iQualificationIds) {
        QualificationsRequestBean requestBean = new QualificationsRequestBean();
        DataManager dataManager = DataManager.getSoleInstance();
        requestBean.setUserId(dataManager.getSignInResponse().getUserId());
        requestBean.setDoctorId(dataManager.getDoctorId());
        requestBean.setQualificationIds(iQualificationIds);
        String request = new Gson().toJson(requestBean);
        Log.d(MyConstants.REQUEST, request);
        Log.d("test" , "request for update qualification req: "+request);
        new NetworkManagerTask(MyUrls.POST_QUALIFICATIONS, request, MyConstants.HTTP_POST, this, ServerCallPurpose.UPDATE_QUALIFICATIONS).execute();///
    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {

    }

    private void handleLookUpsError() {
        mProgressDialog.dismiss();
        showErrorDialogForLookUps(getResources().getString(R.string.msg_server_error));
    }

    private void showErrorDialogForLookUps(String iMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(iMessage);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
        getActivity().finish();
    }
}
