package com.vuka.app.fragments.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.adapters.AdminReportAdapter;
import com.vuka.app.adapters.AuditLogAdapter;
import com.vuka.app.models.IncidentReport;
import com.vuka.app.repositories.MockDataRepository;

import java.util.List;

/**
 * Admin Dashboard (Flow 17).
 * KPIs, report moderation, driver verification queue, and audit logs.
 * Restricted to admin role — enforced at activity level.
 */
public class AdminDashboardFragment extends Fragment {

    private AdminReportAdapter reportAdapter;
    private RecyclerView rvReports;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateKpis(view);
        setupReportsList(view);
        setupAuditLogs(view);
        setupFilterChips(view);

        view.findViewById(R.id.btn_admin_logout).setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Log out of admin dashboard?")
                    .setPositiveButton("Logout", (d, w) -> performLogout())
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void populateKpis(View view) {
        MockDataRepository repo = MockDataRepository.getInstance();
        ((TextView) view.findViewById(R.id.tv_kpi_active_trips))
                .setText(String.valueOf(repo.getTrips().size()));
        ((TextView) view.findViewById(R.id.tv_kpi_pending_reports))
                .setText(String.valueOf(countByStatus(repo.getIncidentReports(), IncidentReport.STATUS_SUBMITTED)));
        ((TextView) view.findViewById(R.id.tv_kpi_emergency))
                .setText(repo.isSosActive() ? "1" : "0");
        ((TextView) view.findViewById(R.id.tv_kpi_hotspots)).setText("8");
    }

    private int countByStatus(List<IncidentReport> reports, String status) {
        int count = 0;
        for (IncidentReport r : reports) {
            if (status.equals(r.getStatus())) count++;
        }
        return count;
    }

    private void setupReportsList(View view) {
        rvReports = view.findViewById(R.id.rv_admin_reports);
        rvReports.setLayoutManager(new LinearLayoutManager(getContext()));
        loadReports(null);
    }

    private void loadReports(@Nullable String statusFilter) {
        List<IncidentReport> reports = MockDataRepository.getInstance().getIncidentReports();
        if (statusFilter != null && !statusFilter.isEmpty()) {
            reports.removeIf(r -> !statusFilter.equals(r.getStatus()));
        }
        reportAdapter = new AdminReportAdapter(reports, this::onAdminAction);
        rvReports.setAdapter(reportAdapter);
    }

    private void onAdminAction(String action, String reportId) {
        String newStatus;
        switch (action) {
            case "approve": newStatus = IncidentReport.STATUS_VERIFIED; break;
            case "reject": newStatus = IncidentReport.STATUS_REJECTED; break;
            default: newStatus = IncidentReport.STATUS_UNDER_REVIEW; break;
        }
        MockDataRepository.getInstance().updateReportStatus(reportId, newStatus);
        loadReports(null); // Refresh
        Toast.makeText(getContext(), "Report " + action + "d", Toast.LENGTH_SHORT).show();
        // Refresh KPIs
        if (getView() != null) populateKpis(getView());
    }

    private void setupAuditLogs(View view) {
        RecyclerView rvLogs = view.findViewById(R.id.rv_audit_logs);
        rvLogs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLogs.setAdapter(new AuditLogAdapter(MockDataRepository.getInstance().getAuditLogs()));
    }

    private void setupFilterChips(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_filter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                loadReports(null);
                return;
            }
            Chip chip = group.findViewById(checkedIds.get(0));
            if (chip == null) { loadReports(null); return; }
            String text = chip.getText().toString();
            switch (text) {
                case "New": loadReports(IncidentReport.STATUS_SUBMITTED); break;
                case "Reviewed": loadReports(IncidentReport.STATUS_UNDER_REVIEW); break;
                case "Verified": loadReports(IncidentReport.STATUS_VERIFIED); break;
                default: loadReports(null); break;
            }
        });
    }

    private void performLogout() {
        android.content.SharedPreferences.Editor editor =
                com.vuka.app.VukaApplication.getInstance().getAppPrefs().edit();
        editor.putBoolean(com.vuka.app.VukaApplication.KEY_IS_LOGGED_IN, false);
        editor.apply();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_LOGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }
}
