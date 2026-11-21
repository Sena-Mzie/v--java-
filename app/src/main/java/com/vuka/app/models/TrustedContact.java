package com.vuka.app.models;

/**
 * Represents a trusted contact for a VUKA user.
 * Contacts can receive trip-share links and emergency notifications.
 */
public class TrustedContact {
    private String id;
    private String userId;
    private String name;
    private String phoneNumber;
    private String relationship;
    private String consentStatus; // "pending", "accepted", "declined"
    private long addedAt;

    public static final String CONSENT_PENDING = "pending";
    public static final String CONSENT_ACCEPTED = "accepted";
    public static final String CONSENT_DECLINED = "declined";

    public TrustedContact() {}

    public TrustedContact(String id, String userId, String name, String phoneNumber, String relationship) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.consentStatus = CONSENT_PENDING;
        this.addedAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getConsentStatus() { return consentStatus; }
    public void setConsentStatus(String consentStatus) { this.consentStatus = consentStatus; }

    public long getAddedAt() { return addedAt; }
    public void setAddedAt(long addedAt) { this.addedAt = addedAt; }

    public String getInitials() {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split(" ");
        if (parts.length >= 2) {
            return String.valueOf(parts[0].charAt(0)).toUpperCase()
                    + String.valueOf(parts[1].charAt(0)).toUpperCase();
        }
        return String.valueOf(parts[0].charAt(0)).toUpperCase();
    }

    public boolean isConsentAccepted() {
        return CONSENT_ACCEPTED.equals(consentStatus);
    }
}
