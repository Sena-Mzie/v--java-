package com.vuka.app.fragments.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.adapters.AlertAdapter;
import com.vuka.app.fragments.contacts.TrustedContactsFragment;
import com.vuka.app.fragments.driver.DriverVerificationFragment;
import com.vuka.app.fragments.emergency.SosEmergencyFragment;
import com.vuka.app.fragments.incidents.ReportIncidentFragment;
import com.vuka.app.fragments.trips.CreateTripFragment;
import com.vuka.app.repositories.MockDataRepository;
import com.vuka.app.utils.TimeUtils;

/**
 * Driver home dashboard (Flow 6).
 * Shows safety status, quick actions, trip summary, insights, hotspots, and alerts.
 */
public class DriverDashboardFragment extends Fragment {

    private AlertAdapter alertAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Personalize greeting
        String userName = VukaApplication.getInstance().getUserName();
        ((TextView) view.findViewById(R.id.tv_greeting)).setText(TimeUtils.getGreeting() + ",");
        ((TextView) view.findViewById(R.id.tv_username)).setText(userName);

        // Trips count
        int tripCount = MockDataRepository.getInstance().getTrips().size();
        ((TextView) view.findViewById(R.id.tv_trips_count)).setText(String.valueOf(tripCount));

        // Setup alerts RecyclerView
        RecyclerView rvAlerts = view.findViewById(R.id.rv_alerts);
        rvAlerts.setLayoutManager(new LinearLayoutManager(getContext()));
        alertAdapter = new AlertAdapter(MockDataRepository.getInstance().getAlerts());
        rvAlerts.setAdapter(alertAdapter);

        // Alert badge count
        int unread = MockDataRepository.getInstance().getUnreadAlertCount();
        ((TextView) view.findViewById(R.id.tv_alerts_badge)).setText(String.valueOf(unread));

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        // Quick action: New Trip
        view.findViewById(R.id.btn_new_trip).setOnClickListener(v -> navigateTo(new CreateTripFragment(), "create_trip"));

        // Quick action: Verify Driver
        view.findViewById(R.id.btn_verify_driver_quick).setOnClickListener(v ->
                navigateTo(new DriverVerificationFragment(), "verify_driver"));

        // Quick action: Trusted Contacts
        view.findViewById(R.id.btn_trusted_contacts_quick).setOnClickListener(v ->
                navigateTo(new TrustedContactsFragment(), "contacts"));

        // Quick action: Emergency SOS
        view.findViewById(R.id.btn_sos_quick).setOnClickListener(v ->
                navigateTo(new SosEmergencyFragment(), "sos"));

        // Dashboard cards
        view.findViewById(R.id.card_todays_trips).setOnClickListener(v ->
                navigateTo(new CreateTripFragment(), "create_trip"));

        view.findViewById(R.id.card_safety_insights).setOnClickListener(v ->
                navigateTo(new CreateTripFragment(), "create_trip"));

        view.findViewById(R.id.card_hotspots).setOnClickListener(v ->
                navigateTo(new ReportIncidentFragment(), "report_incident"));

        view.findViewById(R.id.card_recent_alerts).setOnClickListener(v -> {
            // Scroll within — alerts are inline in this card
        });

        // Avatar tap → profile
        view.findViewById(R.id.tv_avatar).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(
                        new com.vuka.app.fragments.profile.ProfileFragment(), "profile");
            }
        });

        // SOS FAB
        ExtendedFloatingActionButton fabSos = view.findViewById(R.id.fab_sos);
        fabSos.setOnClickListener(v -> navigateTo(new SosEmergencyFragment(), "sos"));
    }

    private void navigateTo(Fragment fragment, String tag) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(fragment, tag);
        }
    }
}
