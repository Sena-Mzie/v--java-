package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.Trip;
import com.vuka.app.utils.RiskColorUtils;
import com.vuka.app.utils.TimeUtils;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    public interface OnTripClickListener { void onClick(Trip trip); }

    private final List<Trip> trips;
    private final OnTripClickListener clickListener;

    public TripAdapter(List<Trip> trips, OnTripClickListener clickListener) {
        this.trips = trips;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.tvPickup.setText(trip.getPickupLocation());
        holder.tvDest.setText(trip.getDestination());
        holder.tvTime.setText(TimeUtils.formatTime(trip.getScheduledTime())
                + " · " + (int) trip.getDurationMinutes() + " min");
        holder.tvRisk.setText(RiskColorUtils.getRiskLabel(trip.getRiskLevel()));
        holder.tvRisk.setTextColor(
                RiskColorUtils.getRiskColor(holder.itemView.getContext(), trip.getRiskLevel()));
        holder.tvRisk.setBackground(
                RiskColorUtils.getRiskBackground(holder.itemView.getContext(), trip.getRiskLevel()));

        holder.itemView.setOnClickListener(v -> clickListener.onClick(trip));
    }

    @Override
    public int getItemCount() { return trips.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPickup, tvDest, tvTime, tvRisk;

        ViewHolder(View v) {
            super(v);
            tvPickup = v.findViewById(R.id.tv_trip_pickup);
            tvDest = v.findViewById(R.id.tv_trip_dest);
            tvTime = v.findViewById(R.id.tv_trip_time);
            tvRisk = v.findViewById(R.id.tv_trip_risk);
        }
    }
}
