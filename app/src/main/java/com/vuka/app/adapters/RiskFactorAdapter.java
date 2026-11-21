package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.RiskFactor;

import java.util.List;

public class RiskFactorAdapter extends RecyclerView.Adapter<RiskFactorAdapter.ViewHolder> {

    private final List<RiskFactor> factors;

    public RiskFactorAdapter(List<RiskFactor> factors) {
        this.factors = factors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_risk_factor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiskFactor factor = factors.get(position);
        holder.tvType.setText(factor.getType());
        holder.tvConfidence.setText(factor.getConfidence() + " confidence");
        holder.tvDate.setText(factor.getDate());
        holder.tvExplanation.setText(factor.getExplanation());
    }

    @Override
    public int getItemCount() { return factors.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvConfidence, tvDate, tvExplanation;

        ViewHolder(View v) {
            super(v);
            tvType = v.findViewById(R.id.tv_factor_type);
            tvConfidence = v.findViewById(R.id.tv_factor_confidence);
            tvDate = v.findViewById(R.id.tv_factor_date);
            tvExplanation = v.findViewById(R.id.tv_factor_explanation);
        }
    }
}
