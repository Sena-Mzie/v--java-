package com.vuka.app.fragments.contacts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vuka.app.R;
import com.vuka.app.models.Trip;
import com.vuka.app.repositories.MockDataRepository;

/**
 * Trusted Contact Live View (Flow 16).
 * Android demo of the browser-based trusted contact experience.
 *
 * NOTE: A full browser implementation requires a separate Java/Spring Boot
 * web module — documented as a separate outstanding deliverable.
 * This screen demonstrates the same data and experience natively.
 */
public class TrustedContactLiveFragment extends Fragment {

    private static final String ARG_SHARE_TOKEN = "share_token";
    private static final String ARG_TRIP_ID = "trip_id";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable etaUpdater;
    private int etaMinutes = 6;

    public static TrustedContactLiveFragment newInstance(String shareToken, String tripId) {
        TrustedContactLiveFragment f = new TrustedContactLiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHARE_TOKEN, shareToken);
        args.putString(ARG_TRIP_ID, tripId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trusted_contact_live, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvStatus = view.findViewById(R.id.tv_live_trip_status);
        TextView tvStatusBadge = view.findViewById(R.id.tv_live_status_badge);
        TextView tvEta = view.findViewById(R.id.tv_live_eta);

        // Check if SOS is active
        if (MockDataRepository.getInstance().isSosActive()) {
            tvStatus.setText("SOS ACTIVE");
            tvStatus.setTextColor(getResources().getColor(R.color.sos_red, null));
            tvStatus.setBackgroundResource(R.drawable.bg_risk_high);
            tvStatusBadge.setText("🔴 SOS ACTIVE");
            tvStatusBadge.setTextColor(getResources().getColor(R.color.sos_red, null));
        }

        // ETA live update
        etaUpdater = new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) return;
                if (etaMinutes > 0) {
                    etaMinutes--;
                    tvEta.setText(etaMinutes + " minutes");
                    handler.postDelayed(this, 15000);
                } else {
                    tvEta.setText("Arrived");
                    tvStatus.setText("Completed");
                }
            }
        };
        handler.postDelayed(etaUpdater, 15000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
