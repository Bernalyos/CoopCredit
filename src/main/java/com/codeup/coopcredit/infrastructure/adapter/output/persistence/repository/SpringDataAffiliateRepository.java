package com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository;

import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpringDataAffiliateRepository extends JpaRepository<AffiliateEntity, Long> {
    Optional<AffiliateEntity> findByDocument(String document);

    boolean existsByDocument(String document);
}
