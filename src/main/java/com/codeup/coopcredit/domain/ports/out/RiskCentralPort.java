package com.codeup.coopcredit.domain.ports.out;

import com.codeup.coopcredit.domain.model.creditrequest.RiskEvaluation;
import java.math.BigDecimal;

public interface RiskCentralPort {
    RiskEvaluation evaluateRisk(String document, BigDecimal amount, Integer term);
}
