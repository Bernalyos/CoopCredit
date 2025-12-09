package com.codeup.coopcredit.infrastructure.adapter.output.persistence.mapper;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.CreditRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AffiliateMapper.class })
public interface CreditRequestMapper {

    @Mapping(source = "riskEvaluation.score", target = "riskScore")
    @Mapping(source = "riskEvaluation.riskLevel", target = "riskLevel")
    @Mapping(source = "riskEvaluation.reason", target = "riskReason")
    @Mapping(source = "riskEvaluation.evaluationDate", target = "riskEvaluationDate")
    CreditRequestEntity toEntity(CreditRequest domain);

    @Mapping(source = "riskScore", target = "riskEvaluation.score")
    @Mapping(source = "riskLevel", target = "riskEvaluation.riskLevel")
    @Mapping(source = "riskReason", target = "riskEvaluation.reason")
    @Mapping(source = "riskEvaluationDate", target = "riskEvaluation.evaluationDate")
    CreditRequest toDomain(CreditRequestEntity entity);
}
