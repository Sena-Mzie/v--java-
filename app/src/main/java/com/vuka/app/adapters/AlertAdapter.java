package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.Alert;
import com.vuka.app.utils.RiskColorUtils;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private final List<Alert> alerts;

    public AlertAdapter(List<Alert> alerts) {
        this.alerts = alerts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        holder.tvIcon.setText(alert.getSeverityIcon());
        holder.tvTitle.setText(alert.getTitle());
        holder.tvDesc.setText(alert.getDescription());
        holder.tvTime.setText(alert.getDistanceOrTime());

        int color = RiskColorUtils.getRiskColor(holder.itemView.getContext(), alert.getSeverity());
        holder.tvIcon.setTextColor(color);

        // Mark as read when displayed
        alert.setRead(true);
    }

    @Override
    public int getItemCount() { return Math.min(alerts.size(), 3); } // Show up to 3

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvTitle, tvDesc, tvTime;

        ViewHolder(View v) {
            super(v);
            tvIcon = v.findViewById(R.id.tv_alert_icon);
            tvTitle = v.findViewById(R.id.tv_alert_title);
            tvDesc = v.findViewById(R.id.tv_alert_desc);
            tvTime = v.findViewById(R.id.tv_alert_time);
        }
    }
}
