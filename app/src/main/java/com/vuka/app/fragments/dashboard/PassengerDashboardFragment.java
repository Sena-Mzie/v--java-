package com.vuka.app.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.fragments.contacts.TrustedContactsFragment;
import com.vuka.app.fragments.emergency.SosEmergencyFragment;
import com.vuka.app.fragments.incidents.ReportIncidentFragment;
import com.vuka.app.fragments.profile.ProfileFragment;
import com.vuka.app.fragments.trips.CreateTripFragment;
import com.vuka.app.repositories.MockDataRepository;
import com.vuka.app.utils.TimeUtils;

/**
 * Passenger home dashboard (Flow 6).
 * Shows journey CTA, shared trips, safety alerts, contacts, and report shortcut.
 */
public class PassengerDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Personalize greeting
        String userName = VukaApplication.getInstance().getUserName();
        ((TextView) view.findViewById(R.id.tv_greeting)).setText(TimeUtils.getGreeting() + ",");
        ((TextView) view.findViewById(R.id.tv_username)).setText(userName);

        // Contacts summary
        int total = MockDataRepository.getInstance().getTrustedContacts().size();
        int accepted = MockDataRepository.getInstance().getAcceptedContactCount();
        ((TextView) view.findViewById(R.id.tv_contacts_summary))
                .setText(total + " contacts  |  " + accepted + " accepted");

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        // Start Journey CTA
        view.findViewById(R.id.btn_start_journey).setOnClickListener(v ->
                navigateTo(new CreateTripFragment(), "create_trip"));

        // Shared Trips
        view.findViewById(R.id.card_shared_trips).setOnClickListener(v ->
                navigateTo(new CreateTripFragment(), "create_trip"));

        // Safety Alerts
        view.findViewById(R.id.card_safety_alerts).setOnClickListener(v ->
                navigateTo(new ReportIncidentFragment(), "report_incident"));

        // Trusted Contacts
        view.findViewById(R.id.card_trusted_contacts).setOnClickListener(v ->
                navigateTo(new TrustedContactsFragment(), "contacts"));

        // Report Incident
        view.findViewById(R.id.card_report_incident).setOnClickListener(v ->
                navigateTo(new ReportIncidentFragment(), "report_incident"));

        // Avatar → Profile
        view.findViewById(R.id.tv_avatar_pass).setOnClickListener(v ->
                navigateTo(new ProfileFragment(), "profile"));

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
