package com.vuka.app.repositories;

import com.vuka.app.models.Alert;
import com.vuka.app.models.AuditLog;
import com.vuka.app.models.IncidentReport;
import com.vuka.app.models.RiskFactor;
import com.vuka.app.models.Trip;
import com.vuka.app.models.TripSafetyProfile;
import com.vuka.app.models.TrustedContact;
import com.vuka.app.models.User;
import com.vuka.app.models.DriverProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Central mock data repository for demonstration purposes.
 * Provides stateful in-memory data that updates during the demo session.
 *
 * IMPORTANT: All safety data, risk scores, and incident reports
 * in this repository are ILLUSTRATIVE ONLY and not verified intelligence.
 */
public class MockDataRepository {

    private static MockDataRepository instance;

    // In-memory state
    private final List<TrustedContact> trustedContacts = new ArrayList<>();
    private final List<Trip> trips = new ArrayList<>();
    private final List<IncidentReport> incidentReports = new ArrayList<>();
    private final List<Alert> alerts = new ArrayList<>();
    private final List<AuditLog> auditLogs = new ArrayList<>();

    private User currentUser;
    private DriverProfile currentDriverProfile;
    private Trip activeTrip;
    private TripSafetyProfile currentSafetyProfile;

    private boolean sosActive = false;
    private boolean duressActive = false;

    private MockDataRepository() {
        seedDemoData();
    }

    public static synchronized MockDataRepository getInstance() {
        if (instance == null) {
            instance = new MockDataRepository();
        }
        return instance;
    }

    /**
     * Seeds realistic demonstration data.
     * Labels all content as illustrative.
     */
    private void seedDemoData() {
        // Demo user (passenger default)
        currentUser = new User("user_001", "Jane Doe", "+27711234567", "passenger");
        currentUser.setVerified(true);

        // Demo driver profile
        currentDriverProfile = new DriverProfile("user_002");
        currentDriverProfile.setVehicleRegistration("CA 123-455");
        currentDriverProfile.setVehicleMake("Toyota");
        currentDriverProfile.setVehicleModel("Corolla");
        currentDriverProfile.setVehicleColour("White");
        currentDriverProfile.setEhailingPlatform("Uber");
        currentDriverProfile.setVerificationStatus(DriverProfile.STATUS_PENDING);

        // Trusted contacts
        trustedContacts.add(new TrustedContact("tc_001", "user_001", "Nomsa Dlamini", "+27712345678", "Friend"));
        trustedContacts.get(0).setConsentStatus(TrustedContact.CONSENT_ACCEPTED);

        trustedContacts.add(new TrustedContact("tc_002", "user_001", "Sipho Mthembu", "+27723456789", "Family"));
        trustedContacts.get(1).setConsentStatus(TrustedContact.CONSENT_ACCEPTED);

        trustedContacts.add(new TrustedContact("tc_003", "user_001", "Thandi Khumalo", "+27734567890", "Family"));
        trustedContacts.get(2).setConsentStatus(TrustedContact.CONSENT_PENDING);

        // Demo trips
        Trip trip1 = new Trip("trp_001", "user_001", "Johannesburg CBD", "Sandton City");
        trip1.setStatus(Trip.STATUS_COMPLETED);
        trip1.setOverallRiskScore(92);
        trip1.setRiskLevel(Trip.RISK_LOW);
        trip1.setDurationMinutes(28);
        trip1.setDistanceKm(12.5);
        trip1.setSafetyScore(95);
        trips.add(trip1);

        Trip trip2 = new Trip("trp_002", "user_001", "Rosebank", "Fourways Mall");
        trip2.setStatus(Trip.STATUS_COMPLETED);
        trip2.setOverallRiskScore(74);
        trip2.setRiskLevel(Trip.RISK_MODERATE);
        trip2.setDurationMinutes(35);
        trip2.setDistanceKm(18.2);
        trip2.setSafetyScore(78);
        trips.add(trip2);

        // Incident reports
        IncidentReport report1 = new IncidentReport("RPT-1047", "user_001",
                "Suspicious Activity", "Sandton Drive, Sandton");
        report1.setStatus(IncidentReport.STATUS_UNDER_REVIEW);
        report1.setDescription("Suspicious activity reported near intersection. Vehicle followed for 3 blocks.");
        report1.setOnRoute(true);
        incidentReports.add(report1);

        IncidentReport report2 = new IncidentReport("RPT-1045", "user_001",
                "Road Blocked", "M1 Highway, near Marlboro");
        report2.setStatus(IncidentReport.STATUS_VERIFIED);
        report2.setDescription("Road blocked due to accident. Use alternative route.");
        incidentReports.add(report2);

        // Alerts (illustrative)
        alerts.add(new Alert("alt_001", "High Risk Area",
                "Increased activity 2km away", "risk", "high", "2km"));
        alerts.add(new Alert("alt_002", "Road Closure",
                "M1 near Marlboro — Road closed", "incident", "moderate", "5km"));
        alerts.add(new Alert("alt_003", "Safety Alert",
                "Reduced visibility — evening hours", "risk", "elevated", "Recent"));

        // Audit logs
        auditLogs.add(new AuditLog("log_001", "admin_001", "APPROVED_DRIVER",
                "DriverProfile", "drv_123", "Driver application approved after document review"));
        auditLogs.add(new AuditLog("log_002", "admin_001", "VERIFIED_REPORT",
                "IncidentReport", "RPT-1045", "Road closure verified by field team"));
        auditLogs.add(new AuditLog("log_003", "admin_001", "REJECTED_REPORT",
                "IncidentReport", "RPT-1040", "Report could not be corroborated"));
    }

