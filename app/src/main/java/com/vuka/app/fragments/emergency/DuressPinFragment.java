package com.vuka.app.fragments.emergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.activities.MainActivity;
import com.vuka.app.activities.TripMonitorActivity;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Duress PIN / Safety Check-In screen (Flow 12).
 *
 * Normal PIN (1234): Successful check-in, continue trip.
 * Duress PIN (9999): Appears as normal success but triggers silent simulated escalation.
 *
 * SECURITY NOTES:
 * - Entered PINs are NEVER logged.
 * - PINs are compared in-memory only.
 * - Duress state is not visible in the normal UI.
 * - In production: PINs must be hashed and stored securely.
 */
public class DuressPinFragment extends Fragment {

    // Demo PINs — never log these
    private static final String NORMAL_PIN = "1234";
    private static final String DURESS_PIN = "9999";

    private final StringBuilder enteredPin = new StringBuilder();
    private final View[] pinDots = new View[4];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_duress_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pinDots[0] = view.findViewById(R.id.pin_dot_1);
        pinDots[1] = view.findViewById(R.id.pin_dot_2);
        pinDots[2] = view.findViewById(R.id.pin_dot_3);
        pinDots[3] = view.findViewById(R.id.pin_dot_4);

        // Numpad buttons
        int[] numBtnIds = {R.id.pin_0, R.id.pin_1, R.id.pin_2, R.id.pin_3, R.id.pin_4,
                R.id.pin_5, R.id.pin_6, R.id.pin_7, R.id.pin_8, R.id.pin_9};
        String[] digits = {"0","1","2","3","4","5","6","7","8","9"};

        for (int i = 0; i < numBtnIds.length; i++) {
            final String digit = digits[i];
            view.findViewById(numBtnIds[i]).setOnClickListener(v -> appendDigit(digit));
        }

        view.findViewById(R.id.pin_delete).setOnClickListener(v -> deleteDigit());
    }

    private void appendDigit(String digit) {
        if (enteredPin.length() >= 4) return;
        enteredPin.append(digit);
        updateDotDisplay();

        if (enteredPin.length() == 4) {
            processPin();
        }
    }

    private void deleteDigit() {
        if (enteredPin.length() > 0) {
            enteredPin.deleteCharAt(enteredPin.length() - 1);
            updateDotDisplay();
        }
    }

    private void updateDotDisplay() {
        if (getContext() == null) return;
        for (int i = 0; i < pinDots.length; i++) {
            if (pinDots[i] != null) {
                pinDots[i].setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                i < enteredPin.length()
                                        ? ContextCompat.getColor(requireContext(), R.color.vuka_gold)
                                        : ContextCompat.getColor(requireContext(), R.color.card_border)
                        )
                );
            }
        }
    }

    private void processPin() {
        String pin = enteredPin.toString();
        // Clear immediately — NEVER store or log the PIN
        enteredPin.setLength(0);
        updateDotDisplay();

        if (DURESS_PIN.equals(pin)) {
            // Silent duress escalation — normal-looking UI response
            MockDataRepository.getInstance().activateDuress();
            showNormalCheckInSuccess(); // Appear normal to prevent attacker noticing
        } else if (NORMAL_PIN.equals(pin)) {
            showNormalCheckInSuccess();
        } else {
            Toast.makeText(getContext(), "Incorrect PIN — please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNormalCheckInSuccess() {
        // Both normal and duress PIN show this — duress runs silent escalation in background
        Toast.makeText(getContext(), "✅ Check-in confirmed — monitoring continues", Toast.LENGTH_SHORT).show();
        if (getActivity() instanceof TripMonitorActivity) {
            getActivity().onBackPressed();
        } else if (getActivity() instanceof MainActivity) {
            getActivity().onBackPressed();
        }
    }
}
