package com.franchise.pt.infrastructure.outbound.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class ProductEntity {
    private String uuid; // Partition Key (BRANCH#<uuid>)
    private String sk;   // Sort Key (PRODUCT#<uuid>)
    private String name;
    private Integer stock;
    private String productUuid;
    private String branchUuid;

    @DynamoDbPartitionKey
    public String getUuid() {
        return uuid;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }
}
