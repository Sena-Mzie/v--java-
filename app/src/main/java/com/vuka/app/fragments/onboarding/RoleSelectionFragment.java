package com.vuka.app.fragments.onboarding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.fragments.auth.LoginFragment;

/**
 * Role selection screen (Flow 1).
 * Allows user to select Driver or Passenger role.
 * Role persists to SharedPreferences.
 */
public class RoleSelectionFragment extends Fragment {

    private String selectedRole = null;
    private MaterialCardView cardDriver;
    private MaterialCardView cardPassenger;
    private Button btnContinue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_role_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardDriver = view.findViewById(R.id.card_driver_role);
        cardPassenger = view.findViewById(R.id.card_passenger_role);
        btnContinue = view.findViewById(R.id.btn_continue_role);

        cardDriver.setOnClickListener(v -> selectRole(VukaApplication.ROLE_DRIVER));
        cardPassenger.setOnClickListener(v -> selectRole(VukaApplication.ROLE_PASSENGER));

        btnContinue.setOnClickListener(v -> {
            if (selectedRole != null) {
                persistRole();
                proceedToLogin();
            }
        });

        btnContinue.setEnabled(false);
    }

    private void selectRole(String role) {
        selectedRole = role;
        btnContinue.setEnabled(true);

        boolean isDriver = VukaApplication.ROLE_DRIVER.equals(role);
        highlightCard(cardDriver, isDriver);
        highlightCard(cardPassenger, !isDriver);
    }

    private void highlightCard(MaterialCardView card, boolean selected) {
        if (getContext() == null) return;
        if (selected) {
            card.setStrokeWidth(4);
            card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.vuka_gold));
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.vuka_navy_surface));
        } else {
            card.setStrokeWidth(2);
            card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.card_border));
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_bg));
        }
    }

    private void persistRole() {
        SharedPreferences.Editor editor = VukaApplication.getInstance().getAppPrefs().edit();
        editor.putString(VukaApplication.KEY_USER_ROLE, selectedRole);
        editor.putBoolean(VukaApplication.KEY_ONBOARDING_DONE, true);
        editor.apply();
    }

    private void proceedToLogin() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(new LoginFragment(), "login");
        }
    }
}
