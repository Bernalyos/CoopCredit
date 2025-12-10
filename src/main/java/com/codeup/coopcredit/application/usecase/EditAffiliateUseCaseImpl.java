package com.codeup.coopcredit.application.usecase;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.ports.in.EditAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import java.math.BigDecimal;

public class EditAffiliateUseCaseImpl implements EditAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public EditAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate edit(Long id, String name, BigDecimal salary) {
        Affiliate affiliate = affiliateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Affiliate not found with ID: " + id));

        affiliate.updateInfo(name, salary);
        return affiliateRepository.save(affiliate);
    }
}
