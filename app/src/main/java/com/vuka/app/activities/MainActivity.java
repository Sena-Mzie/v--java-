package com.vuka.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vuka.app.R;
import com.vuka.app.VukaApplication;
import com.vuka.app.fragments.admin.AdminDashboardFragment;
import com.vuka.app.fragments.contacts.TrustedContactsFragment;
import com.vuka.app.fragments.dashboard.DriverDashboardFragment;
import com.vuka.app.fragments.dashboard.PassengerDashboardFragment;
import com.vuka.app.fragments.incidents.ReportHistoryFragment;
import com.vuka.app.fragments.onboarding.PermissionsFragment;
import com.vuka.app.fragments.onboarding.RoleSelectionFragment;
import com.vuka.app.fragments.auth.LoginFragment;
import com.vuka.app.fragments.auth.OtpVerifyFragment;
import com.vuka.app.fragments.profile.ProfileFragment;
import com.vuka.app.fragments.trips.TripHistoryFragment;
import com.vuka.app.navigation.NavigationManager;

/**
 * Main activity — hosts the fragment navigation for the entire app.
 * Acts as the navigation hub after splash screen.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_START_SCREEN = "start_screen";
    public static final String SCREEN_PERMISSIONS = "permissions";
    public static final String SCREEN_ROLE_SELECT = "role_select";
    public static final String SCREEN_LOGIN = "login";
    public static final String SCREEN_OTP = "otp";
    public static final String SCREEN_DASHBOARD = "dashboard";
    public static final String SCREEN_ADMIN = "admin";

    private NavigationManager navigationManager;
    private BottomNavigationView bottomNavigation;
    private String currentRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        navigationManager = new NavigationManager(
                getSupportFragmentManager(),
                R.id.fragment_container
        );

        currentRole = VukaApplication.getInstance().getUserRole();
        setupBottomNavigation();

        // Determine start screen
        String startScreen = getIntent().getStringExtra(EXTRA_START_SCREEN);
        if (startScreen == null) startScreen = SCREEN_DASHBOARD;

        navigateToStartScreen(startScreen);
    }

    private void navigateToStartScreen(String screen) {
        switch (screen) {
            case SCREEN_PERMISSIONS:
                navigateTo(new PermissionsFragment(), "permissions");
                hideBottomNav();
                break;
            case SCREEN_ROLE_SELECT:
                navigateTo(new RoleSelectionFragment(), "role_select");
                hideBottomNav();
                break;
            case SCREEN_LOGIN:
                navigateTo(new LoginFragment(), "login");
                hideBottomNav();
                break;
            case SCREEN_OTP:
                navigateTo(new OtpVerifyFragment(), "otp");
                hideBottomNav();
                break;
            case SCREEN_DASHBOARD:
            default:
                showDashboard();
                break;
        }
    }

    private void setupBottomNavigation() {
        // Set menu based on role
        if (VukaApplication.ROLE_DRIVER.equals(currentRole)) {
            bottomNavigation.inflateMenu(R.menu.bottom_nav_driver);
        } else {
            bottomNavigation.inflateMenu(R.menu.bottom_nav_passenger);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showDashboard();
                return true;
            } else if (id == R.id.nav_trips) {
                navigateTo(new TripHistoryFragment(), "trips");
                return true;
            } else if (id == R.id.nav_reports) {
                navigateTo(new ReportHistoryFragment(), "reports");
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(new ProfileFragment(), "profile");
                return true;
            }
            return false;
        });
    }

    public void showDashboard() {
        Fragment dashboard;
        currentRole = VukaApplication.getInstance().getUserRole();

        if (VukaApplication.ROLE_DRIVER.equals(currentRole)) {
            dashboard = new DriverDashboardFragment();
        } else if (VukaApplication.ROLE_ADMIN.equals(currentRole)) {
            startActivity(new Intent(this, AdminActivity.class));
            return;
        } else {
            dashboard = new PassengerDashboardFragment();
        }

        navigationManager.navigateToRoot(dashboard, "dashboard");
        showBottomNav();
    }

    public void navigateTo(Fragment fragment, String tag) {
        navigationManager.navigateTo(fragment, tag);
    }

    public void navigateToRoot(Fragment fragment, String tag) {
        navigationManager.navigateToRoot(fragment, tag);
    }

    public void showBottomNav() {
        bottomNavigation.setVisibility(View.VISIBLE);
        findViewById(R.id.nav_divider).setVisibility(View.VISIBLE);
    }

    public void hideBottomNav() {
        bottomNavigation.setVisibility(View.GONE);
        findViewById(R.id.nav_divider).setVisibility(View.GONE);
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
