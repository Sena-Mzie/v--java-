package com.vuka.app.fragments.contacts;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.adapters.TrustedContactAdapter;
import com.vuka.app.models.TrustedContact;
import com.vuka.app.repositories.MockDataRepository;

import java.util.List;

/**
 * Trusted Contacts management screen (Flow 5).
 * Add, edit, remove contacts and view consent status.
 */
public class TrustedContactsFragment extends Fragment {

    private TrustedContactAdapter contactAdapter;
    private RecyclerView rvContacts;
    private View llEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trusted_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvContacts = view.findViewById(R.id.rv_contacts);
        llEmpty = view.findViewById(R.id.ll_empty_contacts);

        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        loadContacts();

        view.findViewById(R.id.btn_back_contacts).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_add_contact).setOnClickListener(v -> showAddContactDialog(null));
        view.findViewById(R.id.btn_add_contact_bottom).setOnClickListener(v -> showAddContactDialog(null));
    }

    private void loadContacts() {
        List<TrustedContact> contacts = MockDataRepository.getInstance().getTrustedContacts();

        if (contacts.isEmpty()) {
            rvContacts.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        } else {
            rvContacts.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);

            contactAdapter = new TrustedContactAdapter(contacts,
                    contact -> showAddContactDialog(contact),   // edit
                    contact -> confirmRemoveContact(contact)    // delete
            );
            rvContacts.setAdapter(contactAdapter);
        }
    }

    private void showAddContactDialog(@Nullable TrustedContact existing) {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_contact);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etName = dialog.findViewById(R.id.et_dialog_name);
        EditText etPhone = dialog.findViewById(R.id.et_dialog_phone);
        EditText etRelationship = dialog.findViewById(R.id.et_dialog_relationship);

        // Pre-fill if editing
        if (existing != null) {
            etName.setText(existing.getName());
            etPhone.setText(existing.getPhoneNumber());
            etRelationship.setText(existing.getRelationship());
        }

        dialog.findViewById(R.id.btn_dialog_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btn_dialog_save).setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
            String rel = etRelationship.getText() != null ? etRelationship.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                etPhone.setError("Phone number is required");
                return;
            }

            if (existing != null) {
                // Update existing
                existing.setName(name);
                existing.setPhoneNumber(phone);
                existing.setRelationship(rel);
                MockDataRepository.getInstance().updateTrustedContact(existing);
                Toast.makeText(getContext(), "Contact updated", Toast.LENGTH_SHORT).show();
            } else {
                // Add new
                TrustedContact newContact = new TrustedContact(
                        "", "user_001", name, phone, rel);
                MockDataRepository.getInstance().addTrustedContact(newContact);
                Toast.makeText(getContext(),
                        "Contact added — DEMO: Consent request simulated",
                        Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadContacts();
        });

        dialog.show();
    }

    private void confirmRemoveContact(TrustedContact contact) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Remove Contact")
                .setMessage("Remove " + contact.getName() + " from your trusted contacts?")
                .setPositiveButton("Remove", (d, w) -> {
                    MockDataRepository.getInstance().removeTrustedContact(contact.getId());
                    loadContacts();
                    Toast.makeText(getContext(), "Contact removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
