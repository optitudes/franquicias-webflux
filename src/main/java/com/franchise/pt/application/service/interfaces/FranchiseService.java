package com.franchise.pt.application.service.interfaces;

import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.infrastructure.inbound.api.dto.FranchiseRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {
    Mono<Franchise> create(FranchiseRequest request);
    Flux<Franchise> findAll();
    Mono<Franchise> findByUuid(String uuid);
    Mono<Franchise> update(String uuid, FranchiseRequest request);
    Mono<Void> delete(String uuid);
}
