package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.ProductService;
import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.inbound.api.dto.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/branches/{branchUuid}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(
            @PathVariable String branchUuid,
            @Valid @RequestBody ProductRequest request) {
        return productService.create(branchUuid, request);
    }

    @GetMapping
    public Flux<Product> getAll(@PathVariable String branchUuid) {
        return productService.findAllByBranch(branchUuid);
    }

    @GetMapping("/{productUuid}")
    public Mono<Product> getById(
            @PathVariable String branchUuid,
            @PathVariable String productUuid) {
        return productService.findByUuid(branchUuid, productUuid);
    }

    @PutMapping("/{productUuid}")
    public Mono<Product> update(
            @PathVariable String branchUuid,
            @PathVariable String productUuid,
            @Valid @RequestBody ProductRequest request) {
        return productService.update(branchUuid, productUuid, request);
    }

    @DeleteMapping("/{productUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(
            @PathVariable String branchUuid,
            @PathVariable String productUuid) {
        return productService.delete(branchUuid, productUuid);
    }
}
