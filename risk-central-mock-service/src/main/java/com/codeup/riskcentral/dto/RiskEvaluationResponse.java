package com.codeup.riskcentral.dto;

import java.time.LocalDateTime;

public class RiskEvaluationResponse {
    private Integer score;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String reason;
    private LocalDateTime evaluationDate;

    public RiskEvaluationResponse(Integer score, String riskLevel, String reason) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.reason = reason;
        this.evaluationDate = LocalDateTime.now();
    }

    // Getters
    public Integer getScore() {
        return score;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }
}
