package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.exception.DuplicateResourceException;
import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.model.affiliate.AffiliateStatus;
import com.codeup.coopcredit.domain.ports.in.RegisterAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class RegisterAffiliateUseCaseImpl implements RegisterAffiliateUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterAffiliateUseCaseImpl.class);
    
    private final AffiliateRepositoryPort repository;

    public RegisterAffiliateUseCaseImpl(AffiliateRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Affiliate register(String document, String name, BigDecimal salary, LocalDate affiliationDate) {
        log.info("Registering new affiliate with document: {}", document);
        
        // Validate document is unique
        if (repository.existsByDocument(document)) {
            log.warn("Attempt to register duplicate affiliate with document: {}", document);
            throw new DuplicateResourceException("Affiliate", "document", document);
        }
        
        // Set affiliation date to today if not provided
        if (affiliationDate == null) {
            affiliationDate = LocalDate.now();
            log.debug("Affiliation date not provided, using current date: {}", affiliationDate);
        }

        Affiliate affiliate = new Affiliate(
                null,
                document,
                name,
                salary,
                affiliationDate,
                AffiliateStatus.ACTIVE);
        
        Affiliate saved = repository.save(affiliate);
        log.info("Affiliate registered successfully with ID: {}", saved.getId());
        
        return saved;
    }
}
