package com.vuka.app.fragments.incidents;

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
import com.vuka.app.adapters.IncidentReportAdapter;
import com.vuka.app.models.IncidentReport;
import com.vuka.app.repositories.MockDataRepository;

import java.util.List;

/**
 * Report History screen — bottom nav "Reports" tab.
 * Shows all submitted incident reports with status.
 */
public class ReportHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_back_reports).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_new_report).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(new ReportIncidentFragment(), "report_incident");
            }
        });

        List<IncidentReport> reports = MockDataRepository.getInstance().getIncidentReports();

        RecyclerView rv = view.findViewById(R.id.rv_reports);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new IncidentReportAdapter(reports));

        view.findViewById(R.id.ll_empty_reports).setVisibility(
                reports.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setVisibility(reports.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
