package com.vuka.app.models;

/**
 * User data model for VUKA platform.
 * Represents both driver and passenger profiles.
 */
public class User {
    private String id;
    private String name;
    private String phoneNumber;
    private String role; // "driver", "passenger", "admin"
    private String profilePhotoPath;
    private boolean isVerified;
    private String saIdNumber;
    private long createdAt;

    public User() {}

    public User(String id, String name, String phoneNumber, String role) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isVerified = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfilePhotoPath() { return profilePhotoPath; }
    public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getSaIdNumber() { return saIdNumber; }
    public void setSaIdNumber(String saIdNumber) { this.saIdNumber = saIdNumber; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getInitials() {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.trim().split(" ");
        if (parts.length >= 2) {
            return String.valueOf(parts[0].charAt(0)).toUpperCase()
                    + String.valueOf(parts[1].charAt(0)).toUpperCase();
        }
        return String.valueOf(parts[0].charAt(0)).toUpperCase();
    }
}
