package com.vuka.app;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic unit tests for VUKA application logic.
 * Tests are limited to pure Java classes (no Android Context dependencies).
 */
public class VukaUnitTest {

    @Test
    public void testPhoneValidation() {
        // Valid SA mobile numbers
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidSAPhone("0821234567"));
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidSAPhone("+27821234567"));
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidSAPhone("27821234567"));
        // Invalid
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAPhone("123"));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAPhone(""));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAPhone(null));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAPhone("0121234567")); // landline prefix
    }

    @Test
    public void testSAIdValidation() {
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidSAId("9001015000080"));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAId("12345"));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAId(""));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAId(null));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidSAId("900101500008A")); // non-numeric
    }

    @Test
    public void testRegistrationValidation() {
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidRegistration("CA 123-456"));
        assertTrue(com.vuka.app.utils.ValidationUtils.isValidRegistration("GP1234AB"));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidRegistration("AB"));
        assertFalse(com.vuka.app.utils.ValidationUtils.isValidRegistration(null));
    }

    @Test
    public void testPhoneNormalization() {
        assertEquals("+27821234567",
                com.vuka.app.utils.ValidationUtils.normalizePhone("0821234567"));
        assertEquals("+27821234567",
                com.vuka.app.utils.ValidationUtils.normalizePhone("27821234567"));
    }

    @Test
    public void testRiskLabels() {
        assertEquals("LOW", com.vuka.app.utils.RiskColorUtils.getRiskLabel("low"));
        assertEquals("MODERATE", com.vuka.app.utils.RiskColorUtils.getRiskLabel("moderate"));
        assertEquals("ELEVATED", com.vuka.app.utils.RiskColorUtils.getRiskLabel("elevated"));
        assertEquals("HIGH", com.vuka.app.utils.RiskColorUtils.getRiskLabel("high"));
        assertEquals("LOW", com.vuka.app.utils.RiskColorUtils.getRiskLabel(null));
    }

    @Test
    public void testRiskIcons() {
        assertEquals("🟢", com.vuka.app.utils.RiskColorUtils.getRiskIcon("low"));
        assertEquals("🟡", com.vuka.app.utils.RiskColorUtils.getRiskIcon("moderate"));
        assertEquals("🟠", com.vuka.app.utils.RiskColorUtils.getRiskIcon("elevated"));
        assertEquals("🔴", com.vuka.app.utils.RiskColorUtils.getRiskIcon("high"));
    }

    @Test
    public void testUserInitials() {
        com.vuka.app.models.User user = new com.vuka.app.models.User(
                "1", "Jane Doe", "+27711234567", "passenger");
        assertEquals("JD", user.getInitials());

        com.vuka.app.models.User single = new com.vuka.app.models.User(
                "2", "Madonna", "+27722345678", "passenger");
        assertEquals("M", single.getInitials());
    }

    @Test
    public void testTripStatusTransitions() {
        com.vuka.app.models.Trip trip = new com.vuka.app.models.Trip(
                "t1", "u1", "CBD", "Sandton");
        assertEquals(com.vuka.app.models.Trip.STATUS_CREATED, trip.getStatus());

        trip.setStatus(com.vuka.app.models.Trip.STATUS_ACTIVE);
        assertTrue(trip.isActive());
        assertFalse(trip.isCompleted());

        trip.setStatus(com.vuka.app.models.Trip.STATUS_COMPLETED);
        assertTrue(trip.isCompleted());
        assertFalse(trip.isActive());
    }

    @Test
    public void testTrustedContactInitials() {
        com.vuka.app.models.TrustedContact contact = new com.vuka.app.models.TrustedContact(
                "tc1", "u1", "Nomsa Dlamini", "+27712345678", "Friend");
        assertEquals("ND", contact.getInitials());
    }

    @Test
    public void testMockDataRepositorySeeding() {
        com.vuka.app.repositories.MockDataRepository repo =
                com.vuka.app.repositories.MockDataRepository.getInstance();
        assertNotNull(repo);
        assertFalse(repo.getTrustedContacts().isEmpty());
        assertFalse(repo.getTrips().isEmpty());
        assertFalse(repo.getAlerts().isEmpty());
        assertFalse(repo.getAuditLogs().isEmpty());
        assertNotNull(repo.getCurrentUser());
    }

    @Test
    public void testContactAddRemove() {
        com.vuka.app.repositories.MockDataRepository repo =
                com.vuka.app.repositories.MockDataRepository.getInstance();
        int initialCount = repo.getTrustedContacts().size();

        com.vuka.app.models.TrustedContact c = new com.vuka.app.models.TrustedContact(
                "", "u1", "Test Contact", "+27799999999", "Friend");
        repo.addTrustedContact(c);
        assertEquals(initialCount + 1, repo.getTrustedContacts().size());

        // Find the added contact and remove
        String addedId = repo.getTrustedContacts().get(repo.getTrustedContacts().size() - 1).getId();
        repo.removeTrustedContact(addedId);
        assertEquals(initialCount, repo.getTrustedContacts().size());
    }

    @Test
    public void testSosActivationAndCancellation() {
        com.vuka.app.repositories.MockDataRepository repo =
                com.vuka.app.repositories.MockDataRepository.getInstance();
        repo.cancelSos(); // ensure clean state
        assertFalse(repo.isSosActive());

        repo.activateSos();
        assertTrue(repo.isSosActive());

        repo.cancelSos();
        assertFalse(repo.isSosActive());
    }
}
