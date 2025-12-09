package com.codeup.coopcredit.domain.model.creditrequest;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditRequest {
    private Long id;
    private Affiliate affiliate;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal rate;
    private LocalDateTime requestDate;
    private CreditRequestStatus status;
    private RiskEvaluation riskEvaluation;

    public CreditRequest() {
    }

    public CreditRequest(Long id, Affiliate affiliate, BigDecimal amount, Integer term, BigDecimal rate,
            LocalDateTime requestDate, CreditRequestStatus status) {
        this.id = id;
        this.affiliate = affiliate;
        this.amount = amount;
        this.term = term;
        this.rate = rate;
        this.requestDate = requestDate;
        this.status = status;
    }

    public void approve(String reason) {
        this.status = CreditRequestStatus.APPROVED;
    }

    public void reject(String reason) {
        this.status = CreditRequestStatus.REJECTED;
    }

    public void assignEvaluation(RiskEvaluation riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getTerm() {
        return term;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public CreditRequestStatus getStatus() {
        return status;
    }

    public RiskEvaluation getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRiskEvaluation(RiskEvaluation riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
    }
}
