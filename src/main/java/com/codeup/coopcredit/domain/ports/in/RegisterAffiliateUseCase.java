package com.codeup.coopcredit.domain.ports.in;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface RegisterAffiliateUseCase {
    Affiliate register(String document, String name, BigDecimal salary, LocalDate affiliationDate);
}
