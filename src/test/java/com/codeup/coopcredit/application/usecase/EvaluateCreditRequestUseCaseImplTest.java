package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequestStatus;
import com.codeup.coopcredit.domain.model.creditrequest.RiskEvaluation;
import com.codeup.coopcredit.domain.model.creditrequest.RiskLevel;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.RiskCentralPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluateCreditRequestUseCaseImplTest {

    @Mock
    private CreditRequestRepositoryPort creditRequestRepositoryPort;

    @Mock
    private RiskCentralPort riskCentralPort;

    @InjectMocks
    private EvaluateCreditRequestUseCaseImpl useCase;

    @Test
    void shouldApproveWhenLowRiskAndPolicyMet() {
        // Given
        Long requestId = 1L;
        // Seniority > 6 months
        com.codeup.coopcredit.domain.model.affiliate.Affiliate affiliate = new com.codeup.coopcredit.domain.model.affiliate.Affiliate(
                1L, "12345", "John Doe", BigDecimal.valueOf(5000), LocalDate.now().minusMonths(7), null);
        // Rate provided to avoid NPE in calculation
        CreditRequest request = new CreditRequest(requestId, affiliate, BigDecimal.valueOf(1000), 12,
                BigDecimal.valueOf(2.0), LocalDateTime.now().minusDays(1), CreditRequestStatus.PENDING);
        RiskEvaluation evaluation = new RiskEvaluation(850, RiskLevel.LOW, "Approved", LocalDateTime.now());

        when(creditRequestRepositoryPort.findById(requestId)).thenReturn(java.util.Optional.of(request));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(evaluation);
        when(creditRequestRepositoryPort.save(any(CreditRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        CreditRequest result = useCase.evaluate(requestId);

        // Then
        assertEquals(CreditRequestStatus.APPROVED, result.getStatus());
        assertEquals(RiskLevel.LOW, result.getRiskEvaluation().getRiskLevel());
    }

    @Test
    void shouldRejectWhenHighRisk() {
        // Given
        Long requestId = 1L;
        com.codeup.coopcredit.domain.model.affiliate.Affiliate affiliate = new com.codeup.coopcredit.domain.model.affiliate.Affiliate(
                1L, "12345", "John Doe", BigDecimal.valueOf(5000), LocalDate.now().minusMonths(7), null);
        CreditRequest request = new CreditRequest(requestId, affiliate, BigDecimal.valueOf(1000), 12,
                BigDecimal.valueOf(2.0), LocalDateTime.now().minusDays(1), CreditRequestStatus.PENDING);
        RiskEvaluation evaluation = new RiskEvaluation(300, RiskLevel.HIGH, "Risky", LocalDateTime.now());

        when(creditRequestRepositoryPort.findById(requestId)).thenReturn(java.util.Optional.of(request));
        when(riskCentralPort.evaluateRisk(any(), any(), any())).thenReturn(evaluation);
        when(creditRequestRepositoryPort.save(any(CreditRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        CreditRequest result = useCase.evaluate(requestId);

        // Then
        assertEquals(CreditRequestStatus.REJECTED, result.getStatus());
    }
}
