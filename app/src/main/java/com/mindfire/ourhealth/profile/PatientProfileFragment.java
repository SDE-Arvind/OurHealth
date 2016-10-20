package com.mindfire.ourhealth.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindfire.ourhealth.R;
import com.mindfire.ourhealth.background.ImageFetcher;
import com.mindfire.ourhealth.beans.DataManager;
import com.mindfire.ourhealth.beans.PatientProfileBean;
import com.mindfire.ourhealth.beans.ProfileAddressBean;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ImageResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;

import java.util.Locale;

/**
 * This fragment is used to display the profile of the user.
 */
public class PatientProfileFragment extends Fragment  implements ImageResponseListener{

    private static final String TOOLBAR_TITLE = "Profile";
    private static final String ID_REQUEST_FORMAT = "{\"UserId\": %d }";

    private ImageView mProfileImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_patient, container, false);
        // Sets the toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        initViews(rootView);
        String request = String.format(Locale.US, ID_REQUEST_FORMAT, DataManager.getSoleInstance().getSignInResponse().getUserId());
        new ImageFetcher(MyUrls.GET_PROFILE_PIC, request, this, getContext()).execute();
        return rootView;
    }

    /**
     * Initializes the views for this fragment.
     *
     * @param iView root view.
     */
    private void initViews(View iView) {
        // Initializes the views and sets the data in the respective fields.
        PatientProfileBean profileBean = DataManager.getSoleInstance().getPatientProfileBean();
        if (profileBean != null) {
            ProfileAddressBean addressBean = profileBean.getAddressBean();
            ((TextView) iView.findViewById(R.id.name_text_view)).setText(String.format("%s %s", profileBean.getFirstName(), profileBean.getLastName()));
            ((TextView) iView.findViewById(R.id.gender_text_view)).setText(profileBean.getGender());
            ((TextView) iView.findViewById(R.id.dob_text_view)).setText(profileBean.getDob());
            ((TextView) iView.findViewById(R.id.address_text_view)).setText(addressBean.getAddress());
            ((TextView) iView.findViewById(R.id.locality_text_view)).setText(addressBean.getLocality());
            ((TextView) iView.findViewById(R.id.city_text_view)).setText(addressBean.getCity());
            ((TextView) iView.findViewById(R.id.state_text_view)).setText(addressBean.getState());
            ((TextView) iView.findViewById(R.id.country_text_view)).setText(addressBean.getCountry());
            ((TextView) iView.findViewById(R.id.pin_text_view)).setText(addressBean.getPin());
            ((TextView) iView.findViewById(R.id.contact_text_view)).setText(profileBean.getContactNo());
            ((TextView) iView.findViewById(R.id.emergency_contact_text_view)).setText(profileBean.getEmergencyContact());
            ((TextView) iView.findViewById(R.id.emergency_number_text_view)).setText(profileBean.getEmergencyNumber());
            ((TextView) iView.findViewById(R.id.insurance_company_text_view)).setText(profileBean.getInsuranceCompany());
            ((TextView) iView.findViewById(R.id.insurance_number_text_view)).setText(profileBean.getInsuranceNo());
        }
        mProfileImageView = (ImageView) iView.findViewById(R.id.profile_image_view);
    }

    @Override
    public void imageFromServer(byte[] iImageByteArray) {
        if (iImageByteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(iImageByteArray, 0, iImageByteArray.length);
            mProfileImageView.setBackground(null);
            mProfileImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void errorOccurredForImageRequest(Exception iException, int iResponseCode) {

    }
}
