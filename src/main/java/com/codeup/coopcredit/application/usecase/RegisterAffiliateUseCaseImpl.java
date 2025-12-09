package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.model.affiliate.AffiliateStatus;
import com.codeup.coopcredit.domain.ports.in.RegisterAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RegisterAffiliateUseCaseImpl implements RegisterAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public RegisterAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate register(String document, String name, BigDecimal salary, LocalDate affiliationDate) {
        if (affiliateRepository.existsByDocument(document)) {
            throw new IllegalArgumentException("Affiliate with document " + document + " already exists.");
        }

        Affiliate newAffiliate = new Affiliate(null, document, name, salary, affiliationDate, AffiliateStatus.ACTIVE);
        return affiliateRepository.save(newAffiliate);
    }
}
