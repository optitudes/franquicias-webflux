package com.franchise.pt.infrastructure.outbound.dynamodb.repository;

import com.franchise.pt.application.repository.BranchRepository;
import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.BranchEntity;
import com.franchise.pt.infrastructure.outbound.dynamodb.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
public class BranchDynamoDbRepository implements BranchRepository {

    private final DynamoDbAsyncTable<BranchEntity> table;

    public BranchDynamoDbRepository(
            DynamoDbEnhancedAsyncClient enhancedAsyncClient,
            @Value("${aws.dynamodb.table-name:FranchisePlatform}") String tableName) {
        this.table = enhancedAsyncClient.table(tableName, TableSchema.fromBean(BranchEntity.class));
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = BranchMapper.toEntity(branch);
        return Mono.fromFuture(table.putItem(entity))
                .thenReturn(branch);
    }

    @Override
    public Mono<Branch> findByUuid(String franchiseUuid, String branchUuid) {
        Key key = Key.builder()
                .partitionValue("FRANCHISE#" + franchiseUuid)
                .sortValue("BRANCH#" + branchUuid)
                .build();
        return Mono.fromFuture(table.getItem(key))
                .map(BranchMapper::toDomain);
    }

    @Override
    public Flux<Branch> findAllByFranchiseUuid(String franchiseUuid) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(
                Key.builder()
                        .partitionValue("FRANCHISE#" + franchiseUuid)
                        .sortValue("BRANCH#")
                        .build()
        );

        return Flux.from(table.query(queryConditional))
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(BranchMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteByUuid(String franchiseUuid, String branchUuid) {
        Key key = Key.builder()
                .partitionValue("FRANCHISE#" + franchiseUuid)
                .sortValue("BRANCH#" + branchUuid)
                .build();
        return Mono.fromFuture(table.deleteItem(key)).then();
    }
}
