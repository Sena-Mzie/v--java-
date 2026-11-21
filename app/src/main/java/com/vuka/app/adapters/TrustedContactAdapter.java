package com.vuka.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.models.TrustedContact;
import com.vuka.app.utils.RiskColorUtils;

import java.util.List;

public class TrustedContactAdapter extends RecyclerView.Adapter<TrustedContactAdapter.ViewHolder> {

    public interface OnEditListener { void onEdit(TrustedContact contact); }
    public interface OnDeleteListener { void onDelete(TrustedContact contact); }

    private final List<TrustedContact> contacts;
    private final OnEditListener editListener;
    private final OnDeleteListener deleteListener;

    public TrustedContactAdapter(List<TrustedContact> contacts,
                                  OnEditListener editListener,
                                  OnDeleteListener deleteListener) {
        this.contacts = contacts;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrustedContact contact = contacts.get(position);
        holder.tvAvatar.setText(contact.getInitials());
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhoneNumber());

        // Consent status badge
        String status = contact.getConsentStatus();
        holder.tvConsentStatus.setText(capitalize(status));
        int color = RiskColorUtils.getConsentColor(holder.itemView.getContext(), status);
        holder.tvConsentStatus.setTextColor(color);

        switch (status) {
            case TrustedContact.CONSENT_ACCEPTED:
                holder.tvConsentStatus.setBackgroundResource(R.drawable.bg_risk_low);
                break;
            case TrustedContact.CONSENT_DECLINED:
                holder.tvConsentStatus.setBackgroundResource(R.drawable.bg_risk_high);
                break;
            default:
                holder.tvConsentStatus.setBackgroundResource(R.drawable.bg_risk_moderate);
                break;
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(contact));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(contact));
    }

    @Override
    public int getItemCount() { return contacts.size(); }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName, tvPhone, tvConsentStatus;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvAvatar = v.findViewById(R.id.tv_contact_avatar);
            tvName = v.findViewById(R.id.tv_contact_name);
            tvPhone = v.findViewById(R.id.tv_contact_phone);
            tvConsentStatus = v.findViewById(R.id.tv_consent_status);
            btnEdit = v.findViewById(R.id.btn_edit_contact);
            btnDelete = v.findViewById(R.id.btn_delete_contact);
        }
    }
}
