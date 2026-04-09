package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.ProductService;
import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.inbound.api.dto.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/branches/{branchUuid}/products")
@Tag(name = "Products", description = "Endpoints para la gestión de Productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear Producto", description = "Añade un nuevo producto a una sucursal específica con su respectivo stock inicial.")
    public Mono<Product> create(
            @PathVariable String branchUuid,
            @Valid @RequestBody ProductRequest request) {
        return productService.create(branchUuid, request);
    }

    @GetMapping
    @Operation(summary = "Listar Productos", description = "Obtiene todos los productos asignados a una sucursal.")
    public Flux<Product> getAll(@PathVariable String branchUuid) {
        return productService.findAllByBranch(branchUuid);
    }

    @GetMapping("/{productUuid}")
    @Operation(summary = "Obtener Producto", description = "Retorna el detalle exacto de un producto.")
    public Mono<Product> getById(
            @PathVariable String branchUuid,
            @PathVariable String productUuid) {
        return productService.findByUuid(branchUuid, productUuid);
    }

    @PutMapping("/{productUuid}")
    @Operation(summary = "Actualizar Producto", description = "Actualiza el nombre o modifica la cantidad del stock (inventario) de un producto existente.")
    public Mono<Product> update(
            @PathVariable String branchUuid,
            @PathVariable String productUuid,
            @Valid @RequestBody ProductRequest request) {
        return productService.update(branchUuid, productUuid, request);
    }

    @DeleteMapping("/{productUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar Producto", description = "Borra un producto del catálogo de la sucursal.")
    public Mono<Void> delete(
            @PathVariable String branchUuid,
            @PathVariable String productUuid) {
        return productService.delete(branchUuid, productUuid);
    }
}
