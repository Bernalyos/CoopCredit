package com.codeup.coopcredit.domain.ports.in;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import java.math.BigDecimal;

public interface CreateCreditRequestUseCase {
    CreditRequest create(Long affiliateId, BigDecimal amount, Integer term);
}
