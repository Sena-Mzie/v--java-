package com.vuka.app;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * VUKA Application class — initialized at startup.
 * Provides application-level context and shared preferences initialization.
 */
public class VukaApplication extends Application {

    public static final String PREFS_NAME = "VukaPrefs";
    public static final String KEY_USER_ROLE = "user_role";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PHONE = "user_phone";
    public static final String KEY_ONBOARDING_DONE = "onboarding_done";
    public static final String KEY_PIN_HASH = "pin_hash";
    public static final String KEY_DURESS_PIN_HASH = "duress_pin_hash";

    public static final String ROLE_DRIVER = "driver";
    public static final String ROLE_PASSENGER = "passenger";
    public static final String ROLE_ADMIN = "admin";

    // Demo admin credentials (demo only — not for production)
    public static final String DEMO_ADMIN_PHONE = "+27800000001";
    public static final String DEMO_OTP = "123456";

    private static VukaApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static VukaApplication getInstance() {
        return instance;
    }

    public SharedPreferences getAppPrefs() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return getAppPrefs().getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserRole() {
        return getAppPrefs().getString(KEY_USER_ROLE, ROLE_PASSENGER);
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(getUserRole());
    }

    public String getUserName() {
        return getAppPrefs().getString(KEY_USER_NAME, "User");
    }

    public String getUserPhone() {
        return getAppPrefs().getString(KEY_USER_PHONE, "");
    }

    public boolean isOnboardingDone() {
        return getAppPrefs().getBoolean(KEY_ONBOARDING_DONE, false);
    }

    /**
     * Reset all demonstration data back to defaults.
     * Accessible from the Profile screen.
     */
    public void resetDemoData() {
        SharedPreferences.Editor editor = getAppPrefs().edit();
        editor.clear();
        editor.apply();
    }
}
