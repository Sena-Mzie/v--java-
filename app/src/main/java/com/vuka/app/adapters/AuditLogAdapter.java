package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.models.AuditLog;
import com.vuka.app.utils.TimeUtils;

import java.util.List;

public class AuditLogAdapter extends RecyclerView.Adapter<AuditLogAdapter.ViewHolder> {

    private final List<AuditLog> logs;

    public AuditLogAdapter(List<AuditLog> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Simple text-based row using alert layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.two_line_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AuditLog log = logs.get(position);
        holder.tvLine1.setText(log.getAction() + " — " + log.getEntityType() + ": " + log.getEntityId());
        holder.tvLine2.setText(TimeUtils.formatDateTime(log.getTimestamp()) + " · " + log.getDetails());
        holder.tvLine1.setTextColor(0xFFB0B8D4);
        holder.tvLine2.setTextColor(0xFF6B7399);
    }

    @Override
    public int getItemCount() { return logs.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLine1, tvLine2;

        ViewHolder(View v) {
            super(v);
            tvLine1 = v.findViewById(android.R.id.text1);
            tvLine2 = v.findViewById(android.R.id.text2);
        }
    }
}
