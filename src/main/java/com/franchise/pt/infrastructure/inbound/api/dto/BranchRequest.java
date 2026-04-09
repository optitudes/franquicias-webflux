package com.franchise.pt.infrastructure.inbound.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchRequest {
    @NotBlank(message = "El nombre de la sucursal es requerido")
    private String name;
}
