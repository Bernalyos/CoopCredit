package com.codeup.coopcredit.domain.ports.out;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import java.util.List;
import java.util.Optional;

public interface CreditRequestRepositoryPort {
    CreditRequest save(CreditRequest request);

    Optional<CreditRequest> findById(Long id);

    List<CreditRequest> findAllByAffiliateId(Long affiliateId);

    List<CreditRequest> findAllPending();
}
