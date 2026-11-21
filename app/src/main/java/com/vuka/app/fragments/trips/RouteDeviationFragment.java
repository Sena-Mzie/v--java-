package com.vuka.app.fragments.trips;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.activities.TripMonitorActivity;
import com.vuka.app.fragments.emergency.SosEmergencyFragment;
import com.vuka.app.models.Trip;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Route Deviation detection screen (Flow 11).
 * Presents options: I'm OK, Expected Detour, I Need Help.
 * Auto-escalates if no response in 60 seconds.
 */
public class RouteDeviationFragment extends Fragment {

    private CountDownTimer autoEscalateTimer;
    private static final int AUTO_ESCALATE_SECONDS = 60;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_deviation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTimer = view.findViewById(R.id.tv_auto_response_timer);

        // Start auto-escalate countdown
        autoEscalateTimer = new CountDownTimer(AUTO_ESCALATE_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isAdded()) {
                    tvTimer.setText("Auto-escalating in " + (millisUntilFinished / 1000) + " seconds if no response");
                }
            }

            @Override
            public void onFinish() {
                if (isAdded()) {
                    // Auto-escalate to SOS
                    activateSos();
                }
            }
        };
        autoEscalateTimer.start();

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_im_ok).setOnClickListener(v -> {
            cancelTimerAndUpdate();
            Trip trip = MockDataRepository.getInstance().getActiveTrip();
            if (trip != null) trip.setDeviationDetected(false);
            Toast.makeText(getContext(), "✅ Confirmed safe — continuing trip", Toast.LENGTH_SHORT).show();
            // Return to monitoring
            if (getActivity() instanceof TripMonitorActivity) {
                getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.btn_expected_detour).setOnClickListener(v -> {
            cancelTimerAndUpdate();
            Toast.makeText(getContext(), "Detour noted — monitoring continues", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof TripMonitorActivity) {
                getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.btn_need_help_deviation).setOnClickListener(v -> {
            cancelTimerAndUpdate();
            activateSos();
        });
    }

    private void cancelTimerAndUpdate() {
        if (autoEscalateTimer != null) {
            autoEscalateTimer.cancel();
        }
    }

    private void activateSos() {
        MockDataRepository.getInstance().activateSos();
        if (getActivity() instanceof TripMonitorActivity) {
            ((TripMonitorActivity) getActivity()).navigateTo(new SosEmergencyFragment(), "sos");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoEscalateTimer != null) {
            autoEscalateTimer.cancel();
        }
    }
}
