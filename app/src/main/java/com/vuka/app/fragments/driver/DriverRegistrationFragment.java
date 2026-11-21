package com.vuka.app.fragments.driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.models.DriverProfile;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Driver profile registration (Flow 3).
 * Personal details, photo capture, vehicle details, e-hailing platform, and verification status.
 */
public class DriverRegistrationFragment extends Fragment {

    private TextInputEditText etFullName, etIdNumber, etRegNumber,
            etVehicleMake, etVehicleModel, etVehicleColour;
    private Spinner spinnerPlatform;
    private TextView tvError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFullName = view.findViewById(R.id.et_full_name);
        etIdNumber = view.findViewById(R.id.et_id_number);
        etRegNumber = view.findViewById(R.id.et_reg_number);
        etVehicleMake = view.findViewById(R.id.et_vehicle_make);
        etVehicleModel = view.findViewById(R.id.et_vehicle_model);
        etVehicleColour = view.findViewById(R.id.et_vehicle_colour);
        spinnerPlatform = view.findViewById(R.id.spinner_platform);
        tvError = view.findViewById(R.id.tv_driver_error);

        // Setup platform spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.ehailing_platforms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlatform.setAdapter(adapter);

        // Pre-fill from existing profile
        DriverProfile existing = MockDataRepository.getInstance().getCurrentDriverProfile();
        if (existing != null) prefillForm(existing);

        view.findViewById(R.id.btn_back_driver).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_take_photo).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Camera capture simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_choose_gallery).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Gallery selection simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_save_driver).setOnClickListener(v -> handleSave());
    }

    private void prefillForm(DriverProfile profile) {
        if (profile.getVehicleRegistration() != null)
            etRegNumber.setText(profile.getVehicleRegistration());
        if (profile.getVehicleMake() != null) etVehicleMake.setText(profile.getVehicleMake());
        if (profile.getVehicleModel() != null) etVehicleModel.setText(profile.getVehicleModel());
        if (profile.getVehicleColour() != null) etVehicleColour.setText(profile.getVehicleColour());
    }

    private void handleSave() {
        String name = getText(etFullName);
        String id = getText(etIdNumber);
        String reg = getText(etRegNumber);
        String make = getText(etVehicleMake);
        String model = getText(etVehicleModel);
        String colour = getText(etVehicleColour);
        String platform = spinnerPlatform.getSelectedItem() != null ?
                spinnerPlatform.getSelectedItem().toString() : "";

        tvError.setVisibility(View.GONE);

        if (TextUtils.isEmpty(name)) { showError("Full name is required"); return; }
        if (TextUtils.isEmpty(reg)) { showError("Registration number is required"); return; }
        if (TextUtils.isEmpty(make)) { showError("Vehicle make is required"); return; }
        if ("Select Platform".equals(platform)) { showError("Please select an e-hailing platform"); return; }

        // Save to repository and prefs
        DriverProfile profile = MockDataRepository.getInstance().getCurrentDriverProfile();
        if (profile == null) profile = new DriverProfile("user_001");
        profile.setVehicleRegistration(reg);
        profile.setVehicleMake(make);
        profile.setVehicleModel(model);
        profile.setVehicleColour(colour);
        profile.setEhailingPlatform(platform);
        profile.setVerificationStatus(DriverProfile.STATUS_PENDING);
        MockDataRepository.getInstance().setCurrentDriverProfile(profile);

        SharedPreferences.Editor editor = VukaApplication.getInstance().getAppPrefs().edit();
        editor.putString(VukaApplication.KEY_USER_NAME, name);
        editor.apply();

        Toast.makeText(getContext(), "Profile saved — Verification pending", Toast.LENGTH_SHORT).show();

        // Navigate to dashboard
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showDashboard();
        }
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
}
