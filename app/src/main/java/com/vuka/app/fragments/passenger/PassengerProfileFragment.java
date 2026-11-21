package com.vuka.app.fragments.passenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.fragments.contacts.TrustedContactsFragment;

/**
 * Passenger profile setup screen (Flow 4).
 * Full name, phone (pre-filled), optional photo, trusted contacts introduction.
 */
public class PassengerProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Reuse driver registration layout base — simplified view
        View view = inflater.inflate(R.layout.fragment_driver_registration, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Adapt the driver form for passenger — hide vehicle sections
        view.findViewById(R.id.tv_photo_section).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tv_vehicle_section).setVisibility(View.GONE);
        view.findViewById(R.id.til_reg_number).setVisibility(View.GONE);
        view.findViewById(R.id.ll_make_model).setVisibility(View.GONE);
        view.findViewById(R.id.til_colour).setVisibility(View.GONE);
        view.findViewById(R.id.tv_platform_label).setVisibility(View.GONE);
        view.findViewById(R.id.spinner_platform).setVisibility(View.GONE);
        view.findViewById(R.id.ll_verification_status).setVisibility(View.GONE);
        view.findViewById(R.id.ll_steps).setVisibility(View.GONE);

        TextInputEditText etName = view.findViewById(R.id.et_full_name);
        // Hide ID for passenger
        view.findViewById(R.id.til_id_number).setVisibility(View.GONE);

        view.findViewById(R.id.btn_back_driver).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_take_photo).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Camera capture simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_choose_gallery).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Gallery simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_save_driver).setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                return;
            }

            SharedPreferences.Editor editor = VukaApplication.getInstance().getAppPrefs().edit();
            editor.putString(VukaApplication.KEY_USER_NAME, name);
            editor.apply();

            // Show trusted contacts intro
            showTrustedContactsIntro();
        });
    }

    private void showTrustedContactsIntro() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Trusted Contacts")
                .setMessage("Add trusted contacts who can monitor your trips and receive emergency alerts.")
                .setPositiveButton("Add Contact Now", (d, w) -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).navigateTo(
                                new TrustedContactsFragment(), "contacts");
                    }
                })
                .setNegativeButton("Skip", (d, w) -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).showDashboard();
                    }
                })
                .show();
    }
}
