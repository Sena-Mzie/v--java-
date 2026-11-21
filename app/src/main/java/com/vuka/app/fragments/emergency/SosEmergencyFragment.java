package com.vuka.app.fragments.emergency;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.repositories.MockDataRepository;
import com.vuka.app.utils.SosHoldButton;

/**
 * SOS Emergency screen (Flow 13).
 * Press-and-hold 2 seconds to activate.
 * Shows asynchronous simulated notification status after activation.
 *
 * IMPORTANT: All notifications are SIMULATED. No real emergency services contacted.
 */
public class SosEmergencyFragment extends Fragment {

    private LinearLayout llNormalState;
    private LinearLayout llEmergencyState;
    private CountDownTimer emergencyTimer;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int timerSeconds = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sos_emergency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llNormalState = view.findViewById(R.id.ll_normal_state);
        llEmergencyState = view.findViewById(R.id.ll_emergency_state);

        // Check if SOS already active
        if (MockDataRepository.getInstance().isSosActive()) {
            showEmergencyState(view);
        }

        // SOS hold button
        SosHoldButton sosButton = view.findViewById(R.id.btn_sos_hold);
        sosButton.setSosActivationListener(new SosHoldButton.SosActivationListener() {
            @Override
            public void onSosActivated() {
                activateEmergency(view);
            }

            @Override
            public void onSosHoldStarted() {
                // Visual feedback already handled in SosHoldButton
            }

            @Override
            public void onSosHoldCancelled() {
                // Reset
            }
        });

        // Accessible alternative button
        view.findViewById(R.id.btn_sos_accessible).setOnClickListener(v -> showConfirmDialog(view));

        // Cancel from normal state
        view.findViewById(R.id.btn_cancel_sos_screen).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // Cancel emergency
        view.findViewById(R.id.btn_cancel_emergency).setOnClickListener(v -> cancelEmergency());
    }

    private void showConfirmDialog(View view) {
        // Simple inline confirmation — show a toast then activate after 2s
        Toast.makeText(getContext(), "Hold confirmed — activating emergency...", Toast.LENGTH_SHORT).show();
        handler.postDelayed(() -> {
            if (isAdded()) activateEmergency(view);
        }, 1500);
    }

    private void activateEmergency(View view) {
        MockDataRepository.getInstance().activateSos();
        showEmergencyState(view);
    }

    private void showEmergencyState(View view) {
        llNormalState.setVisibility(View.GONE);
        llEmergencyState.setVisibility(View.VISIBLE);

        // Simulate async service notifications
        simulateNotificationProgress(view);

        // Emergency timer
        TextView tvTimer = view.findViewById(R.id.tv_emergency_timer);
        timerSeconds = 0;
        emergencyTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerSeconds++;
                int mins = timerSeconds / 60;
                int secs = timerSeconds % 60;
                if (isAdded()) {
                    tvTimer.setText(String.format("%02d:%02d", mins, secs));
                }
            }

            @Override
            public void onFinish() {}
        };
        emergencyTimer.start();
    }

    private void simulateNotificationProgress(View view) {
        ProgressBar pbContacts = view.findViewById(R.id.pb_contacts);
        ProgressBar pbSms = view.findViewById(R.id.pb_sms);
        ProgressBar pbPartner = view.findViewById(R.id.pb_partner);
        ProgressBar pbLocation = view.findViewById(R.id.pb_location);

        TextView tvContacts = view.findViewById(R.id.tv_status_contacts);
        TextView tvSms = view.findViewById(R.id.tv_status_sms);
        TextView tvPartner = view.findViewById(R.id.tv_status_partner);
        TextView tvLocation = view.findViewById(R.id.tv_status_location);

        // Step 1: Contacts notified (1s)
        handler.postDelayed(() -> {
            if (isAdded()) {
                pbContacts.setVisibility(View.INVISIBLE);
                tvContacts.setText("✅ Contacts Notified (DEMO)");
                if (getContext() != null)
                    tvContacts.setTextColor(ContextCompat.getColor(requireContext(), R.color.risk_low));
            }
        }, 1000);

        // Step 2: SMS sent (2s)
        handler.postDelayed(() -> {
            if (isAdded()) {
                pbSms.setVisibility(View.INVISIBLE);
                tvSms.setText("✅ SMS Sent (DEMO — simulated)");
                if (getContext() != null)
                    tvSms.setTextColor(ContextCompat.getColor(requireContext(), R.color.risk_low));
            }
        }, 2000);

        // Step 3: Partner notified (3s)
        handler.postDelayed(() -> {
            if (isAdded()) {
                pbPartner.setVisibility(View.INVISIBLE);
                tvPartner.setText("✅ Partner Notified (DEMO)");
                if (getContext() != null)
                    tvPartner.setTextColor(ContextCompat.getColor(requireContext(), R.color.risk_low));
            }
        }, 3000);

        // Step 4: Location sharing (4s)
        handler.postDelayed(() -> {
            if (isAdded()) {
                pbLocation.setVisibility(View.INVISIBLE);
                tvLocation.setText("✅ Location Sharing Active (DEMO)");
                if (getContext() != null)
                    tvLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.risk_low));
            }
        }, 4000);
    }

    private void cancelEmergency() {
        MockDataRepository.getInstance().cancelSos();
        if (emergencyTimer != null) emergencyTimer.cancel();
        Toast.makeText(getContext(), "Emergency cancelled", Toast.LENGTH_SHORT).show();
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        if (emergencyTimer != null) emergencyTimer.cancel();
    }
}
