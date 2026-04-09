package com.franchise.pt.application.repository;

import com.franchise.pt.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findByUuid(String franchiseUuid, String branchUuid);
    Flux<Branch> findAllByFranchiseUuid(String franchiseUuid);
    Mono<Void> deleteByUuid(String franchiseUuid, String branchUuid);
}
