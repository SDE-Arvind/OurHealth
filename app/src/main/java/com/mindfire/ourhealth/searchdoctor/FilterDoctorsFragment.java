package com.mindfire.ourhealth.searchdoctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.MyArrayAdapter;
import com.mindfire.ourhealth.background.NetworkManagerTask;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.DoctorProfileBean;
import com.mindfire.ourhealth.beans.MyCheckBoxBean;
import com.mindfire.ourhealth.beans.PatientProfileBean;
import com.mindfire.ourhealth.beans.SearchDoctorRequestBean;
import com.mindfire.ourhealth.beans.SpecializationsBean;
import com.mindfire.ourhealth.constants.MyConstants;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.constants.ServerCallPurpose;
import com.mindfire.ourhealth.listeners.ServerResponseListener;
import com.mindfire.ourhealth.utils.JsonParser;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.util.List;

/**
 * This fragment is used to filter the list of doctors.
 */
public class FilterDoctorsFragment extends Fragment implements ServerResponseListener, View.OnClickListener {

    private static final String TOOLBAR_TITLE = "Filter Doctors";
    private static final String NO_SPECIALIZATIONS_MESSAGE = "No specializations exist..";

    private SpecializationsBean[] mSpecializationsBeans;
    private MyArrayAdapter myArrayAdapter;

    private EditText mLocalityEditText;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_doctors, container, false);
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        mListView = (ListView) rootView.findViewById(R.id.my_list_view);
        mLocalityEditText = (EditText) rootView.findViewById(R.id.locality_edit_text);
        mLocalityEditText.setText(getLocalityFromDataManager());
        rootView.findViewById(R.id.filter_button).setOnClickListener(this);
        new NetworkManagerTask(MyUrls.GET_SPECIALIZATIONS, null, MyConstants.HTTP_GET, this, ServerCallPurpose.SPECIALIZATIONS).execute();
        return rootView;
    }

    /**
     * Fetches locality from the data manager if it exists.
     */
    private String getLocalityFromDataManager() {
        String locality = null;
        DataManager dataManager = DataManager.getSoleInstance();
        int roleId = dataManager.getSignInResponse().getRoleId();
        if (roleId == MyConstants.ROLE_ID_PATIENT) {
            PatientProfileBean profileBean = dataManager.getPatientProfileBean();
            locality = profileBean.getAddressBean().getLocality();
        } else if (roleId == MyConstants.ROLE_ID_DOCTOR) {
            DoctorProfileBean profileBean = dataManager.getDoctorProfileBean();
            locality = profileBean.getAddressBean().getLocality();
        }
        return locality;
    }

    /**
     * Fetches response received from the server.
     * This method also calls various methods based on the call purpose of the server request to
     * handle the server response.
     *
     * @param iResponse response received from the server.
     * @param iCallPurpose call purpose for the server request.
     */
    @Override
    public void responseFromServer(String iResponse, int iCallPurpose) {
        mSpecializationsBeans = JsonParser.createSpecializationsBeans(iResponse);
        int length = mSpecializationsBeans != null ? mSpecializationsBeans.length : 0;
        MyCheckBoxBean[] myCheckBoxBeen = new MyCheckBoxBean[length];
        for (int i = 0; i < length; i++) {
            myCheckBoxBeen[i] = new MyCheckBoxBean(mSpecializationsBeans[i].getSpecialization());
        }
        myArrayAdapter = new MyArrayAdapter(getContext(), R.id.my_list_view, myCheckBoxBeen);
        mListView.setAdapter(myArrayAdapter);
    }

    @Override
    public void errorOccurred(int iResponseCode, Exception iException, int iCallPurpose) {

    }

    /**
     * When filter button is clicked, sets the SearchDoctorRequestBean and pops up fragment from
     * the backstack.
     */
    @Override
    public void onClick(View iView) {
        DataManager dataManager = DataManager.getSoleInstance();
        int[] specializationIds = new int[0];
        if (myArrayAdapter != null) {
            List<String> specializations = myArrayAdapter.getSelectedItems();
            specializationIds = getSpecializationIds(specializations);
        }
        dataManager.setSearchDoctorRequestBean(new SearchDoctorRequestBean(mLocalityEditText.getText().toString(), specializationIds));
        getFragmentManager().popBackStack();
    }

    /**
     * Extracts specialization ids from SpecializationBean corresponding to the selected specializations.
     */
    private int[] getSpecializationIds(List<String> iSpecializations) {
        int size = iSpecializations.size();
        int[] specializationIds = new int[size];
        for (int i = 0; i < size; i++) {
            for (SpecializationsBean mSpecializationsBean : mSpecializationsBeans) {
                if (mSpecializationsBean.getSpecialization().equals(iSpecializations.get(i))) {
                    specializationIds[i] = mSpecializationsBean.getSpecializationId();
                }
            }
        }
        return specializationIds;
    }

}
