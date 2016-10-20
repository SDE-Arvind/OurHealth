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
import com.mindfire.ourhealth.beans.DoctorProfileBean;
import com.mindfire.ourhealth.beans.ProfileAddressBean;
import com.mindfire.ourhealth.constants.MyUrls;
import com.mindfire.ourhealth.listeners.ImageResponseListener;
import com.mindfire.ourhealth.utils.OurHealthUtils;
import com.mindfire.ourhealth.utils.DataParser;

import java.util.Arrays;
import java.util.Locale;

/**
 * This fragment is used to display the profile of doctor.
 */
public class DoctorProfileFragment extends Fragment implements ImageResponseListener {

    private static final String TOOLBAR_TITLE = "Profile";
    private static final String ID_REQUEST_FORMAT = "{\"UserId\": %d }";

    private ImageView mProfileImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        // Sets the toolbar title for the container activity.
        OurHealthUtils.setToolbarTitleForContainerActivity(getActivity(), TOOLBAR_TITLE);
        initViews(rootView);
        String request = String.format(Locale.US, ID_REQUEST_FORMAT, DataManager.getSoleInstance().getSignInResponse().getUserId());
        new ImageFetcher(MyUrls.GET_PROFILE_PIC, request, this, getContext()).execute();
        return rootView;
    }

    private void initViews(View iView) {
        // Initializes the views and sets the data in the respective fields.
        DoctorProfileBean profileBean = DataManager.getSoleInstance().getDoctorProfileBean();
        if (profileBean != null) {
            ProfileAddressBean addressBean = profileBean.getAddressBean();
            ((TextView) iView.findViewById(R.id.name_text_view)).setText(String.format("%s %s", profileBean.getFirstName(), profileBean.getLastName()));
            ((TextView) iView.findViewById(R.id.gender_text_view)).setText(profileBean.getGender());
            ((TextView) iView.findViewById(R.id.address_text_view)).setText(addressBean.getAddress());
            ((TextView) iView.findViewById(R.id.locality_text_view)).setText(addressBean.getLocality());
            ((TextView) iView.findViewById(R.id.city_text_view)).setText(addressBean.getCity());
            ((TextView) iView.findViewById(R.id.state_text_view)).setText(addressBean.getState());
            ((TextView) iView.findViewById(R.id.country_text_view)).setText(addressBean.getCountry());
            ((TextView) iView.findViewById(R.id.pin_text_view)).setText(addressBean.getPin());
            ((TextView) iView.findViewById(R.id.medical_reg_no_text_view)).setText(profileBean.getMedicalRegNo());
            ((TextView) iView.findViewById(R.id.about_me_text_view)).setText(profileBean.getAboutMe());
            ((TextView) iView.findViewById(R.id.contact_text_view)).setText(profileBean.getContactNo());
            ((TextView) iView.findViewById(R.id.qualification_text_view)).setText(Arrays.toString(DataParser.getQualifications(profileBean.getQualificationBeen())));
            ((TextView) iView.findViewById(R.id.specialization_text_view)).setText(Arrays.toString(DataParser.getSpecializations(profileBean.getSpecializationsBeen())));
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
