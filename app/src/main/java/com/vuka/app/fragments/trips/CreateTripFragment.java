package com.vuka.app.fragments.trips;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.adapters.TripAdapter;
import com.vuka.app.models.Trip;
import com.vuka.app.repositories.MockDataRepository;

import java.util.Calendar;
import java.util.Locale;

/**
 * Create Trip screen (Flow 7).
 * Pickup, destination, date/time selection, map preview, and safety profile generation.
 */
public class CreateTripFragment extends Fragment {

    private EditText etPickup;
    private EditText etDestination;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvError;
    private final Calendar selectedDateTime = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPickup = view.findViewById(R.id.et_pickup);
        etDestination = view.findViewById(R.id.et_destination);
        tvDate = view.findViewById(R.id.tv_date);
        tvTime = view.findViewById(R.id.tv_time);
        tvError = view.findViewById(R.id.tv_trip_error);

        // Update date/time display
        updateDateTimeDisplay();

        // Date picker
        view.findViewById(R.id.ll_date_picker).setOnClickListener(v -> showDatePicker());
        // Time picker
        view.findViewById(R.id.ll_time_picker).setOnClickListener(v -> showTimePicker());

        // Suggested destinations
        view.findViewById(R.id.btn_home_sug).setOnClickListener(v ->
                etDestination.setText("Home — Soweto, Johannesburg"));
        view.findViewById(R.id.btn_work_sug).setOnClickListener(v ->
                etDestination.setText("Work — Sandton CBD"));

        // Back button
        view.findViewById(R.id.btn_back_trip).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // Recent trips
        RecyclerView rvRecent = view.findViewById(R.id.rv_recent_trips);
        rvRecent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecent.setAdapter(new TripAdapter(
                MockDataRepository.getInstance().getRecentTrips(3),
                this::onTripClicked));

        // Generate safety profile
        view.findViewById(R.id.btn_generate_safety_profile).setOnClickListener(v -> handleGenerateProfile());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (dp, year, month, day) -> {
            selectedDateTime.set(year, month, day);
            updateDateTimeDisplay();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(requireContext(), (tp, hour, minute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, hour);
            selectedDateTime.set(Calendar.MINUTE, minute);
            updateDateTimeDisplay();
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private void updateDateTimeDisplay() {
        tvDate.setText(String.format(Locale.getDefault(), "%d %s",
                selectedDateTime.get(Calendar.DAY_OF_MONTH),
                selectedDateTime.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())));
        tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d",
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE)));
    }

    private void handleGenerateProfile() {
        String pickup = etPickup.getText() != null ? etPickup.getText().toString().trim() : "";
        String dest = etDestination.getText() != null ? etDestination.getText().toString().trim() : "";

        tvError.setVisibility(View.GONE);

        if (TextUtils.isEmpty(pickup)) {
            tvError.setText("Please enter a pickup location");
            tvError.setVisibility(View.VISIBLE);
            etPickup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dest)) {
            tvError.setText("Please enter a destination");
            tvError.setVisibility(View.VISIBLE);
            etDestination.requestFocus();
            return;
        }

        // Create a trip and navigate to loading analysis
        String tripId = "trp_" + System.currentTimeMillis();
        Trip newTrip = new Trip(tripId, "user_001", pickup, dest);
        newTrip.setScheduledTime(selectedDateTime.getTimeInMillis());

        MockDataRepository repo = MockDataRepository.getInstance();
        repo.addTrip(newTrip);
        repo.setActiveTrip(newTrip);

        // Navigate to loading/analysis screen
        LoadingAnalysisFragment loadingFragment = LoadingAnalysisFragment.newInstance(tripId, pickup, dest);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(loadingFragment, "loading_analysis");
        }
    }

    private void onTripClicked(Trip trip) {
        // Pre-fill from previous trip
        etPickup.setText(trip.getPickupLocation());
        etDestination.setText(trip.getDestination());
    }
}
