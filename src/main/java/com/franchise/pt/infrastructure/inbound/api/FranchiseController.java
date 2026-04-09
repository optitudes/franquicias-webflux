package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.FranchiseService;
import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.inbound.api.dto.BranchMaxStockResponse;
import com.franchise.pt.infrastructure.inbound.api.dto.FranchiseRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises")
@Tag(name = "Franchises", description = "Endpoints para la gestión de Franquicias")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear Franquicia", description = "Crea una nueva franquicia y retorna la entidad creada.")
    public Mono<Franchise> create(@Valid @RequestBody FranchiseRequest request) {
        return franchiseService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar Franquicias", description = "Obtiene todas las franquicias registradas.")
    public Flux<Franchise> getAll() {
        return franchiseService.findAll();
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Obtener Franquicia por UUID", description = "Retorna el detalle de una franquicia dado su identificador único.")
    public Mono<Franchise> getById(@PathVariable String uuid) {
        return franchiseService.findByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Actualizar Franquicia", description = "Actualiza el nombre de una franquicia existente dado su UUID.")
    public Mono<Franchise> update(@PathVariable String uuid, @Valid @RequestBody FranchiseRequest request) {
        return franchiseService.update(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar Franquicia", description = "Elimina permanentemente una franquicia por su UUID.")
    public Mono<Void> delete(@PathVariable String uuid) {
        return franchiseService.delete(uuid);
    }

    @GetMapping("/{uuid}/branches/products/max-stock")
    @Operation(summary = "Obtener productos con mayor stock", description = "Retorna el producto con mayor stock por cada sucursal para la franquicia especificada.")
    public Flux<BranchMaxStockResponse> getMaxStockProductsPerBranch(@PathVariable String uuid) {
        return franchiseService.findMaxStockProductsPerBranch(uuid);
    }
}
