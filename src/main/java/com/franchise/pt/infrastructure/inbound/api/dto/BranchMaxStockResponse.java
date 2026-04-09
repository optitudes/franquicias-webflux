package com.franchise.pt.infrastructure.inbound.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchMaxStockResponse {
    private String productUuid;
    private String name;
    private Integer stock;
    private String branchUuid;
    private String branchName;
}
