package com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository;

import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.CreditRequestEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataCreditRequestRepository extends JpaRepository<CreditRequestEntity, Long> {

    @EntityGraph(attributePaths = { "affiliate" })
    List<CreditRequestEntity> findAllByAffiliateId(Long affiliateId);

    @EntityGraph(attributePaths = { "affiliate" })
    List<CreditRequestEntity> findByStatus(String status);
}
