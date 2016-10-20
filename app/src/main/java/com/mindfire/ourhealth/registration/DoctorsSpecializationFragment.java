package com.mindfire.ourhealth.registration;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.activities.MyDashboardActivity;
import com.mindfire.ourhealth.adapters.MyArrayAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DoctorProfileBean;
import com.mindfire.ourhealth.beans.MyCheckBoxBean;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.SpecializationsBean;
import com.mindfire.ourhealth.beans.SpecializationsRequestBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;

import java.util.List;
import java.util.Locale;

/**
 * This fragment is used to update the specializations for the doctor.
 */
public class DoctorsSpecializationFragment extends Fragment implements ServerResponseListener, View.OnClickListener {
    private static final String TOOLBAR_TITLE = "Specialization";
   //
    private static final String REQUEST_DOCTOR_PROFILE = "{\"DoctorId\": %d }";
    private final DataManager mDataManager = DataManager.getSoleInstance();
    private NetworkManagerTask mRequestDoctorProfileTask;
   //

    private ListView mListView;

    private SpecializationsBean[] mSpecializationsBeans;
    private List<String> mSelectedSpecializationsList;

    private MyArrayAdapter mMyArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_specialization, container, false);
        ((Toolbar) rootView.findViewById(R.id.doctors_specialization_fragment_tool_bar)).setTitle(TOOLBAR_TITLE);
        mListView = (ListView) rootView.findViewById(R.id.my_list_view);
        new NetworkManagerTask(MyUrls.GET_SPECIALIZATIONS, null, MyConstants.HTTP_GET, this, ServerCallPurpose.SPECIALIZATIONS).execute();
        rootView.findViewById(R.id.specialization_next_button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (mMyArrayAdapter != null) {
            // Extracts specialization id's.
            int size = mSelectedSpecializationsList.size();
            int[] mSpecializationIds = new int[size];
            for (int i = 0; i < size; i++) {
                for (SpecializationsBean specializationsBean : mSpecializationsBeans) {
                    if (specializationsBean.getSpecialization().equals(mSelectedSpecializationsList.get(i))) {
                        mSpecializationIds[i] = specializationsBean.getSpecializationId();
                    }
                }
            }
            updateDoctorSpecializations(mSpecializationIds);
        }
    }

    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        if (iResponse != null) {
            switch (iCallPurpose) {
                case ServerCallPurpose.SPECIALIZATIONS:
                    Log.e("test","load specification res:"+iResponse);
                    mSpecializationsBeans = JsonParser.createSpecializationsBeans(iResponse);
                    populateSpecializationsListView();
                    break;

                case ServerCallPurpose.UPDATE_SPECIALIZATIONS:
                    Log.e("test","specification updated responce:"+iResponse);
                    if(MyConstants.UPLOADED.equals(iResponse)) {                                                                    //
                        String  request = String.format(Locale.US, REQUEST_DOCTOR_PROFILE, mDataManager.getDoctorId());             //
                        mRequestDoctorProfileTask = new NetworkManagerTask(MyUrls.GET_DOCTOR_PROFILE,                               //
                                request, MyConstants.HTTP_POST, this, ServerCallPurpose.GET_DOCTOR_PROFILE);                        //
                        mRequestDoctorProfileTask.execute();
 // OurHealthUtils.inflateFragment(new RegisterPracticeLocationFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
                    }                                                                                                               //
                    else                                                                                                            //
                        Toast.makeText(getActivity(),"error to save specialization",Toast.LENGTH_SHORT).show();                     //
                    break;
                case ServerCallPurpose.GET_DOCTOR_PROFILE:                                                                          //
                    DoctorProfileBean bean = new Gson().fromJson(iResponse, DoctorProfileBean.class);                               //
                    mDataManager.setDoctorProfileBean(bean);                                                                        //
                    startActivity(new Intent(getContext(), MyDashboardActivity.class));                                             //
            }
        }
    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {

    }

    private void populateSpecializationsListView() {
        int length = mSpecializationsBeans.length;
        MyCheckBoxBean[] myCheckBoxBeen = new MyCheckBoxBean[length];
        for (int i = 0; i < length; i++) {
            myCheckBoxBeen[i] = new MyCheckBoxBean(mSpecializationsBeans[i].getSpecialization());
        }
        mMyArrayAdapter = new MyArrayAdapter(getContext(), R.id.my_list_view, myCheckBoxBeen);
        mSelectedSpecializationsList = mMyArrayAdapter.getSelectedItems();
        mListView.setAdapter(mMyArrayAdapter);
    }

    /**
     * Creates request for updating the specializations of a doctor and call NetworkManagerTask
     * to make request to the server.
     *
     * @param iSpecializationIds array of specializations of a doctor.
     */
    private void updateDoctorSpecializations(int[] iSpecializationIds) {
        SpecializationsRequestBean requestBean = new SpecializationsRequestBean();
        DataManager dataManager = DataManager.getSoleInstance();
        requestBean.setUserId(dataManager.getSignInResponse().getUserId());
        requestBean.setDoctorId(dataManager.getDoctorId());
        requestBean.setSpecializationIds(iSpecializationIds);
        String request = new Gson().toJson(requestBean);
        Log.e("test","load specification : request :"+request);
        new NetworkManagerTask(MyUrls.POST_SPECIALIZATIONS, request, MyConstants.HTTP_POST, this, ServerCallPurpose.UPDATE_SPECIALIZATIONS).execute();
    }

}
