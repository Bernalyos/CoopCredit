package com.codeup.coopcredit.domain.ports.in;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import java.math.BigDecimal;

public interface EditAffiliateUseCase {
    Affiliate edit(Long id, String name, BigDecimal salary);
}
