package com.vuka.app.fragments.trips;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.vuka.app.fragments.incidents.ReportIncidentFragment;
import com.vuka.app.models.Trip;
import com.vuka.app.repositories.MockDataRepository;
import com.vuka.app.utils.RiskColorUtils;

/**
 * Active Trip Monitoring screen (Flow 10).
 * Full-screen map simulation + trip details + actions.
 * Simulates route deviation after 15 seconds for demonstration.
 */
public class ActiveMonitoringFragment extends Fragment {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int etaMinutes = 6;
    private Runnable etaRunnable;
    private Runnable deviationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_monitoring, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Trip trip = MockDataRepository.getInstance().getActiveTrip();

        // Populate driver info
        TextView tvDriver = view.findViewById(R.id.tv_driver_name);
        TextView tvVehicle = view.findViewById(R.id.tv_vehicle_info);
        TextView tvRisk = view.findViewById(R.id.tv_risk_status_monitoring);
        TextView tvEta = view.findViewById(R.id.tv_eta);

        if (trip != null) {
            String riskLevel = trip.getRiskLevel() != null ? trip.getRiskLevel() : Trip.RISK_LOW;
            tvRisk.setText(RiskColorUtils.getRiskLabel(riskLevel) + " RISK");
            tvRisk.setTextColor(RiskColorUtils.getRiskColor(requireContext(), riskLevel));
            tvRisk.setBackground(RiskColorUtils.getRiskBackground(requireContext(), riskLevel));
        }

        // ETA countdown
        tvEta.setText(etaMinutes + " min");
        etaRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) return;
                if (etaMinutes > 0) {
                    etaMinutes--;
                    tvEta.setText(etaMinutes + " min");
                    handler.postDelayed(this, 10000); // Update every 10 seconds
                } else {
                    // Arrived
                    navigateToSafeArrival();
                }
            }
        };
        handler.postDelayed(etaRunnable, 10000);

        // Simulate deviation after 15 seconds (demo)
        deviationRunnable = () -> {
            if (isAdded()) {
                Trip activeTrip = MockDataRepository.getInstance().getActiveTrip();
                if (activeTrip != null) {
                    activeTrip.setDeviationDetected(true);
                }
                navigateToDeviation();
            }
        };
        handler.postDelayed(deviationRunnable, 15000);

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_back_monitoring).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_sos_monitoring).setOnClickListener(v -> {
            if (getActivity() instanceof TripMonitorActivity) {
                ((TripMonitorActivity) getActivity()).navigateTo(new SosEmergencyFragment(), "sos");
            }
        });

        view.findViewById(R.id.btn_share_trip).setOnClickListener(v -> {
            Toast.makeText(getContext(),
                    "DEMO: Trip link shared with trusted contacts (simulated)",
                    Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_report_monitoring).setOnClickListener(v -> {
            if (getActivity() instanceof TripMonitorActivity) {
                ((TripMonitorActivity) getActivity()).navigateTo(new ReportIncidentFragment(), "report");
            }
        });
    }

    private void navigateToDeviation() {
        if (getActivity() instanceof TripMonitorActivity) {
            ((TripMonitorActivity) getActivity()).navigateTo(
                    new RouteDeviationFragment(), "route_deviation");
        }
    }

    private void navigateToSafeArrival() {
        if (getActivity() instanceof TripMonitorActivity) {
            ((TripMonitorActivity) getActivity()).navigateTo(
                    new SafeArrivalFragment(), "safe_arrival");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
