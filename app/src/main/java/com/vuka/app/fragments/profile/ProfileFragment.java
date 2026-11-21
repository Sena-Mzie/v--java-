package com.vuka.app.fragments.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.repositories.MockDataRepository;

/**
 * User Profile screen — bottom nav "Profile" tab.
 * Shows user info, preferences, security settings, and logout.
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VukaApplication app = VukaApplication.getInstance();

        // Populate user info
        String name = app.getUserName();
        String phone = app.getUserPhone();
        String role = app.getUserRole();

        TextView tvAvatar = view.findViewById(R.id.tv_profile_avatar);
        TextView tvName = view.findViewById(R.id.tv_profile_name);
        TextView tvPhone = view.findViewById(R.id.tv_profile_phone);
        TextView tvRole = view.findViewById(R.id.tv_profile_role);

        // Initials for avatar
        if (name != null && !name.isEmpty()) {
            String[] parts = name.trim().split(" ");
            String initials = parts.length >= 2
                    ? String.valueOf(parts[0].charAt(0)).toUpperCase() + String.valueOf(parts[1].charAt(0)).toUpperCase()
                    : String.valueOf(parts[0].charAt(0)).toUpperCase();
            tvAvatar.setText(initials);
        }

        tvName.setText(name != null ? name : "User");
        tvPhone.setText(phone != null && !phone.isEmpty() ? phone : "+27 00 000 0000");
        tvRole.setText(role != null ? capitalize(role) : "Passenger");

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.item_account).setOnClickListener(v ->
                Toast.makeText(getContext(), "Account details — coming soon", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.item_preferences).setOnClickListener(v ->
                Toast.makeText(getContext(), "Preferences — coming soon", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.item_security).setOnClickListener(v ->
                Toast.makeText(getContext(), "Security settings — coming soon", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.item_help).setOnClickListener(v ->
                Toast.makeText(getContext(), "Help & Support — coming soon", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.item_logout).setOnClickListener(v -> confirmLogout());

        view.findViewById(R.id.btn_reset_demo).setOnClickListener(v -> resetDemo());
    }

    private void confirmLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out of VUKA?")
                .setPositiveButton("Logout", (d, w) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        SharedPreferences.Editor editor = VukaApplication.getInstance().getAppPrefs().edit();
        editor.putBoolean(VukaApplication.KEY_IS_LOGGED_IN, false);
        editor.apply();

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_LOGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }

    private void resetDemo() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Reset Demo Data")
                .setMessage("This will reset all demonstration data to defaults. Continue?")
                .setPositiveButton("Reset", (d, w) -> {
                    MockDataRepository.getInstance().reset();
                    VukaApplication.getInstance().resetDemoData();
                    Toast.makeText(getContext(), "Demo data reset", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    if (getActivity() != null) getActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
