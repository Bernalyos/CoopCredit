package com.codeup.coopcredit.infrastructure.adapter.input.rest;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.ports.in.CreateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.in.EvaluateCreditRequestUseCase;
import com.codeup.coopcredit.infrastructure.adapter.input.rest.dto.CreditRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit-requests")
public class CreditRequestController {

    private final CreateCreditRequestUseCase createUseCase;
    private final EvaluateCreditRequestUseCase evaluateUseCase;

    public CreditRequestController(CreateCreditRequestUseCase createUseCase,
            EvaluateCreditRequestUseCase evaluateUseCase) {
        this.createUseCase = createUseCase;
        this.evaluateUseCase = evaluateUseCase;
    }

    @PreAuthorize("hasRole('ROLE_AFILIADO')")
    @PostMapping
    public ResponseEntity<CreditRequest> create(@RequestBody @jakarta.validation.Valid CreditRequestDto request) {
        CreditRequest created = createUseCase.create(
                request.getAffiliateId(),
                request.getAmount(),
                request.getTerm());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<CreditRequest> evaluate(@PathVariable Long id) {
        CreditRequest evaluated = evaluateUseCase.evaluate(id);
        return ResponseEntity.ok(evaluated);
    }
}
