package com.franchise.pt.application.repository;

import com.franchise.pt.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findByUuid(String branchUuid, String productUuid);
    Flux<Product> findAllByBranchUuid(String branchUuid);
    Mono<Void> deleteByUuid(String branchUuid, String productUuid);
}
