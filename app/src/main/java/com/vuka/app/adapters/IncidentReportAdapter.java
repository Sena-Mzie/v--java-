package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.IncidentReport;

import java.util.List;

public class IncidentReportAdapter extends RecyclerView.Adapter<IncidentReportAdapter.ViewHolder> {

    private final List<IncidentReport> reports;

    public IncidentReportAdapter(List<IncidentReport> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incident_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncidentReport report = reports.get(position);
        holder.tvId.setText(report.getId());
        holder.tvStatus.setText(report.getStatusDisplayText());
        holder.tvType.setText(report.getIncidentType());
        holder.tvLocation.setText("📍 " + report.getLocationName());

        // Status badge styling
        switch (report.getStatus()) {
            case IncidentReport.STATUS_VERIFIED:
                holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_verified));
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_verified);
                break;
            case IncidentReport.STATUS_REJECTED:
                holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_rejected));
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rejected);
                break;
            default:
                holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending));
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                break;
        }
    }

    @Override
    public int getItemCount() { return reports.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvType, tvLocation;

        ViewHolder(View v) {
            super(v);
            tvId = v.findViewById(R.id.tv_report_id);
            tvStatus = v.findViewById(R.id.tv_report_status);
            tvType = v.findViewById(R.id.tv_report_type);
            tvLocation = v.findViewById(R.id.tv_report_location);
        }
    }
}
