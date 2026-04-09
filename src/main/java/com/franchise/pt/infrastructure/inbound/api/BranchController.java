package com.franchise.pt.infrastructure.inbound.api;

import com.franchise.pt.application.service.interfaces.BranchService;
import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.infrastructure.inbound.api.dto.BranchRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/franchises/{franchiseUuid}/branches")
@Tag(name = "Branches", description = "Endpoints para la gestión de Sucursales")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear Sucursal", description = "Crea una nueva sucursal asignada a una franquicia específica.")
    public Mono<Branch> create(
            @PathVariable String franchiseUuid,
            @Valid @RequestBody BranchRequest request) {
        return branchService.create(franchiseUuid, request);
    }

    @GetMapping
    @Operation(summary = "Listar Sucursales", description = "Obtiene todas las sucursales correspondientes a una franquicia.")
    public Flux<Branch> getAll(@PathVariable String franchiseUuid) {
        return branchService.findAllByFranchise(franchiseUuid);
    }

    @GetMapping("/{branchUuid}")
    @Operation(summary = "Obtener Sucursal", description = "Retorna el detalle de una sucursal dado su UUID y el de su franquicia.")
    public Mono<Branch> getById(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid) {
        return branchService.findByUuid(franchiseUuid, branchUuid);
    }

    @PutMapping("/{branchUuid}")
    @Operation(summary = "Actualizar Sucursal", description = "Modifica los datos de una sucursal existente.")
    public Mono<Branch> update(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid,
            @Valid @RequestBody BranchRequest request) {
        return branchService.update(franchiseUuid, branchUuid, request);
    }

    @DeleteMapping("/{branchUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar Sucursal", description = "Elimina permanentemente una sucursal por su UUID.")
    public Mono<Void> delete(
            @PathVariable String franchiseUuid,
            @PathVariable String branchUuid) {
        return branchService.delete(franchiseUuid, branchUuid);
    }
}
