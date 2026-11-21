package com.vuka.app.fragments.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.activities.TripMonitorActivity;
import com.vuka.app.adapters.RiskFactorAdapter;
import com.vuka.app.models.Trip;
import com.vuka.app.models.TripSafetyProfile;
import com.vuka.app.repositories.MockDataRepository;
import com.vuka.app.utils.RiskColorUtils;

/**
 * Trip Safety Profile screen (Flow 8).
 * Shows overall risk score, breakdown by area, risk factors, and safer route recommendation.
 */
public class TripSafetyProfileFragment extends Fragment {

    private TripSafetyProfile profile;
    private boolean riskFactorsExpanded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_safety_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = MockDataRepository.getInstance().getCurrentSafetyProfile();
        if (profile == null) {
            if (getActivity() != null) getActivity().onBackPressed();
            return;
        }

        populateProfile(view);
        setupClickListeners(view);
    }

    private void populateProfile(View view) {
        if (getContext() == null) return;

        // Overall risk
        TextView tvScore = view.findViewById(R.id.tv_risk_score);
        TextView tvBadge = view.findViewById(R.id.tv_risk_level_badge);
        MaterialCardView cardOverall = view.findViewById(R.id.card_overall_risk);

        tvScore.setText(String.valueOf(profile.getOverallScore()));
        tvBadge.setText(RiskColorUtils.getRiskLabel(profile.getOverallRiskLevel()) + " RISK");

        int riskColor = RiskColorUtils.getRiskColor(requireContext(), profile.getOverallRiskLevel());
        tvScore.setTextColor(riskColor);
        tvBadge.setTextColor(riskColor);
        tvBadge.setBackground(RiskColorUtils.getRiskBackground(requireContext(), profile.getOverallRiskLevel()));
        cardOverall.setStrokeColor(riskColor);

        // Pickup risk
        setRiskSection(view, R.id.tv_pickup_risk_badge, R.id.tv_pickup_risk_desc,
                profile.getPickupRiskLevel(), profile.getPickupRiskDescription());

        // Route risk
        setRiskSection(view, R.id.tv_route_risk_badge, R.id.tv_route_risk_desc,
                profile.getRouteRiskLevel(), profile.getRouteRiskDescription());

        // Destination risk
        setRiskSection(view, R.id.tv_dest_risk_badge, R.id.tv_dest_risk_desc,
                profile.getDestinationRiskLevel(), profile.getDestinationRiskDescription());

        // Risk factors recycler
        RecyclerView rvFactors = view.findViewById(R.id.rv_risk_factors);
        rvFactors.setLayoutManager(new LinearLayoutManager(getContext()));
        if (profile.getRiskFactors() != null) {
            rvFactors.setAdapter(new RiskFactorAdapter(profile.getRiskFactors()));
        }
    }

    private void setRiskSection(View view, int badgeId, int descId, String riskLevel, String description) {
        if (getContext() == null) return;
        TextView badge = view.findViewById(badgeId);
        TextView desc = view.findViewById(descId);

        badge.setText(RiskColorUtils.getRiskLabel(riskLevel));
        badge.setTextColor(RiskColorUtils.getRiskColor(requireContext(), riskLevel));
        badge.setBackground(RiskColorUtils.getRiskBackground(requireContext(), riskLevel));
        if (desc != null) desc.setText(description);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_back_profile).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // Risk factors expand/collapse
        view.findViewById(R.id.card_risk_factors).setOnClickListener(v -> {
            riskFactorsExpanded = !riskFactorsExpanded;
            LinearLayout detail = view.findViewById(R.id.ll_risk_factors_detail);
            TextView arrow = view.findViewById(R.id.tv_expand_factors);
            detail.setVisibility(riskFactorsExpanded ? View.VISIBLE : View.GONE);
            arrow.setText(riskFactorsExpanded ? "▲" : "▼");
        });

        // Safer route selection
        view.findViewById(R.id.btn_use_safer_route).setOnClickListener(v -> {
            Trip activeTrip = MockDataRepository.getInstance().getActiveTrip();
            if (activeTrip != null) {
                activeTrip.setSaferRouteSelected(true);
            }
            Toast.makeText(getContext(), "✅ Safer route selected", Toast.LENGTH_SHORT).show();
            startActiveMonitoring();
        });

        view.findViewById(R.id.btn_keep_original).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Original route kept", Toast.LENGTH_SHORT).show();
            startActiveMonitoring();
        });

        view.findViewById(R.id.btn_start_trip).setOnClickListener(v -> startActiveMonitoring());
    }

    private void startActiveMonitoring() {
        Trip activeTrip = MockDataRepository.getInstance().getActiveTrip();
        if (activeTrip != null) {
            activeTrip.setStatus(Trip.STATUS_ACTIVE);
            activeTrip.setStartTime(System.currentTimeMillis());
        }

        Intent intent = new Intent(getContext(), TripMonitorActivity.class);
        startActivity(intent);
    }
}
