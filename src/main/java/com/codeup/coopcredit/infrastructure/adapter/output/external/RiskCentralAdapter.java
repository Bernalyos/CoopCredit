package com.codeup.coopcredit.infrastructure.adapter.output.external;

import com.codeup.coopcredit.domain.model.creditrequest.RiskEvaluation;
import com.codeup.coopcredit.domain.model.creditrequest.RiskLevel;
import com.codeup.coopcredit.domain.ports.out.RiskCentralPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class RiskCentralAdapter implements RiskCentralPort {

    private final RestTemplate restTemplate;
    private final String riskCentralUrl;

    public RiskCentralAdapter(RestTemplate restTemplate,
            @Value("${risk.central.url:http://localhost:8081}") String riskCentralUrl) {
        this.restTemplate = restTemplate;
        this.riskCentralUrl = riskCentralUrl;
    }

    @Override
    public RiskEvaluation evaluateRisk(String document, BigDecimal amount, Integer term) {
        try {
            String url = riskCentralUrl + "/risk-evaluation";

            Map<String, Object> request = new HashMap<>();
            request.put("document", document);
            request.put("amount", amount);
            request.put("term", term);

            RiskEvaluationResponse response = restTemplate.postForObject(url, request, RiskEvaluationResponse.class);

            if (response != null) {
                return new RiskEvaluation(
                        response.getScore(),
                        response.getRiskLevel(),
                        response.getReason(),
                        response.getEvaluationDate());
            }

            // Fallback if service is unavailable
            return new RiskEvaluation(500, RiskLevel.MEDIUM, "Risk service unavailable", LocalDateTime.now());

        } catch (Exception e) {
            // Fallback on error
            return new RiskEvaluation(500, RiskLevel.MEDIUM, "Error calling risk service: " + e.getMessage(),
                    LocalDateTime.now());
        }
    }

    // DTO for external service response
    private static class RiskEvaluationResponse {
        private Integer score;
        private RiskLevel riskLevel;
        private String reason;
        private LocalDateTime evaluationDate;

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public RiskLevel getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public LocalDateTime getEvaluationDate() {
            return evaluationDate;
        }

        public void setEvaluationDate(LocalDateTime evaluationDate) {
            this.evaluationDate = evaluationDate;
        }
    }
}
