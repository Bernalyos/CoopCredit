package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.exception.BusinessRuleException;
import com.codeup.coopcredit.domain.exception.ResourceNotFoundException;
import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.model.affiliate.AffiliateStatus;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequestStatus;
import com.codeup.coopcredit.domain.ports.in.CreateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreateCreditRequestUseCaseImpl implements CreateCreditRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCreditRequestUseCaseImpl.class);
    private static final BigDecimal BASE_RATE = new BigDecimal("0.15");

    private final CreditRequestRepositoryPort creditRequestRepository;
    private final AffiliateRepositoryPort affiliateRepository;

    public CreateCreditRequestUseCaseImpl(
            CreditRequestRepositoryPort creditRequestRepository,
            AffiliateRepositoryPort affiliateRepository) {
        this.creditRequestRepository = creditRequestRepository;
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public CreditRequest create(Long affiliateId, BigDecimal amount, Integer term) {
        log.info("Creating credit request for affiliate ID: {}, amount: {}, term: {}", 
                 affiliateId, amount, term);

        // Validate affiliate exists
        Affiliate affiliate = affiliateRepository.findById(affiliateId)
                .orElseThrow(() -> {
                    log.warn("Affiliate not found with ID: {}", affiliateId);
                    return new ResourceNotFoundException("Affiliate", "id", affiliateId);
                });

        // Validate affiliate is active
        if (affiliate.getStatus() != AffiliateStatus.ACTIVE) {
            log.warn("Attempt to create credit request for inactive affiliate ID: {}, status: {}", 
                     affiliateId, affiliate.getStatus());
            throw new BusinessRuleException(
                    "Cannot create credit request for inactive affiliate. Current status: " + affiliate.getStatus());
        }

        // Validate no pending requests
        List<CreditRequest> pendingRequests = creditRequestRepository
                .findAllByAffiliateId(affiliateId)
                .stream()
                .filter(cr -> cr.getStatus() == CreditRequestStatus.PENDING)
                .toList();

        if (!pendingRequests.isEmpty()) {
            log.warn("Affiliate ID: {} already has {} pending credit request(s)", 
                     affiliateId, pendingRequests.size());
            throw new BusinessRuleException(
                    "Affiliate already has a pending credit request. Please wait for evaluation.");
        }

        log.debug("Creating credit request with rate: {}", BASE_RATE);

        CreditRequest creditRequest = new CreditRequest(
                null,
                affiliate,
                amount,
                term,
                BASE_RATE,
                LocalDateTime.now(),
                CreditRequestStatus.PENDING);

        CreditRequest saved = creditRequestRepository.save(creditRequest);
        log.info("Credit request created successfully with ID: {}", saved.getId());

        return saved;
    }
}
