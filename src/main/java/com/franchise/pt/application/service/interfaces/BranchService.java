package com.franchise.pt.application.service.interfaces;

import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.infrastructure.inbound.api.dto.BranchRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchService {
    Mono<Branch> create(String franchiseUuid, BranchRequest request);
    Flux<Branch> findAllByFranchise(String franchiseUuid);
    Mono<Branch> findByUuid(String franchiseUuid, String branchUuid);
    Mono<Branch> update(String franchiseUuid, String branchUuid, BranchRequest request);
    Mono<Void> delete(String franchiseUuid, String branchUuid);
}
