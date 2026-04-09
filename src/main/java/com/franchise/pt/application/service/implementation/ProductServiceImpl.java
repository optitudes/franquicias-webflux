package com.franchise.pt.application.service.implementation;

import com.franchise.pt.application.service.interfaces.ProductService;
import com.franchise.pt.domain.model.Product;
import com.franchise.pt.application.repository.ProductRepository;
import com.franchise.pt.infrastructure.inbound.api.dto.ProductRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> create(String branchUuid, ProductRequest request) {
        Product product = Product.builder()
                .uuid(UUID.randomUUID().toString())
                .branchUuid(branchUuid)
                .name(request.getName())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Flux<Product> findAllByBranch(String branchUuid) {
        return productRepository.findAllByBranchUuid(branchUuid);
    }

    @Override
    public Mono<Product> findByUuid(String branchUuid, String productUuid) {
        return productRepository.findByUuid(branchUuid, productUuid)
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Product not found")));
    }

    @Override
    public Mono<Product> update(String branchUuid, String productUuid, ProductRequest request) {
        return findByUuid(branchUuid, productUuid)
                .map(existing -> {
                    existing.setName(request.getName());
                    if (request.getStock() != null) {
                        existing.setStock(request.getStock());
                    }
                    return existing;
                })
                .flatMap(productRepository::save);
    }

    @Override
    public Mono<Void> delete(String branchUuid, String productUuid) {
        return findByUuid(branchUuid, productUuid)
                .flatMap(p -> productRepository.deleteByUuid(branchUuid, productUuid));
    }
}
