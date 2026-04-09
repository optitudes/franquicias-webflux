package com.franchise.pt.domain.repository;

import com.franchise.pt.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findByUuid(String uuid);
    Flux<Franchise> findAll();
    Mono<Void> deleteByUuid(String uuid);
}
