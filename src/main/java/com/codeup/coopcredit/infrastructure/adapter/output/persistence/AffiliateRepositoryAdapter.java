package com.codeup.coopcredit.infrastructure.adapter.output.persistence;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.mapper.AffiliateMapper;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository.SpringDataAffiliateRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AffiliateRepositoryAdapter implements AffiliateRepositoryPort {

    private final SpringDataAffiliateRepository repository;
    private final AffiliateMapper mapper;

    public AffiliateRepositoryAdapter(SpringDataAffiliateRepository repository, AffiliateMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity = mapper.toEntity(affiliate);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return repository.findByDocument(document).map(mapper::toDomain);
    }

    @Override
    public boolean existsByDocument(String document) {
        return repository.existsByDocument(document);
    }
}
