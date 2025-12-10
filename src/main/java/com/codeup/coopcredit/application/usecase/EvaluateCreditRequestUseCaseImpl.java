package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.model.creditrequest.RiskEvaluation;
import com.codeup.coopcredit.domain.model.creditrequest.RiskLevel;
import com.codeup.coopcredit.domain.ports.in.EvaluateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.RiskCentralPort;

public class EvaluateCreditRequestUseCaseImpl implements EvaluateCreditRequestUseCase {

    private final CreditRequestRepositoryPort creditRequestRepository;
    private final RiskCentralPort riskCentralPort;

    public EvaluateCreditRequestUseCaseImpl(CreditRequestRepositoryPort creditRequestRepository,
            RiskCentralPort riskCentralPort) {
        this.creditRequestRepository = creditRequestRepository;
        this.riskCentralPort = riskCentralPort;
    }

    @Override
    public CreditRequest evaluate(Long requestId) {
        CreditRequest request = creditRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Credit Request not found with ID: " + requestId));

        // 1. Get Risk Evaluation
        RiskEvaluation evaluation = riskCentralPort.evaluateRisk(
                request.getAffiliate().getDocument(),
                request.getAmount(),
                request.getTerm());
        request.assignEvaluation(evaluation);

        // 2. Validate Business Policies
        try {
            validateBusinessRules(request.getAffiliate(), request.getAmount(), request.getTerm(), request.getRate());

            // 3. Decision Logic based on Score/Risk
            if (evaluation.getRiskLevel() == RiskLevel.HIGH) {
                request.reject("High risk level detected: Score " + evaluation.getScore());
            } else if (evaluation.getRiskLevel() == RiskLevel.LOW && evaluation.getScore() < 800) {
                // Example nuance logic: even if LOW, if score is borderline? No, let's keep it
                // simple.
                // Actually standard logic: High -> Reject. Low/Medium -> Approve?
                // Prompt didn't specify exact cutoff for Reject vs Approve other than "Risk
                // Service returns Level".
                // Let's assume only HIGH is rejected, or maybe MEDIUM too depending on policy.
                // I'll reject HIGH. Approve others.
                request.approve("Credit approved based on satisfactory risk evaluation.");
            } else {
                request.approve("Credit approved.");
            }

        } catch (IllegalArgumentException e) {
            request.reject("Policy violation: " + e.getMessage());
        }

        return creditRequestRepository.save(request);
    }

    private void validateBusinessRules(Affiliate affiliate,
            BigDecimal amount,
            Integer term,
            BigDecimal rate) {
        validateSeniority(affiliate.getAffiliationDate());
        validateMaxAmount(affiliate.getSalary(), amount);
        validateQuotaIncomeRatio(affiliate.getSalary(), amount, term, rate);
    }

    private void validateSeniority(LocalDate affiliationDate) {
        long months = ChronoUnit.MONTHS.between(affiliationDate, LocalDate.now());
        if (months < 6) { // MIN_SENIORITY_MONTHS = 6
            throw new IllegalArgumentException(
                    "Affiliate does not meet the minimum seniority requirement of 6 months.");
        }
    }

    private void validateMaxAmount(BigDecimal salary, BigDecimal amount) {
        BigDecimal maxAmount = salary.multiply(new BigDecimal("50")); // MAX_AMOUNT_MULTIPLIER = 50
        if (amount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Requested amount exceeds the maximum allowed limit based on salary.");
        }
    }

    private void validateQuotaIncomeRatio(BigDecimal salary, BigDecimal amount, Integer term, BigDecimal rate) {
        // Validation logic for quota/income
        BigDecimal monthlyRate = rate.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        BigDecimal quota;

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            quota = amount.divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);
        } else {
            // Simplified calculation: (Amount / Term) * 1.2 buffer
            BigDecimal rawQuota = amount.divide(new BigDecimal(term), 10, RoundingMode.HALF_UP);
            quota = rawQuota.multiply(new BigDecimal("1.2"));
        }

        BigDecimal maxQuota = salary.multiply(new BigDecimal("0.50")); // MAX_QUOTA_INCOME_RATIO = 0.50

        if (quota.compareTo(maxQuota) > 0) {
            throw new IllegalArgumentException("Estimated quota exceeds 50% of monthly income.");
        }
    }
}
