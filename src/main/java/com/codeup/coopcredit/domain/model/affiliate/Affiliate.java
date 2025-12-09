package com.codeup.coopcredit.domain.model.affiliate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Affiliate {
    private Long id;
    private String document;
    private String name;
    private BigDecimal salary;
    private LocalDate affiliationDate;
    private AffiliateStatus status;

    public Affiliate() {
    }

    public Affiliate(Long id, String document, String name, BigDecimal salary, LocalDate affiliationDate,
            AffiliateStatus status) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.salary = salary;
        this.affiliationDate = affiliationDate;
        this.status = status;
    }

    public void activate() {
        this.status = AffiliateStatus.ACTIVE;
    }

    public void inactivate() {
        this.status = AffiliateStatus.INACTIVE;
    }

    public void updateInfo(String name, BigDecimal salary) {
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be greater than zero");
        }
        this.name = name;
        this.salary = salary;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }

    public AffiliateStatus getStatus() {
        return status;
    }

    // Setters (Protected or Public depending on need, sticking to POJO guidelines
    // but preferring business methods)
    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
