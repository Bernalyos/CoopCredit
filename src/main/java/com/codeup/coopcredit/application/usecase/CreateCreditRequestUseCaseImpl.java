package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.model.affiliate.AffiliateStatus;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequestStatus;
import com.codeup.coopcredit.domain.ports.in.CreateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateCreditRequestUseCaseImpl implements CreateCreditRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCreditRequestUseCaseImpl.class);

    private final CreditRequestRepositoryPort creditRequestRepository;
    private final AffiliateRepositoryPort affiliateRepository;

    public CreateCreditRequestUseCaseImpl(CreditRequestRepositoryPort creditRequestRepository,
            AffiliateRepositoryPort affiliateRepository) {
        this.creditRequestRepository = creditRequestRepository;
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public CreditRequest create(Long affiliateId, BigDecimal amount, Integer term) {
        Affiliate affiliate = affiliateRepository.findById(affiliateId)
                .orElseThrow(() -> new IllegalArgumentException("Affiliate not found with ID: " + affiliateId));

        log.debug("Creating credit request for affiliate ID: {}, Status: {}", affiliateId, affiliate.getStatus());

        if (affiliate.getStatus() != AffiliateStatus.ACTIVE) {
            throw new IllegalStateException("Affiliate must be ACTIVE to request a credit.");
        }

        // Rate is not specified in input, assuming logic or default.
        // For now set to 0 or handled elsewhere?
        // The prompt says "tasa propuesta". Let's assume a fixed rate or calculated.
        // I will assume a default rate for now or 1.5% monthly as per common examples.
        BigDecimal rate = new BigDecimal("1.5");

        CreditRequest request = new CreditRequest(null, affiliate, amount, term, rate, LocalDateTime.now(),
                CreditRequestStatus.PENDING);

        return creditRequestRepository.save(request);
    }
}
