package com.vuka.app.models;

/**
 * Represents a VUKA safety trip.
 * Tracks the journey from creation through monitoring to completion.
 */
public class Trip {
    private String id;
    private String userId;
    private String pickupLocation;
    private String destination;
    private long scheduledTime;
    private long startTime;
    private long endTime;
    private String status; // "created", "active", "completed", "cancelled", "emergency"
    private int overallRiskScore;
    private String riskLevel; // "low", "moderate", "elevated", "high"
    private boolean saferRouteSelected;
    private boolean deviationDetected;
    private boolean emergencyActivated;
    private String shareToken;
    private double durationMinutes;
    private double distanceKm;
    private int safetyScore;

    public static final String STATUS_CREATED = "created";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_EMERGENCY = "emergency";

    public static final String RISK_LOW = "low";
    public static final String RISK_MODERATE = "moderate";
    public static final String RISK_ELEVATED = "elevated";
    public static final String RISK_HIGH = "high";

    public Trip() {}

    public Trip(String id, String userId, String pickupLocation, String destination) {
        this.id = id;
        this.userId = userId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.status = STATUS_CREATED;
        this.scheduledTime = System.currentTimeMillis();
        this.shareToken = "VUKA_SHARE_" + id;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public long getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getOverallRiskScore() { return overallRiskScore; }
    public void setOverallRiskScore(int overallRiskScore) { this.overallRiskScore = overallRiskScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public boolean isSaferRouteSelected() { return saferRouteSelected; }
    public void setSaferRouteSelected(boolean saferRouteSelected) { this.saferRouteSelected = saferRouteSelected; }

    public boolean isDeviationDetected() { return deviationDetected; }
    public void setDeviationDetected(boolean deviationDetected) { this.deviationDetected = deviationDetected; }

    public boolean isEmergencyActivated() { return emergencyActivated; }
    public void setEmergencyActivated(boolean emergencyActivated) { this.emergencyActivated = emergencyActivated; }

    public String getShareToken() { return shareToken; }
    public void setShareToken(String shareToken) { this.shareToken = shareToken; }

    public double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(double durationMinutes) { this.durationMinutes = durationMinutes; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getSafetyScore() { return safetyScore; }
    public void setSafetyScore(int safetyScore) { this.safetyScore = safetyScore; }

    public boolean isActive() { return STATUS_ACTIVE.equals(status); }
    public boolean isCompleted() { return STATUS_COMPLETED.equals(status); }
}
