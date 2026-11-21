package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.IncidentReport;

import java.util.List;

public class AdminReportAdapter extends RecyclerView.Adapter<AdminReportAdapter.ViewHolder> {

    public interface OnAdminActionListener {
        void onAction(String action, String reportId);
    }

    private final List<IncidentReport> reports;
    private final OnAdminActionListener listener;

    public AdminReportAdapter(List<IncidentReport> reports, OnAdminActionListener listener) {
        this.reports = reports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncidentReport report = reports.get(position);
        holder.tvId.setText(report.getId());
        holder.tvStatus.setText(report.getStatusDisplayText());
        holder.tvType.setText(report.getIncidentType());
        holder.tvDesc.setText(report.getDescription() != null ? report.getDescription() : "No description provided");

        holder.btnApprove.setOnClickListener(v -> listener.onAction("approve", report.getId()));
        holder.btnReject.setOnClickListener(v -> listener.onAction("reject", report.getId()));
        holder.btnEscalate.setOnClickListener(v -> listener.onAction("escalate", report.getId()));
    }

    @Override
    public int getItemCount() { return reports.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvType, tvDesc;
        Button btnApprove, btnReject, btnEscalate;

        ViewHolder(View v) {
            super(v);
            tvId = v.findViewById(R.id.tv_admin_report_id);
            tvStatus = v.findViewById(R.id.tv_admin_report_status);
            tvType = v.findViewById(R.id.tv_admin_report_type);
            tvDesc = v.findViewById(R.id.tv_admin_report_desc);
            btnApprove = v.findViewById(R.id.btn_admin_approve);
            btnReject = v.findViewById(R.id.btn_admin_reject);
            btnEscalate = v.findViewById(R.id.btn_admin_escalate);
        }
    }
}
