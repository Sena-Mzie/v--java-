package com.vuka.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vuka.app.R;
import com.vuka.app.VukaApplication;

/**
 * VUKA Splash screen.
 * Displays brand identity and transitions to onboarding or dashboard.
 * Auto-advances after 2.5 seconds.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.iv_vuka_logo);
        TextView appName = findViewById(R.id.tv_app_name);
        TextView tagline = findViewById(R.id.tv_tagline);
        TextView fullTagline = findViewById(R.id.tv_full_tagline);

        // Fade-in animation for logo and name
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1000);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);

        AlphaAnimation fadeInDelayed = new AlphaAnimation(0f, 1f);
        fadeInDelayed.setStartOffset(500);
        fadeInDelayed.setDuration(800);
        tagline.startAnimation(fadeInDelayed);
        fullTagline.startAnimation(fadeInDelayed);

        // Auto-advance
        new Handler(Looper.getMainLooper()).postDelayed(this::proceedToNextScreen, SPLASH_DURATION_MS);
    }

    private void proceedToNextScreen() {
        VukaApplication app = VukaApplication.getInstance();
        Intent intent;

        if (!app.isOnboardingDone()) {
            // First time — go to main for onboarding flow
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_PERMISSIONS);
        } else if (!app.isLoggedIn()) {
            // Onboarding done but not logged in
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_LOGIN);
        } else if (app.isAdmin()) {
            // Admin user
            intent = new Intent(this, AdminActivity.class);
        } else {
            // Regular user — go to dashboard
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_START_SCREEN, MainActivity.SCREEN_DASHBOARD);
        }

        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
