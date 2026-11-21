package com.vuka.app.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.fragments.admin.AdminDashboardFragment;
import com.vuka.app.navigation.NavigationManager;

/**
 * Admin activity — restricted to administrator demo account.
 * Role restriction enforced at login and here as a safety check.
 */
public class AdminActivity extends AppCompatActivity {

    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Role gate: only admin may access this activity
        if (!VukaApplication.getInstance().isAdmin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Hide bottom nav (admin uses its own navigation)
        findViewById(R.id.bottom_navigation).setVisibility(android.view.View.GONE);
        findViewById(R.id.nav_divider).setVisibility(android.view.View.GONE);

        navigationManager = new NavigationManager(
                getSupportFragmentManager(),
                R.id.fragment_container
        );

        navigationManager.navigateToRoot(new AdminDashboardFragment(), "admin_dashboard");
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
