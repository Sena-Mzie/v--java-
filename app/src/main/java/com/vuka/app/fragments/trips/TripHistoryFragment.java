package com.vuka.app.fragments.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

/**
 * Trip History screen — shown via bottom navigation "Trips" tab.
 */
public class TripHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Reuse report history layout structure with adapter
        View view = inflater.inflate(R.layout.fragment_report_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_back_reports).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_new_report).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(new CreateTripFragment(), "create_trip");
            }
        });

        List<Trip> trips = MockDataRepository.getInstance().getTrips();

        RecyclerView rv = view.findViewById(R.id.rv_reports);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new TripAdapter(trips, trip -> {
            // Tap trip to view details (placeholder)
        }));

        view.findViewById(R.id.ll_empty_reports).setVisibility(
                trips.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setVisibility(trips.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
