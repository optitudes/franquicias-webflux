package com.franchise.pt.infrastructure.outbound.dynamodb.repository;

import com.franchise.pt.application.repository.FranchiseRepository;
import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.FranchiseEntity;
import com.franchise.pt.infrastructure.outbound.dynamodb.mapper.FranchiseMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

@Repository
public class FranchiseDynamoDbRepository implements FranchiseRepository {

    private final DynamoDbAsyncTable<FranchiseEntity> table;

    public FranchiseDynamoDbRepository(
            DynamoDbEnhancedAsyncClient enhancedAsyncClient,
            @Value("${aws.dynamodb.table-name:FranchisePlatform}") String tableName) {
        this.table = enhancedAsyncClient.table(tableName, TableSchema.fromBean(FranchiseEntity.class));
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = FranchiseMapper.toEntity(franchise);
        return Mono.fromFuture(table.putItem(entity))
                .thenReturn(franchise);
    }

    @Override
    public Mono<Franchise> findByUuid(String uuid) {
        Key key = Key.builder()
                .partitionValue("FRANCHISE#" + uuid)
                .sortValue("METADATA")
                .build();
        return Mono.fromFuture(table.getItem(key))
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return Flux.from(table.scan(ScanEnhancedRequest.builder().build()))
                .flatMap(page -> Flux.fromIterable(page.items()))
                .filter(entity -> entity.getSk() != null && entity.getSk().equals("METADATA"))
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteByUuid(String uuid) {
        Key key = Key.builder()
                .partitionValue("FRANCHISE#" + uuid)
                .sortValue("METADATA")
                .build();
        return Mono.fromFuture(table.deleteItem(key)).then();
    }
}
