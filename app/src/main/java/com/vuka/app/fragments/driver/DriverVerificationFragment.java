package com.vuka.app.fragments.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.fragments.emergency.SosEmergencyFragment;

/**
 * Driver Verification screen (Flow 9).
 * QR scanning simulation with verified/mismatch states.
 */
public class DriverVerificationFragment extends Fragment {

    private View llScanState;
    private View llVerifiedState;
    private View llMismatchState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llScanState = view.findViewById(R.id.ll_scan_state);
        llVerifiedState = view.findViewById(R.id.ll_verified_state);
        llMismatchState = view.findViewById(R.id.ll_mismatch_state);

        view.findViewById(R.id.btn_back_verify).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // Manual search
        view.findViewById(R.id.btn_manual_search).setOnClickListener(v -> {
            Toast.makeText(getContext(), "DEMO: Enter code 'VUKA_DEMO' to verify", Toast.LENGTH_SHORT).show();
            showVerifiedState();
        });

        // Demo scan button (simulates a successful QR scan)
        view.findViewById(R.id.btn_demo_scan).setOnClickListener(v -> {
            Toast.makeText(getContext(), "DEMO: QR scanned (simulated)", Toast.LENGTH_SHORT).show();
            showVerifiedState();
        });

        view.findViewById(R.id.btn_start_trip_verified).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(
                        new com.vuka.app.fragments.trips.CreateTripFragment(), "create_trip");
            }
        });

        // Scan again
        view.findViewById(R.id.btn_scan_again).setOnClickListener(v -> showScanState());

        // Mismatch buttons
        view.findViewById(R.id.btn_notify_contacts_mismatch).setOnClickListener(v -> {
            Toast.makeText(getContext(),
                    "DEMO: Trusted contacts notified of vehicle mismatch (simulated)",
                    Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(new SosEmergencyFragment(), "sos");
            }
        });

        view.findViewById(R.id.btn_dismiss_mismatch).setOnClickListener(v -> showScanState());
    }

    private void showVerifiedState() {
        llScanState.setVisibility(View.GONE);
        llMismatchState.setVisibility(View.GONE);
        llVerifiedState.setVisibility(View.VISIBLE);
    }

    private void showMismatchState() {
        llScanState.setVisibility(View.GONE);
        llVerifiedState.setVisibility(View.GONE);
        llMismatchState.setVisibility(View.VISIBLE);
    }

    private void showScanState() {
        llVerifiedState.setVisibility(View.GONE);
        llMismatchState.setVisibility(View.GONE);
        llScanState.setVisibility(View.VISIBLE);
    }
}
