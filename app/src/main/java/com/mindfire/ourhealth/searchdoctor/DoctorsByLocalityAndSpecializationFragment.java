package com.mindfire.ourhealth.searchdoctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.FilteredDoctorListAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.SearchDoctorRequestBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.constants.ServerCallPurpose;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * This fragment is used to display the list of doctors filtered by locality and specialization.
 */
public class DoctorsByLocalityAndSpecializationFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Doctor List";
    private static final String NO_DOCTORS_MESSAGE = "No doctors found..";

    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctors_by_locality_and_specialization, container, false);
        rootView.findViewById(R.id.filter_button).setOnClickListener(this);
        mListView = (ListView) rootView.findViewById(R.id.doctor_list);
        // Set toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        requestDoctorList();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        DataManager.getSoleInstance().setSearchDoctorRequestBean(null);
    }

    @Override
    public void onClick(View iView) {
        OurHealthUtils.inflateFragment(new FilterDoctorsFragment(), getActivity(), MyConstants.FRAGMENT_CONTAINER_ID);
    }

    /**
     * Fetches the response received from the server.
     * Set the adapter on the list view to display the filtered list of doctors.
     *
     * @param iResponse response from the server.
     * @param iCallPurpose call purpose of the network call.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        mListView.setAdapter(new FilteredDoctorListAdapter(
                getContext(),
                R.layout.filtered_doctor_list_item,
                JsonParser.createFilteredDoctorsBeans(iResponse)
        ));
    }

    /**
     * Fetches the error which occurred in the network call.
     *
     * @param iResponseCode response code received from the server.
     */
    @Override
    public void errorOccurred(int iResponseCode, final Exception iException, int iCallPurpose) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (iException instanceof ConnectException || iException instanceof UnknownHostException) {
                    OurHealthUtils.showInternetNotConnectedMessage(getContext());
                } else if (iException instanceof FileNotFoundException) {
                    OurHealthUtils.showMyMessage(getContext(), NO_DOCTORS_MESSAGE);
                }
            }
        });
    }

    private void requestDoctorList() {
        String locality = getLocalityByRoleId();
        SearchDoctorRequestBean requestBean = DataManager.getSoleInstance().getSearchDoctorRequestBean();
        if (requestBean == null) {
            requestFilteredDoctorList(new SearchDoctorRequestBean(locality, new int[MyConstants.ZERO]));
        } else {
            requestFilteredDoctorList(requestBean);
        }
    }

    private String getLocalityByRoleId() {
        DataManager dataManager = DataManager.getSoleInstance();
        int roleId = dataManager.getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            return dataManager.getPatientProfileBean().getAddressBean().getLocality();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR){
            return dataManager.getDoctorProfileBean().getAddressBean().getLocality();
        }
        return null;
    }

    /**
     * This method starts NetworkManagerTask to request doctor list filtered on the basis of provided location and
     * specialization ids.
     */
    private void requestFilteredDoctorList(SearchDoctorRequestBean requestBean) {
        String request = new Gson().toJson(requestBean);
        new NetworkManagerTask(
                MyUrls.GET_DOCTOR_BY_LOCALITY_AND_SPECIALIZATION,
                request,
                MyConstants.HTTP_POST,
                this,
                ServerCallPurpose.DOCTOR_BY_LOCALITY_AND_SPECIALIZATION).execute();
    }

}
