package com.vuka.app.models;

/**
 * Represents a contributing risk factor in a trip safety profile.
 * All data is illustrative and not verified crime intelligence.
 */
public class RiskFactor {
    private String type;
    private String confidence; // "low", "medium", "high"
    private String date;
    private String explanation;
    private String riskLevel;

    public RiskFactor() {}

    public RiskFactor(String type, String confidence, String date, String explanation, String riskLevel) {
        this.type = type;
        this.confidence = confidence;
        this.date = date;
        this.explanation = explanation;
        this.riskLevel = riskLevel;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
