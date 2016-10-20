package com.mindfire.ourhealth.searchdoctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.adapters.PracticeLocationsAdapter;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.utils.OurHealthUtils;

/**
 * This fragment is used to display all the practice locations of a doctor.
 * It is also used to access book appointments screen.
 */
public class DoctorPracticeLocationFragment extends Fragment {

    private static final String TOOLBAR_TITLE = "Practice Locations";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctor_practice_location, container, false);
        // Set toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        // Sets the adapter for the list view.
        PracticeLocationsAdapter adapter = new PracticeLocationsAdapter(
                getContext(),
                R.layout.practice_location_item,
                DataManager.getSoleInstance().getDoctorPracticeLocationBeen()
        );
        ((ListView)rootView.findViewById(R.id.my_list_view)).setAdapter(adapter);
        return rootView;
    }

}
