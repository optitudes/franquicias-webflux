package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.FranchiseService;
import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.infrastructure.inbound.api.dto.FranchiseRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> create(@Valid @RequestBody FranchiseRequest request) {
        return franchiseService.create(request);
    }

    @GetMapping
    public Flux<Franchise> getAll() {
        return franchiseService.findAll();
    }

    @GetMapping("/{uuid}")
    public Mono<Franchise> getById(@PathVariable String uuid) {
        return franchiseService.findByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    public Mono<Franchise> update(@PathVariable String uuid, @Valid @RequestBody FranchiseRequest request) {
        return franchiseService.update(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String uuid) {
        return franchiseService.delete(uuid);
    }
}
