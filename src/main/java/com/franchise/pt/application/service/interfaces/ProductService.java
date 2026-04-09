package com.franchise.pt.application.service.interfaces;

import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.adapter.in.web.dto.ProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> create(String branchUuid, ProductRequest request);
    Flux<Product> findAllByBranch(String branchUuid);
    Mono<Product> findByUuid(String branchUuid, String productUuid);
    Mono<Product> update(String branchUuid, String productUuid, ProductRequest request);
    Mono<Void> delete(String branchUuid, String productUuid);
}
