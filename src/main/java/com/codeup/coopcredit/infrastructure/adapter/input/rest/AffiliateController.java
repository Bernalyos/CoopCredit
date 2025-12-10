package com.codeup.coopcredit.infrastructure.adapter.input.rest;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.ports.in.EditAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.in.RegisterAffiliateUseCase;
import com.codeup.coopcredit.infrastructure.adapter.input.rest.dto.AffiliateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affiliates")
public class AffiliateController {

    private final RegisterAffiliateUseCase registerUseCase;
    private final EditAffiliateUseCase editUseCase;

    public AffiliateController(RegisterAffiliateUseCase registerUseCase, EditAffiliateUseCase editUseCase) {
        this.registerUseCase = registerUseCase;
        this.editUseCase = editUseCase;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANALISTA')")
    @PostMapping
    public ResponseEntity<Affiliate> register(@RequestBody @jakarta.validation.Valid AffiliateRequest request) {
        Affiliate affiliate = registerUseCase.register(
                request.getDocument(),
                request.getName(),
                request.getSalary(),
                request.getAffiliationDate());
        return ResponseEntity.ok(affiliate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Affiliate> edit(@PathVariable Long id, @RequestBody AffiliateRequest request) {
        Affiliate affiliate = editUseCase.edit(
                id,
                request.getName(),
                request.getSalary());
        return ResponseEntity.ok(affiliate);
    }
}
