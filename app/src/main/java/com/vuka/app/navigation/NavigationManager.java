package com.vuka.app.navigation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vuka.app.R;

/**
 * Centralized navigation manager for fragment-based navigation.
 * Provides consistent back-stack management and transitions.
 */
public class NavigationManager {

    private final FragmentManager fragmentManager;
    private final int containerId;

    public NavigationManager(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    /**
     * Navigate to a fragment, adding it to the back stack.
     */
    public void navigateTo(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        transaction.replace(containerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Navigate to a fragment as the root (clears back stack).
     */
    public void navigateToRoot(Fragment fragment, String tag) {
        // Clear all back stack entries
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(containerId, fragment, tag);
        transaction.commit();
    }

    /**
     * Go back to the previous fragment.
     */
    public boolean goBack() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    /**
     * Navigate to a fragment without adding to back stack.
     */
    public void switchTab(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment, tag);
        transaction.commit();
    }
}
