package com.codeup.coopcredit.domain.ports.in;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;

public interface EvaluateCreditRequestUseCase {
    CreditRequest evaluate(Long requestId);
}