    // ==================== TRUSTED CONTACTS ====================

    public List<TrustedContact> getTrustedContacts() {
        return new ArrayList<>(trustedContacts);
    }

    public void addTrustedContact(TrustedContact contact) {
        // Generate ID
        contact.setId("tc_" + System.currentTimeMillis());
        contact.setConsentStatus(TrustedContact.CONSENT_PENDING);
        trustedContacts.add(contact);
    }

    public void updateTrustedContact(TrustedContact updated) {
        for (int i = 0; i < trustedContacts.size(); i++) {
            if (trustedContacts.get(i).getId().equals(updated.getId())) {
                trustedContacts.set(i, updated);
                return;
            }
        }
    }

    public void removeTrustedContact(String contactId) {
        trustedContacts.removeIf(c -> c.getId().equals(contactId));
    }

    public int getAcceptedContactCount() {
        int count = 0;
        for (TrustedContact c : trustedContacts) {
            if (c.isConsentAccepted()) count++;
        }
        return count;
    }

    // ==================== TRIPS ====================

    public List<Trip> getTrips() {
        return new ArrayList<>(trips);
    }

    public Trip getActiveTrip() {
        return activeTrip;
    }

    public void setActiveTrip(Trip trip) {
        this.activeTrip = trip;
    }

    public void addTrip(Trip trip) {
        trips.add(0, trip);
    }

