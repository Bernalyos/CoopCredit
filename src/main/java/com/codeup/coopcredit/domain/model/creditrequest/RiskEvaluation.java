package com.codeup.coopcredit.domain.model.creditrequest;

import java.time.LocalDateTime;

public class RiskEvaluation {
    private Integer score;
    private RiskLevel riskLevel;
    private String reason;
    private LocalDateTime evaluationDate;

    public RiskEvaluation() {
    }

    public RiskEvaluation(Integer score, RiskLevel riskLevel, String reason, LocalDateTime evaluationDate) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.reason = reason;
        this.evaluationDate = evaluationDate;
    }

    public Integer getScore() {
        return score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }
}
