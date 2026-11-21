package com.vuka.app.fragments.trips;

import android.content.Intent;
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
import com.vuka.app.activities.TripMonitorActivity;
import com.vuka.app.fragments.emergency.SosEmergencyFragment;
import com.vuka.app.models.Trip;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Safe Arrival confirmation screen (Flow 15).
 * Prompts user to confirm arrival. Ends monitoring and shows summary.
 * "Need Help" escalates to SOS.
 */
public class SafeArrivalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_safe_arrival, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View arrivalCheck = view.findViewById(R.id.ll_arrival_check);
        View completedState = view.findViewById(R.id.ll_completed_state);

        view.findViewById(R.id.btn_yes_arrived).setOnClickListener(v -> {
            // End trip monitoring
            Trip trip = MockDataRepository.getInstance().getActiveTrip();
            if (trip != null) {
                trip.setStatus(Trip.STATUS_COMPLETED);
                trip.setEndTime(System.currentTimeMillis());
                trip.setSafetyScore(95);
                trip.setDurationMinutes(28);
                trip.setDistanceKm(12.5);
                MockDataRepository.getInstance().updateTrip(trip);
            }
            MockDataRepository.getInstance().setActiveTrip(null);

            // Simulate contact notification
            Toast.makeText(getContext(),
                    "DEMO: Trusted contacts notified of safe arrival (simulated)",
                    Toast.LENGTH_SHORT).show();

            // Show completion summary
            arrivalCheck.setVisibility(View.GONE);
            completedState.setVisibility(View.VISIBLE);
        });

        view.findViewById(R.id.btn_need_help_arrival).setOnClickListener(v -> {
            MockDataRepository.getInstance().activateSos();
            if (getActivity() instanceof TripMonitorActivity) {
                ((TripMonitorActivity) getActivity()).navigateTo(new SosEmergencyFragment(), "sos");
            }
        });

        view.findViewById(R.id.btn_view_details).setOnClickListener(v ->
                Toast.makeText(getContext(), "Trip details — coming soon", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_done_arrival).setOnClickListener(v -> {
            // Return to dashboard
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_DASHBOARD);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });
    }
}
