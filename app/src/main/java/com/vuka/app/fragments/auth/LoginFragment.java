package com.vuka.app.fragments.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.utils.ValidationUtils;

/**
 * Login / Registration screen (Flow 2).
 * South African mobile number input with OTP flow.
 * Uses mock authentication service for demonstration.
 */
public class LoginFragment extends Fragment {

    private EditText etPhoneNumber;
    private TextView tvPhoneError;
    private String enteredPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        tvPhoneError = view.findViewById(R.id.tv_phone_error);

        Button btnRequestOtp = view.findViewById(R.id.btn_request_otp);
        btnRequestOtp.setOnClickListener(v -> handleRequestOtp());

        // Demo social sign-in buttons (demo only)
        view.findViewById(R.id.btn_google).setOnClickListener(v -> showDemoSocialToast("Google"));
        view.findViewById(R.id.btn_apple).setOnClickListener(v -> showDemoSocialToast("Apple"));
    }

    private void handleRequestOtp() {
        String rawPhone = etPhoneNumber.getText() != null ? etPhoneNumber.getText().toString().trim() : "";

        // Clear previous error
        tvPhoneError.setVisibility(View.GONE);
        tvPhoneError.setText("");

        // Validate phone
        if (TextUtils.isEmpty(rawPhone)) {
            showPhoneError(getString(R.string.required_field));
            return;
        }

        // Check for demo admin phone
        String normalizedPhone = ValidationUtils.normalizePhone(rawPhone);
        boolean isAdmin = VukaApplication.DEMO_ADMIN_PHONE.equals(normalizedPhone);

        if (!isAdmin && !ValidationUtils.isValidSAPhone(rawPhone)) {
            showPhoneError(getString(R.string.invalid_phone));
            return;
        }

        this.enteredPhone = normalizedPhone;

        // Navigate to OTP screen
        OtpVerifyFragment otpFragment = OtpVerifyFragment.newInstance(enteredPhone, isAdmin);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(otpFragment, "otp");
        }
    }

    private void showPhoneError(String message) {
        tvPhoneError.setText(message);
        tvPhoneError.setVisibility(View.VISIBLE);
        etPhoneNumber.requestFocus();
    }

    private void showDemoSocialToast(String provider) {
        Toast.makeText(getContext(),
                "DEMO: " + provider + " sign-in not available in demo mode",
                Toast.LENGTH_SHORT).show();
    }
}
