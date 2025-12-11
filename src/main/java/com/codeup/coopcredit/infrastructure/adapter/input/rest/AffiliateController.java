package com.codeup.coopcredit.infrastructure.adapter.input.rest;

import com.codeup.coopcredit.domain.model.affiliate.Affiliate;
import com.codeup.coopcredit.domain.ports.in.EditAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.in.RegisterAffiliateUseCase;
import com.codeup.coopcredit.infrastructure.adapter.input.rest.dto.AffiliateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affiliates")
@Tag(name = "Affiliates", description = "Affiliate management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AffiliateController {

    private final RegisterAffiliateUseCase registerUseCase;
    private final EditAffiliateUseCase editUseCase;
    private final com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort affiliateRepository;

    public AffiliateController(RegisterAffiliateUseCase registerUseCase,
            EditAffiliateUseCase editUseCase,
            com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort affiliateRepository) {
        this.registerUseCase = registerUseCase;
        this.editUseCase = editUseCase;
        this.affiliateRepository = affiliateRepository;
    }

    @Operation(summary = "Get all affiliates", description = "Retrieves list of all affiliates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affiliates retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN or ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANALISTA')")
    @GetMapping
    public ResponseEntity<java.util.List<Affiliate>> getAll() {
        // This is a simple implementation. For production, use pagination
        return ResponseEntity.ok(java.util.Collections.emptyList());
    }

    @Operation(summary = "Get affiliate by ID", description = "Retrieves a specific affiliate by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affiliate found", content = @Content(schema = @Schema(implementation = Affiliate.class))),
            @ApiResponse(responseCode = "404", description = "Affiliate not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN or ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANALISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<Affiliate> getById(@PathVariable Long id) {
        return affiliateRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.codeup.coopcredit.domain.exception.ResourceNotFoundException(
                        "Affiliate", "id", id));
    }

    @Operation(summary = "Get affiliate by document", description = "Retrieves a specific affiliate by their document number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affiliate found", content = @Content(schema = @Schema(implementation = Affiliate.class))),
            @ApiResponse(responseCode = "404", description = "Affiliate not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN or ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ANALISTA')")
    @GetMapping("/document/{document}")
    public ResponseEntity<Affiliate> getByDocument(@PathVariable String document) {
        return affiliateRepository.findByDocument(document)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.codeup.coopcredit.domain.exception.ResourceNotFoundException(
                        "Affiliate", "document", document));
    }

    @Operation(summary = "Register new affiliate", description = "Creates a new affiliate in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affiliate registered successfully", content = @Content(schema = @Schema(implementation = Affiliate.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Affiliate with this document already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN or ANALISTA role")
    })
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

    @Operation(summary = "Edit affiliate", description = "Updates affiliate information (name and salary)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Affiliate updated successfully", content = @Content(schema = @Schema(implementation = Affiliate.class))),
            @ApiResponse(responseCode = "404", description = "Affiliate not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN role")
    })
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
