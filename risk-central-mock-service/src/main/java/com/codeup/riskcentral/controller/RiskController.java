package com.codeup.riskcentral.controller;

import com.codeup.riskcentral.dto.RiskEvaluationRequest;
import com.codeup.riskcentral.dto.RiskEvaluationResponse;
import org.springframework.web.bind.annotation.*;
import java.util.Random;

@RestController
@RequestMapping("/risk-evaluation")
public class RiskController {

    @PostMapping
    public RiskEvaluationResponse evaluate(@RequestBody RiskEvaluationRequest request) {
        String doc = request.getDocument();
        char lastDigitChar = doc.charAt(doc.length() - 1);
        int lastDigit = Character.isDigit(lastDigitChar) ? Character.getNumericValue(lastDigitChar) : 0;

        if (lastDigit <= 3) {
            return new RiskEvaluationResponse(850, "LOW", "Approved based on history (Mock)");
        } else if (lastDigit <= 7) {
            return new RiskEvaluationResponse(650, "MEDIUM", "Requires manual review (Mock)");
        } else {
            return new RiskEvaluationResponse(400, "HIGH", "High default probability (Mock)");
        }
    }
}
