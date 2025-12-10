package com.codeup.riskcentral.dto;

import java.math.BigDecimal;

public class RiskEvaluationRequest {
    private String document;
    private BigDecimal amount;
    private Integer term;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }
}
