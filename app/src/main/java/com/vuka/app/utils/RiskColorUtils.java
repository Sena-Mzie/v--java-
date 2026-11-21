package com.vuka.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.vuka.app.R;
import com.vuka.app.models.Trip;

/**
 * Utility for applying consistent risk-level colors throughout the app.
 * Color is always used together with text labels — never color alone.
 */
public class RiskColorUtils {

    private RiskColorUtils() {}

    public static int getRiskColor(Context context, String riskLevel) {
        if (riskLevel == null) return ContextCompat.getColor(context, R.color.risk_low);
        switch (riskLevel.toLowerCase()) {
            case Trip.RISK_HIGH:
                return ContextCompat.getColor(context, R.color.risk_high);
            case Trip.RISK_ELEVATED:
                return ContextCompat.getColor(context, R.color.risk_elevated);
            case Trip.RISK_MODERATE:
                return ContextCompat.getColor(context, R.color.risk_moderate);
            default:
                return ContextCompat.getColor(context, R.color.risk_low);
        }
    }

    public static Drawable getRiskBackground(Context context, String riskLevel) {
        int drawableRes;
        if (riskLevel == null) {
            drawableRes = R.drawable.bg_risk_low;
        } else {
            switch (riskLevel.toLowerCase()) {
                case Trip.RISK_HIGH: drawableRes = R.drawable.bg_risk_high; break;
                case Trip.RISK_ELEVATED: drawableRes = R.drawable.bg_risk_elevated; break;
                case Trip.RISK_MODERATE: drawableRes = R.drawable.bg_risk_moderate; break;
                default: drawableRes = R.drawable.bg_risk_low; break;
            }
        }
        return ContextCompat.getDrawable(context, drawableRes);
    }

    public static String getRiskLabel(String riskLevel) {
        if (riskLevel == null) return "LOW";
        switch (riskLevel.toLowerCase()) {
            case Trip.RISK_HIGH: return "HIGH";
            case Trip.RISK_ELEVATED: return "ELEVATED";
            case Trip.RISK_MODERATE: return "MODERATE";
            default: return "LOW";
        }
    }

    public static String getRiskIcon(String riskLevel) {
        if (riskLevel == null) return "🟢";
        switch (riskLevel.toLowerCase()) {
            case Trip.RISK_HIGH: return "🔴";
            case Trip.RISK_ELEVATED: return "🟠";
            case Trip.RISK_MODERATE: return "🟡";
            default: return "🟢";
        }
    }

    public static int getConsentColor(Context context, String consentStatus) {
        if (consentStatus == null) return ContextCompat.getColor(context, R.color.consent_pending);
        switch (consentStatus) {
            case "accepted": return ContextCompat.getColor(context, R.color.consent_accepted);
            case "declined": return ContextCompat.getColor(context, R.color.consent_declined);
            default: return ContextCompat.getColor(context, R.color.consent_pending);
        }
    }
}
