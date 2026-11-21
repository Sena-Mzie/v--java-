package com.vuka.app.models;

/**
 * Admin audit log entry.
 * Tracks administrative actions for accountability.
 */
public class AuditLog {
    private String id;
    private String adminId;
    private String action;
    private String entityType;
    private String entityId;
    private long timestamp;
    private String details;

    public AuditLog() {}

    public AuditLog(String id, String adminId, String action, String entityType, String entityId, String details) {
        this.id = id;
        this.adminId = adminId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
