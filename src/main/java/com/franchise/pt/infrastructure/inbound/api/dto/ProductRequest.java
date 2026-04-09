package com.franchise.pt.infrastructure.inbound.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "El nombre del producto es requerido")
    private String name;
    private Integer stock;
}
