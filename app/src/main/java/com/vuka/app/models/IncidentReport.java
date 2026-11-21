package com.vuka.app.models;

/**
 * Represents a community safety incident report submitted by a user.
 */
public class IncidentReport {
    private String id;
    private String userId;
    private String incidentType;
    private String locationName;
    private double latitude;
    private double longitude;
    private String description;
    private String photoPath;
    private String status; // "submitted", "under_review", "verified", "rejected"
    private long reportedAt;
    private boolean onRoute;

    public static final String STATUS_SUBMITTED = "submitted";
    public static final String STATUS_UNDER_REVIEW = "under_review";
    public static final String STATUS_VERIFIED = "verified";
    public static final String STATUS_REJECTED = "rejected";

    public IncidentReport() {}

    public IncidentReport(String id, String userId, String incidentType, String locationName) {
        this.id = id;
        this.userId = userId;
        this.incidentType = incidentType;
        this.locationName = locationName;
        this.status = STATUS_SUBMITTED;
        this.reportedAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getReportedAt() { return reportedAt; }
    public void setReportedAt(long reportedAt) { this.reportedAt = reportedAt; }

    public boolean isOnRoute() { return onRoute; }
    public void setOnRoute(boolean onRoute) { this.onRoute = onRoute; }

    public String getStatusDisplayText() {
        switch (status) {
            case STATUS_SUBMITTED: return "Submitted";
            case STATUS_UNDER_REVIEW: return "Under Review";
            case STATUS_VERIFIED: return "Verified";
            case STATUS_REJECTED: return "Rejected";
            default: return status;
        }
    }
}
