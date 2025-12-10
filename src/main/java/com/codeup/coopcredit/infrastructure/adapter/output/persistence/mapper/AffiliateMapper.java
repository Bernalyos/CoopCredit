package com.codeup.coopcredit.infrastructure.adapter.output.persistence.mapper;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {
    @Mapping(target = "creditRequests", ignore = true)
    AffiliateEntity toEntity(Affiliate domain);

    Affiliate toDomain(AffiliateEntity entity);
}