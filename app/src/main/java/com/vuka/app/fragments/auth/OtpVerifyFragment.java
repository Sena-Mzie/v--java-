package com.vuka.app.fragments.auth;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.vuka.app.fragments.driver.DriverRegistrationFragment;
import com.vuka.app.fragments.passenger.PassengerProfileFragment;

/**
 * OTP verification screen (Flow 2).
 * Six-digit OTP with auto-advance, countdown timer, and resend functionality.
 *
 * DEMO NOTE: The OTP is 123456. No real SMS is sent.
 */
public class OtpVerifyFragment extends Fragment {

    private static final String ARG_PHONE = "phone";
    private static final String ARG_IS_ADMIN = "is_admin";
    private static final int OTP_EXPIRY_SECONDS = 60;

    private EditText[] otpFields;
    private TextView tvOtpError;
    private TextView tvCountdown;
    private Button btnResend;
    private CountDownTimer countDownTimer;

    private String phoneNumber;
    private boolean isAdmin;

    public static OtpVerifyFragment newInstance(String phone, boolean isAdmin) {
        OtpVerifyFragment fragment = new OtpVerifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, phone);
        args.putBoolean(ARG_IS_ADMIN, isAdmin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(ARG_PHONE, "");
            isAdmin = getArguments().getBoolean(ARG_IS_ADMIN, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_verify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update phone display
        TextView tvSentTo = view.findViewById(R.id.tv_otp_sent_to);
        tvSentTo.setText("Enter the 6-digit code sent to\n" + phoneNumber);

        tvOtpError = view.findViewById(R.id.tv_otp_error);
        tvCountdown = view.findViewById(R.id.tv_countdown);
        btnResend = view.findViewById(R.id.btn_resend);

        // Setup OTP fields
        otpFields = new EditText[]{
                view.findViewById(R.id.et_otp_1),
                view.findViewById(R.id.et_otp_2),
                view.findViewById(R.id.et_otp_3),
                view.findViewById(R.id.et_otp_4),
                view.findViewById(R.id.et_otp_5),
                view.findViewById(R.id.et_otp_6)
        };

        setupOtpAutoAdvance();

        view.findViewById(R.id.btn_back_otp).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        view.findViewById(R.id.btn_verify_otp).setOnClickListener(v -> verifyOtp());

        btnResend.setOnClickListener(v -> resendOtp());

        startCountdown();
    }

    private void setupOtpAutoAdvance() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                    // Auto-verify when all filled
                    if (index == otpFields.length - 1 && s.length() == 1) {
                        if (getOtpValue().length() == 6) {
                            verifyOtp();
                        }
                    }
                }
            });
        }
    }

    private String getOtpValue() {
        StringBuilder sb = new StringBuilder();
        for (EditText field : otpFields) {
            if (field.getText() != null) {
                sb.append(field.getText().toString());
            }
        }
        return sb.toString();
    }

    private void verifyOtp() {
        String enteredOtp = getOtpValue();
        tvOtpError.setVisibility(View.GONE);

        if (enteredOtp.length() != 6) {
            tvOtpError.setText("Please enter all 6 digits");
            tvOtpError.setVisibility(View.VISIBLE);
            return;
        }

        // Mock OTP verification — demo OTP is 123456
        if (VukaApplication.DEMO_OTP.equals(enteredOtp)) {
            onVerificationSuccess();
        } else {
            tvOtpError.setText(getString(R.string.invalid_otp));
            tvOtpError.setVisibility(View.VISIBLE);
            clearOtpFields();
        }
    }

    private void onVerificationSuccess() {
        // Persist login state
        SharedPreferences.Editor editor = VukaApplication.getInstance().getAppPrefs().edit();
        editor.putBoolean(VukaApplication.KEY_IS_LOGGED_IN, true);
        editor.putString(VukaApplication.KEY_USER_PHONE, phoneNumber);

        // Set admin role if admin phone
        if (isAdmin) {
            editor.putString(VukaApplication.KEY_USER_ROLE, VukaApplication.ROLE_ADMIN);
            editor.putString(VukaApplication.KEY_USER_NAME, "Admin User");
        } else {
            // Name would come from profile setup
            editor.putString(VukaApplication.KEY_USER_NAME, "User");
        }
        editor.apply();

        // Navigate to profile setup or dashboard
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            String role = VukaApplication.getInstance().getUserRole();

            if (isAdmin) {
                activity.showDashboard();
            } else if (VukaApplication.ROLE_DRIVER.equals(role)) {
                activity.navigateTo(new DriverRegistrationFragment(), "driver_reg");
            } else {
                activity.navigateTo(new PassengerProfileFragment(), "passenger_profile");
            }
        }
    }

    private void clearOtpFields() {
        for (EditText field : otpFields) {
            field.setText("");
        }
        if (otpFields.length > 0) otpFields[0].requestFocus();
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(OTP_EXPIRY_SECONDS * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secs = (int) (millisUntilFinished / 1000);
                tvCountdown.setText(String.format("%02d:%02d", secs / 60, secs % 60));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00");
                btnResend.setEnabled(true);
            }
        };
        countDownTimer.start();
        btnResend.setEnabled(false);
    }

    private void resendOtp() {
        // Simulate resend — reset countdown
        Toast.makeText(getContext(), "DEMO: OTP resent (simulated) — use 123456", Toast.LENGTH_SHORT).show();
        clearOtpFields();
        btnResend.setEnabled(false);
        startCountdown();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
