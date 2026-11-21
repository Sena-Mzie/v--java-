package com.vuka.app.models;

import java.util.List;

/**
 * Full safety profile for a trip.
 * Contains overall and breakdown risk scores plus recommendations.
 * NOTE: All data is ILLUSTRATIVE and not verified crime intelligence.
 */
public class TripSafetyProfile {
    private String tripId;
    private int overallScore;
    private String overallRiskLevel;

    private String pickupRiskLevel;
    private String pickupRiskDescription;

    private String routeRiskLevel;
    private String routeRiskDescription;

    private String destinationRiskLevel;
    private String destinationRiskDescription;

    private List<RiskFactor> riskFactors;

    // Safer route info
    private boolean saferRouteAvailable;
    private int originalRouteMins;
    private int saferRouteMins;
    private int riskReductionPercent;

    public TripSafetyProfile() {}

    // Getters and setters
    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public String getOverallRiskLevel() { return overallRiskLevel; }
    public void setOverallRiskLevel(String overallRiskLevel) { this.overallRiskLevel = overallRiskLevel; }

    public String getPickupRiskLevel() { return pickupRiskLevel; }
    public void setPickupRiskLevel(String pickupRiskLevel) { this.pickupRiskLevel = pickupRiskLevel; }

    public String getPickupRiskDescription() { return pickupRiskDescription; }
    public void setPickupRiskDescription(String pickupRiskDescription) { this.pickupRiskDescription = pickupRiskDescription; }

    public String getRouteRiskLevel() { return routeRiskLevel; }
    public void setRouteRiskLevel(String routeRiskLevel) { this.routeRiskLevel = routeRiskLevel; }

    public String getRouteRiskDescription() { return routeRiskDescription; }
    public void setRouteRiskDescription(String routeRiskDescription) { this.routeRiskDescription = routeRiskDescription; }

    public String getDestinationRiskLevel() { return destinationRiskLevel; }
    public void setDestinationRiskLevel(String destinationRiskLevel) { this.destinationRiskLevel = destinationRiskLevel; }

    public String getDestinationRiskDescription() { return destinationRiskDescription; }
    public void setDestinationRiskDescription(String destinationRiskDescription) { this.destinationRiskDescription = destinationRiskDescription; }

    public List<RiskFactor> getRiskFactors() { return riskFactors; }
    public void setRiskFactors(List<RiskFactor> riskFactors) { this.riskFactors = riskFactors; }

    public boolean isSaferRouteAvailable() { return saferRouteAvailable; }
    public void setSaferRouteAvailable(boolean saferRouteAvailable) { this.saferRouteAvailable = saferRouteAvailable; }

    public int getOriginalRouteMins() { return originalRouteMins; }
    public void setOriginalRouteMins(int originalRouteMins) { this.originalRouteMins = originalRouteMins; }

    public int getSaferRouteMins() { return saferRouteMins; }
    public void setSaferRouteMins(int saferRouteMins) { this.saferRouteMins = saferRouteMins; }

    public int getRiskReductionPercent() { return riskReductionPercent; }
    public void setRiskReductionPercent(int riskReductionPercent) { this.riskReductionPercent = riskReductionPercent; }
}
