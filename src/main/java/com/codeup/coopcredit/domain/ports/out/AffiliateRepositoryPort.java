package com.codeup.coopcredit.domain.ports.out;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import java.util.Optional;

public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);

    Optional<Affiliate> findById(Long id);

    Optional<Affiliate> findByDocument(String document);

    boolean existsByDocument(String document);
}
