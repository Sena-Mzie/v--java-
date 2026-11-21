package com.vuka.app.models;

/**
 * Safety alert displayed on dashboards.
 * All data is illustrative.
 */
public class Alert {
    private String id;
    private String title;
    private String description;
    private String type; // "risk", "incident", "deviation", "emergency"
    private String severity; // "low", "moderate", "elevated", "high"
    private String distanceOrTime;
    private long timestamp;
    private boolean isRead;

    public Alert() {}

    public Alert(String id, String title, String description, String type, String severity, String distanceOrTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.severity = severity;
        this.distanceOrTime = distanceOrTime;
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDistanceOrTime() { return distanceOrTime; }
    public void setDistanceOrTime(String distanceOrTime) { this.distanceOrTime = distanceOrTime; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getSeverityIcon() {
        switch (severity) {
            case "high": return "🔴";
            case "elevated": return "🟡";
            case "moderate": return "🟠";
            default: return "🟢";
        }
    }
}
