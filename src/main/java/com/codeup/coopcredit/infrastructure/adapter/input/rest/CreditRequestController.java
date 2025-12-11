package com.codeup.coopcredit.infrastructure.adapter.input.rest;

import com.codeup.coopcredit.domain.model.creditrequest.CreditRequest;
import com.codeup.coopcredit.domain.ports.in.CreateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.in.EvaluateCreditRequestUseCase;
import com.codeup.coopcredit.infrastructure.adapter.input.rest.dto.CreditRequestDto;
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
@RequestMapping("/credit-requests")
@Tag(name = "Credit Requests", description = "Credit request management and evaluation endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class CreditRequestController {

    private final CreateCreditRequestUseCase createUseCase;
    private final EvaluateCreditRequestUseCase evaluateUseCase;
    private final com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort creditRequestRepository;

    public CreditRequestController(CreateCreditRequestUseCase createUseCase,
            EvaluateCreditRequestUseCase evaluateUseCase,
            com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort creditRequestRepository) {
        this.createUseCase = createUseCase;
        this.evaluateUseCase = evaluateUseCase;
        this.creditRequestRepository = creditRequestRepository;
    }

    @Operation(summary = "Get all credit requests", description = "Retrieves list of all credit requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    @GetMapping
    public ResponseEntity<java.util.List<CreditRequest>> getAll() {
        return ResponseEntity.ok(creditRequestRepository.findAll());
    }

    @Operation(summary = "Get credit request by ID", description = "Retrieves a specific credit request by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit request found", content = @Content(schema = @Schema(implementation = CreditRequest.class))),
            @ApiResponse(responseCode = "404", description = "Credit request not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<CreditRequest> getById(@PathVariable Long id) {
        return creditRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.codeup.coopcredit.domain.exception.ResourceNotFoundException(
                        "CreditRequest", "id", id));
    }

    @Operation(summary = "Get credit requests by affiliate", description = "Retrieves all credit requests for a specific affiliate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit requests retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ANALISTA or AFILIADO role")
    })
    @PreAuthorize("hasRole('ROLE_ANALISTA') or hasRole('ROLE_AFILIADO')")
    @GetMapping("/affiliate/{affiliateId}")
    public ResponseEntity<java.util.List<CreditRequest>> getByAffiliate(@PathVariable Long affiliateId) {
        return ResponseEntity.ok(creditRequestRepository.findAllByAffiliateId(affiliateId));
    }

    @Operation(summary = "Create credit request", description = "Creates a new credit request for an affiliate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit request created successfully", content = @Content(schema = @Schema(implementation = CreditRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Affiliate not found"),
            @ApiResponse(responseCode = "422", description = "Business rule violation (inactive affiliate or pending request exists)"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires AFILIADO role")
    })
    @PreAuthorize("hasRole('ROLE_AFILIADO')")
    @PostMapping
    public ResponseEntity<CreditRequest> create(@RequestBody @jakarta.validation.Valid CreditRequestDto request) {
        CreditRequest created = createUseCase.create(
                request.getAffiliateId(),
                request.getAmount(),
                request.getTerm());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Evaluate credit request", description = "Evaluates a credit request using external risk service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit request evaluated successfully", content = @Content(schema = @Schema(implementation = CreditRequest.class))),
            @ApiResponse(responseCode = "404", description = "Credit request not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ANALISTA role")
    })
    @PreAuthorize("hasRole('ROLE_ANALISTA')")
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<CreditRequest> evaluate(@PathVariable Long id) {
        CreditRequest evaluated = evaluateUseCase.evaluate(id);
        return ResponseEntity.ok(evaluated);
    }
}
