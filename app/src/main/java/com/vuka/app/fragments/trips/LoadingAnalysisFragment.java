package com.vuka.app.fragments.trips;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.models.TripSafetyProfile;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Safety analysis loading screen (Flow 7).
 * Animated 4-step analysis sequence before showing the safety profile.
 */
public class LoadingAnalysisFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";
    private static final String ARG_PICKUP = "pickup";
    private static final String ARG_DEST = "destination";

    private static final long STEP_DELAY_MS = 1000;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private LinearLayout stepPickup, stepRoute, stepDest, stepBuilding;
    private ProgressBar pb1, pb2, pb3, pb4;
    private TextView tv1, tv2, tv3, tv4;

    private String tripId;
    private String pickup;
    private String destination;

    public static LoadingAnalysisFragment newInstance(String tripId, String pickup, String destination) {
        LoadingAnalysisFragment f = new LoadingAnalysisFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIP_ID, tripId);
        args.putString(ARG_PICKUP, pickup);
        args.putString(ARG_DEST, destination);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getString(ARG_TRIP_ID, "");
            pickup = getArguments().getString(ARG_PICKUP, "");
            destination = getArguments().getString(ARG_DEST, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepPickup = view.findViewById(R.id.step_pickup);
        stepRoute = view.findViewById(R.id.step_route);
        stepDest = view.findViewById(R.id.step_destination);
        stepBuilding = view.findViewById(R.id.step_building);

        pb1 = view.findViewById(R.id.pb_step1);
        pb2 = view.findViewById(R.id.pb_step2);
        pb3 = view.findViewById(R.id.pb_step3);
        pb4 = view.findViewById(R.id.pb_step4);

        tv1 = view.findViewById(R.id.tv_step1);
        tv2 = view.findViewById(R.id.tv_step2);
        tv3 = view.findViewById(R.id.tv_step3);
        tv4 = view.findViewById(R.id.tv_step4);

        startAnalysisSequence();
    }

    private void startAnalysisSequence() {
        // Step 1: Checking Pickup (immediate)
        activateStep(stepPickup, pb1, tv1, getString(R.string.checking_pickup));

        // Step 2: Analyzing Route
        handler.postDelayed(() -> {
            completeStep(stepPickup, pb1, tv1);
            activateStep(stepRoute, pb2, tv2, getString(R.string.analyzing_route_step));
        }, STEP_DELAY_MS);

        // Step 3: Checking Destination
        handler.postDelayed(() -> {
            completeStep(stepRoute, pb2, tv2);
            activateStep(stepDest, pb3, tv3, getString(R.string.checking_destination));
        }, STEP_DELAY_MS * 2);

        // Step 4: Building Profile
        handler.postDelayed(() -> {
            completeStep(stepDest, pb3, tv3);
            activateStep(stepBuilding, pb4, tv4, getString(R.string.building_profile));
        }, STEP_DELAY_MS * 3);

        // Navigate to safety profile
        handler.postDelayed(() -> {
            if (isAdded()) {
                completeStep(stepBuilding, pb4, tv4);
                proceedToSafetyProfile();
            }
        }, STEP_DELAY_MS * 4);
    }

    private void activateStep(LinearLayout step, ProgressBar pb, TextView tv, String label) {
        if (step == null || getContext() == null) return;
        step.setAlpha(1f);
        pb.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.vuka_gold)));
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.vuka_text_primary));
        tv.setText(label);
    }

    private void completeStep(LinearLayout step, ProgressBar pb, TextView tv) {
        if (pb == null || tv == null || getContext() == null) return;
        pb.setVisibility(View.INVISIBLE);
        tv.setText("✅ " + tv.getText());
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.risk_low));
    }

    private void proceedToSafetyProfile() {
        // Generate and store the safety profile
        MockDataRepository repo = MockDataRepository.getInstance();
        TripSafetyProfile profile = repo.generateDemoSafetyProfile(tripId, pickup, destination);
        repo.setCurrentSafetyProfile(profile);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(
                    new TripSafetyProfileFragment(), "safety_profile");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
