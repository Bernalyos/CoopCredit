package com.codeup.coopcredit.infrastructure.adapter.output.persistence;

import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository.SpringDataAffiliateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SpringDataAffiliateRepositoryTest {

    @Autowired
    private SpringDataAffiliateRepository repository;

    @Test
    void shouldFindAffiliateByDocument() {
        // Given
        AffiliateEntity entity = new AffiliateEntity();
        entity.setDocument("98765");
        entity.setName("Jane Doe");
        entity.setSalary(BigDecimal.valueOf(4500));
        entity.setAffiliationDate(LocalDate.now());
        entity.setStatus(com.codeup.coopcredit.domain.model.affiliate.AffiliateStatus.ACTIVE.name());
        repository.save(entity);

        // When
        Optional<AffiliateEntity> result = repository.findByDocument("98765");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenDocumentNotFound() {
        // When
        Optional<AffiliateEntity> result = repository.findByDocument("non-existent");

        // Then
        assertTrue(result.isEmpty());
    }
}
