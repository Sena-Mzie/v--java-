package com.vuka.app.fragments.incidents;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.models.IncidentReport;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Report Incident screen (Flow 14).
 * Type selection, location (simulated), photo upload, description, submission.
 */
public class ReportIncidentFragment extends Fragment {

    private String selectedType = null;
    private EditText etDescription;
    private TextView tvError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_incident_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.et_description);
        tvError = view.findViewById(R.id.tv_incident_error);

        setupChipGroup(view);

        view.findViewById(R.id.btn_back_incident).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_use_current_location).setOnClickListener(v -> {
            ((TextView) view.findViewById(R.id.tv_selected_location))
                    .setText("📍 Current location (GPS simulated) · Sandton Drive");
            Toast.makeText(getContext(), "DEMO: Using simulated GPS location", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_select_on_map).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Map pin selection simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.ll_photo_upload).setOnClickListener(v ->
                Toast.makeText(getContext(), "DEMO: Photo upload simulated", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btn_submit_report).setOnClickListener(v -> handleSubmit(view));
    }

    private void setupChipGroup(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_incident_type);

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                selectedType = chip != null ? chip.getText().toString() : null;
            } else {
                selectedType = null;
            }
        });
    }

    private void handleSubmit(View view) {
        tvError.setVisibility(View.GONE);

        if (selectedType == null) {
            tvError.setText("Please select an incident type");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        String desc = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";
        if (TextUtils.isEmpty(desc)) {
            tvError.setText("Please add a description");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        // Create and save report
        IncidentReport report = new IncidentReport("", "user_001", selectedType,
                "Sandton Drive, Sandton (simulated)");
        report.setDescription(desc);
        report.setOnRoute(true);
        MockDataRepository.getInstance().addIncidentReport(report);

        // Show success
        Toast.makeText(getContext(), "✅ Report submitted — Under Verification", Toast.LENGTH_LONG).show();

        // Navigate to report history
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(new ReportHistoryFragment(), "reports");
        }
    }
}
