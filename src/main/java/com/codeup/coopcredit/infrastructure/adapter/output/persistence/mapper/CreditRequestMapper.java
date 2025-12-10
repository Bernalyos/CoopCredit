package com.codeup.coopcredit.infrastructure.adapter.output.persistence.mapper;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.model.creditrequest.CreditRequestStatus;
import com.codeup.coopcredit.domain.model.creditrequest.RiskEvaluation;
import com.codeup.coopcredit.domain.model.creditrequest.RiskLevel;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.CreditRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AffiliateMapper.class })
public interface CreditRequestMapper {

    default CreditRequestEntity toEntity(CreditRequest domain) {
        if (domain == null) {
            return null;
        }

        CreditRequestEntity entity = new CreditRequestEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount());
        entity.setTerm(domain.getTerm());
        entity.setRate(domain.getRate());
        entity.setRequestDate(domain.getRequestDate());
        entity.setStatus(domain.getStatus() != null ? domain.getStatus().name() : null);

        if (domain.getRiskEvaluation() != null) {
            entity.setRiskScore(domain.getRiskEvaluation().getScore());
            entity.setRiskLevel(
                    domain.getRiskEvaluation().getRiskLevel() != null ? domain.getRiskEvaluation().getRiskLevel().name()
                            : null);
            entity.setRiskReason(domain.getRiskEvaluation().getReason());
            entity.setRiskEvaluationDate(domain.getRiskEvaluation().getEvaluationDate());
        }

        return entity;
    }

    default CreditRequest toDomain(CreditRequestEntity entity) {
        if (entity == null) {
            return null;
        }

        CreditRequest domain = new CreditRequest();
        domain.setId(entity.getId());
        domain.setAmount(entity.getAmount());
        domain.setTerm(entity.getTerm());
        domain.setRate(entity.getRate());
        domain.setRequestDate(entity.getRequestDate());
        domain.setStatus(entity.getStatus() != null ? CreditRequestStatus.valueOf(entity.getStatus()) : null);

        if (entity.getRiskScore() != null || entity.getRiskLevel() != null) {
            RiskLevel riskLevel = entity.getRiskLevel() != null ? RiskLevel.valueOf(entity.getRiskLevel()) : null;
            RiskEvaluation riskEvaluation = new RiskEvaluation(
                    entity.getRiskScore(),
                    riskLevel,
                    entity.getRiskReason(),
                    entity.getRiskEvaluationDate());
            domain.setRiskEvaluation(riskEvaluation);
        }

        return domain;
    }
}
