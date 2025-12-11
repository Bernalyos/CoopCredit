package com.codeup.coopcredit.infrastructure.adapter.output.persistence;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.entity.CreditRequestEntity;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.mapper.CreditRequestMapper;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository.SpringDataCreditRequestRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreditRequestRepositoryAdapter implements CreditRequestRepositoryPort {

    private final SpringDataCreditRequestRepository repository;
    private final CreditRequestMapper mapper;

    public CreditRequestRepositoryAdapter(SpringDataCreditRequestRepository repository, CreditRequestMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CreditRequest save(CreditRequest request) {
        CreditRequestEntity entity = mapper.toEntity(request);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<CreditRequest> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CreditRequest> findAllByAffiliateId(Long affiliateId) {
        return repository.findAllByAffiliateId(affiliateId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditRequest> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditRequest> findAllPending() {
        return repository.findByStatus("PENDING")
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
