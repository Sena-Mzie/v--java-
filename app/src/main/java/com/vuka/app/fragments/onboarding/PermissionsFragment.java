package com.vuka.app.fragments.onboarding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Permissions introduction screen (Flow 1).
 * Explains why each permission is needed before requesting it.
 * Provides limited-functionality skip option.
 */
public class PermissionsFragment extends Fragment {

    private ImageView ivLocationCheck;
    private ImageView ivNotifCheck;
    private ImageView ivSmsCheck;
    private ImageView ivCameraCheck;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::handlePermissionResults);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivLocationCheck = view.findViewById(R.id.iv_location_check);
        ivNotifCheck = view.findViewById(R.id.iv_notif_check);
        ivSmsCheck = view.findViewById(R.id.iv_sms_check);
        ivCameraCheck = view.findViewById(R.id.iv_camera_check);

        // Update initial permission state display
        updatePermissionStates();

        view.findViewById(R.id.btn_allow_permissions).setOnClickListener(v -> requestPermissions());
        view.findViewById(R.id.btn_skip_permissions).setOnClickListener(v -> proceedToRoleSelection());
    }

    private void requestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();

        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!hasPermission(Manifest.permission.CAMERA)) {
            permissionsToRequest.add(Manifest.permission.CAMERA);
        }
        if (!hasPermission(Manifest.permission.SEND_SMS)) {
            permissionsToRequest.add(Manifest.permission.SEND_SMS);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (!hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (permissionsToRequest.isEmpty()) {
            proceedToRoleSelection();
            return;
        }

        permissionLauncher.launch(permissionsToRequest.toArray(new String[0]));
    }

    private void handlePermissionResults(Map<String, Boolean> results) {
        updatePermissionStates();
        // Proceed regardless of which were granted — app works in limited mode
        proceedToRoleSelection();
    }

    private void updatePermissionStates() {
        if (getContext() == null) return;

        boolean hasLocation = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        boolean hasCamera = hasPermission(Manifest.permission.CAMERA);
        boolean hasSms = hasPermission(Manifest.permission.SEND_SMS);
        boolean hasNotif = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU
                || hasPermission(Manifest.permission.POST_NOTIFICATIONS);

        setPermissionIcon(ivLocationCheck, hasLocation);
        setPermissionIcon(ivNotifCheck, hasNotif);
        setPermissionIcon(ivSmsCheck, hasSms);
        setPermissionIcon(ivCameraCheck, hasCamera);
    }

    private void setPermissionIcon(ImageView iv, boolean granted) {
        if (iv == null || getContext() == null) return;
        if (granted) {
            iv.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    android.R.drawable.checkbox_on_background));
            iv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.risk_low));
        } else {
            iv.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    android.R.drawable.checkbox_off_background));
            iv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.vuka_text_hint));
        }
    }

    private boolean hasPermission(String permission) {
        return getContext() != null &&
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void proceedToRoleSelection() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(new RoleSelectionFragment(), "role_select");
        }
    }
}
