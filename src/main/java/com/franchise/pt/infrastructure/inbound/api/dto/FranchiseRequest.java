package com.franchise.pt.infrastructure.inbound.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FranchiseRequest {
    @NotBlank(message = "El nombre de la franquicia es requerido")
    private String name;
}
