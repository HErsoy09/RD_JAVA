package com.rd.service;
import com.rd.config.RiskConfig.RiskLevel;

public record RiskScores(
    double frequency,
    double probability,
    double severity
) {
    public RiskScores {
        if (frequency < 0 || probability < 0 || severity < 0) {
            throw new IllegalArgumentException("Risk değerleri negatif olamaz");
        }
    }

    public double getSpecialProbability() {
        return (probability >= 4) ? 2 : 1;
    }

    public double calculateNormalRiskScore() {
        return frequency * probability * severity;
    }

    public double calculateSpecialRiskScore() {
        return frequency * getSpecialProbability() * severity;
    }

    public RiskLevel getNormalRiskLevel() {
        return RiskLevel.fromScore(calculateNormalRiskScore());
    }

    public RiskLevel getSpecialRiskLevel() {
        return RiskLevel.fromScore(calculateSpecialRiskScore());
    }

    public String getNormalRiskDescription() {
        return getNormalRiskLevel().getDescription();
    }

    public String getSpecialRiskDescription() {
        return getSpecialRiskLevel().getDescription();
    }

    public double[] toArray() {
        return new double[]{frequency, probability, severity};
    }
}