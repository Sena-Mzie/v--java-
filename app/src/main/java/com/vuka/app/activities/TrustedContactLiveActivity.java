package com.vuka.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vuka.app.R;
import com.vuka.app.fragments.contacts.TrustedContactLiveFragment;
import com.vuka.app.navigation.NavigationManager;

/**
 * Trusted Contact Live View activity.
 * Provides a secure demonstration view of an active trip for trusted contacts.
 *
 * This is the Android native demonstration of the browser-based trusted contact
 * experience. A full Java web module (Spring Boot) would be a separate deliverable.
 *
 * Access is controlled via a demo trip share token.
 */
public class TrustedContactLiveActivity extends AppCompatActivity {

    public static final String EXTRA_SHARE_TOKEN = "share_token";
    public static final String EXTRA_TRIP_ID = "trip_id";

    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // No bottom nav for contact view
        findViewById(R.id.bottom_navigation).setVisibility(android.view.View.GONE);
        findViewById(R.id.nav_divider).setVisibility(android.view.View.GONE);

        navigationManager = new NavigationManager(
                getSupportFragmentManager(),
                R.id.fragment_container
        );

        String shareToken = getIntent().getStringExtra(EXTRA_SHARE_TOKEN);
        String tripId = getIntent().getStringExtra(EXTRA_TRIP_ID);

        TrustedContactLiveFragment fragment = TrustedContactLiveFragment.newInstance(shareToken, tripId);
        navigationManager.navigateToRoot(fragment, "tc_live");
    }
}
