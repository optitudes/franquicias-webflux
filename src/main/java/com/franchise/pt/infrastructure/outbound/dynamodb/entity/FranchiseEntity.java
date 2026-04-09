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
public class FranchiseEntity {
    private String uuid; // Partition Key
    private String sk;   // Sort Key
    private String name;
    
    // Original UUID for the domain model
    private String franchiseUuid;

    @DynamoDbPartitionKey
    public String getUuid() {
        return uuid;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }
}
