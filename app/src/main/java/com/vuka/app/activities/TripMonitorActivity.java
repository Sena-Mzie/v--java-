package com.vuka.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vuka.app.R;
import com.vuka.app.fragments.trips.ActiveMonitoringFragment;
import com.vuka.app.navigation.NavigationManager;

/**
 * Full-screen trip monitoring activity.
 * Displays active trip map (simulation), driver info, and emergency controls.
 */
public class TripMonitorActivity extends AppCompatActivity {

    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // No bottom nav during active trip monitoring
        findViewById(R.id.bottom_navigation).setVisibility(android.view.View.GONE);
        findViewById(R.id.nav_divider).setVisibility(android.view.View.GONE);

        navigationManager = new NavigationManager(
                getSupportFragmentManager(),
                R.id.fragment_container
        );

        navigationManager.navigateToRoot(new ActiveMonitoringFragment(), "monitoring");
    }

    public void navigateTo(androidx.fragment.app.Fragment fragment, String tag) {
        navigationManager.navigateTo(fragment, tag);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
