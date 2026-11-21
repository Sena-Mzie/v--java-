package com.vuka.app.models;

/**
 * Driver-specific profile extension.
 * Contains vehicle details and verification documents.
 */
public class DriverProfile {
    private String userId;
    private String vehicleRegistration;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleColour;
    private String ehailingPlatform;
    private String verificationStatus; // "pending", "approved", "rejected", "more_info_required"
    private boolean licenceUploaded;
    private boolean idDocumentUploaded;
    private boolean vehicleRegUploaded;
    private String qrCodeToken;

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_MORE_INFO = "more_info_required";

    public DriverProfile() {
        this.verificationStatus = STATUS_PENDING;
    }

    public DriverProfile(String userId) {
        this.userId = userId;
        this.verificationStatus = STATUS_PENDING;
        // Generate a demo QR token
        this.qrCodeToken = "VUKA_DRIVER_" + userId + "_DEMO";
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVehicleRegistration() { return vehicleRegistration; }
    public void setVehicleRegistration(String vehicleRegistration) { this.vehicleRegistration = vehicleRegistration; }

    public String getVehicleMake() { return vehicleMake; }
    public void setVehicleMake(String vehicleMake) { this.vehicleMake = vehicleMake; }

    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public String getVehicleColour() { return vehicleColour; }
    public void setVehicleColour(String vehicleColour) { this.vehicleColour = vehicleColour; }

    public String getEhailingPlatform() { return ehailingPlatform; }
    public void setEhailingPlatform(String ehailingPlatform) { this.ehailingPlatform = ehailingPlatform; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public boolean isLicenceUploaded() { return licenceUploaded; }
    public void setLicenceUploaded(boolean licenceUploaded) { this.licenceUploaded = licenceUploaded; }

    public boolean isIdDocumentUploaded() { return idDocumentUploaded; }
    public void setIdDocumentUploaded(boolean idDocumentUploaded) { this.idDocumentUploaded = idDocumentUploaded; }

    public boolean isVehicleRegUploaded() { return vehicleRegUploaded; }
    public void setVehicleRegUploaded(boolean vehicleRegUploaded) { this.vehicleRegUploaded = vehicleRegUploaded; }

    public String getQrCodeToken() { return qrCodeToken; }
    public void setQrCodeToken(String qrCodeToken) { this.qrCodeToken = qrCodeToken; }

    public String getVehicleSummary() {
        return vehicleMake + " " + vehicleModel + " · " + vehicleColour;
    }

    public boolean isFullyUploaded() {
        return licenceUploaded && idDocumentUploaded && vehicleRegUploaded;
    }
}