    public void updateTrip(Trip updated) {
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(updated.getId())) {
                trips.set(i, updated);
                return;
            }
        }
    }

    public List<Trip> getRecentTrips(int limit) {
        List<Trip> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, trips.size()); i++) {
            result.add(trips.get(i));
        }
        return result;
    }

    // ==================== SAFETY PROFILE ====================

    public TripSafetyProfile getCurrentSafetyProfile() {
        return currentSafetyProfile;
    }

    public void setCurrentSafetyProfile(TripSafetyProfile profile) {
        this.currentSafetyProfile = profile;
    }

    /**
     * Generates an illustrative safety profile for demonstration.
     * NOT real crime intelligence.
     */
    public TripSafetyProfile generateDemoSafetyProfile(String tripId, String pickup, String destination) {
        TripSafetyProfile profile = new TripSafetyProfile();
        profile.setTripId(tripId);
        profile.setOverallScore(92);
        profile.setOverallRiskLevel(Trip.RISK_LOW);

        profile.setPickupRiskLevel(Trip.RISK_LOW);
        profile.setPickupRiskDescription(
                "Area is generally well-lit and active. Pickup point is in a commercial zone.");

        profile.setRouteRiskLevel(Trip.RISK_LOW);
        profile.setRouteRiskDescription(
                "Route passes through 2 high-traffic areas. No major incidents in last 48h.");

        profile.setDestinationRiskLevel(Trip.RISK_LOW);
        profile.setDestinationRiskDescription(
                "Destination is a well-patrolled commercial district with 24h security.");

        // Risk factors (illustrative)
        List<RiskFactor> factors = new ArrayList<>();
        factors.add(new RiskFactor("Time of Day",
                "Medium confidence", "Last 7 days",
                "Evening trips between 6PM–10PM have a 23% higher incident correlation in this area. (ILLUSTRATIVE)",
                Trip.RISK_LOW));
        factors.add(new RiskFactor("Route Density",
                "High confidence", "Last 30 days",
                "Route passes through high-density commercial areas. Increased pedestrian activity. (ILLUSTRATIVE)",
                Trip.RISK_LOW));
        profile.setRiskFactors(factors);

        profile.setSaferRouteAvailable(true);
        profile.setOriginalRouteMins(28);
        profile.setSaferRouteMins(25);
        profile.setRiskReductionPercent(12);

        return profile;
    }

    // ==================== INCIDENT REPORTS ====================

    public List<IncidentReport> getIncidentReports() {
        return new ArrayList<>(incidentReports);
    }

    public void addIncidentReport(IncidentReport report) {
        report.setId("RPT-" + (1000 + incidentReports.size() + 1));
        report.setStatus(IncidentReport.STATUS_SUBMITTED);
        incidentReports.add(0, report);
    }

    public void updateReportStatus(String reportId, String newStatus) {
        for (IncidentReport r : incidentReports) {
            if (r.getId().equals(reportId)) {
                r.setStatus(newStatus);
                // Log admin action
                addAuditLog(new AuditLog(
                        "log_" + System.currentTimeMillis(),
                        "admin_001",
                        "STATUS_UPDATE",
                        "IncidentReport",
                        reportId,
                        "Status changed to: " + newStatus
                ));
                return;
            }
        }
    }

    // ==================== ALERTS ====================

    public List<Alert> getAlerts() {
        return new ArrayList<>(alerts);
    }

    public void addAlert(Alert alert) {
        alerts.add(0, alert);
    }

    public int getUnreadAlertCount() {
        int count = 0;
        for (Alert a : alerts) {
            if (!a.isRead()) count++;
        }
        return count;
    }

    // ==================== SOS / EMERGENCY ====================

    public boolean isSosActive() { return sosActive; }

    public void activateSos() {
        this.sosActive = true;
        if (activeTrip != null) {
            activeTrip.setEmergencyActivated(true);
            activeTrip.setStatus(Trip.STATUS_EMERGENCY);
        }
    }

    public void cancelSos() {
        this.sosActive = false;
        if (activeTrip != null && Trip.STATUS_EMERGENCY.equals(activeTrip.getStatus())) {
            activeTrip.setStatus(Trip.STATUS_ACTIVE);
        }
    }

    public boolean isDuressActive() { return duressActive; }

    public void activateDuress() {
        this.duressActive = true;
        // Silent escalation — does NOT visibly change normal UI
        // In a real system this would silently contact authorities
    }

    // ==================== AUDIT LOGS ====================

    public List<AuditLog> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    public void addAuditLog(AuditLog log) {
        auditLogs.add(0, log);
    }

    // ==================== USER ====================

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) { this.currentUser = user; }

    public DriverProfile getCurrentDriverProfile() { return currentDriverProfile; }

    public void setCurrentDriverProfile(DriverProfile profile) { this.currentDriverProfile = profile; }

    // ==================== RESET ====================

    public void reset() {
        trustedContacts.clear();
        trips.clear();
        incidentReports.clear();
        alerts.clear();
        auditLogs.clear();
        activeTrip = null;
        currentSafetyProfile = null;
        sosActive = false;
        duressActive = false;
        seedDemoData();
    }
}
