package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.BranchService;
import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.infrastructure.inbound.api.dto.BranchRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises/{franchiseUuid}/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Branch> create(
            @PathVariable String franchiseUuid,
            @Valid @RequestBody BranchRequest request) {
        return branchService.create(franchiseUuid, request);
    }

    @GetMapping
    public Flux<Branch> getAll(@PathVariable String franchiseUuid) {
        return branchService.findAllByFranchise(franchiseUuid);
    }

    @GetMapping("/{branchUuid}")
    public Mono<Branch> getById(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid) {
        return branchService.findByUuid(franchiseUuid, branchUuid);
    }

    @PutMapping("/{branchUuid}")
    public Mono<Branch> update(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid,
            @Valid @RequestBody BranchRequest request) {
        return branchService.update(franchiseUuid, branchUuid, request);
    }

    @DeleteMapping("/{branchUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid) {
        return branchService.delete(franchiseUuid, branchUuid);
    }
}
