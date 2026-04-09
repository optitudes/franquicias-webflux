package com.franchise.pt.infrastructure.outbound.dynamodb.repository;

import com.franchise.pt.application.repository.ProductRepository;
import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.ProductEntity;
import com.franchise.pt.infrastructure.outbound.dynamodb.mapper.ProductMapper;
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
public class ProductDynamoDbRepository implements ProductRepository {

    private final DynamoDbAsyncTable<ProductEntity> table;

    public ProductDynamoDbRepository(
            DynamoDbEnhancedAsyncClient enhancedAsyncClient,
            @Value("${aws.dynamodb.table-name:FranchisePlatform}") String tableName) {
        this.table = enhancedAsyncClient.table(tableName, TableSchema.fromBean(ProductEntity.class));
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        return Mono.fromFuture(table.putItem(entity))
                .thenReturn(product);
    }

    @Override
    public Mono<Product> findByUuid(String branchUuid, String productUuid) {
        Key key = Key.builder()
                .partitionValue("BRANCH#" + branchUuid)
                .sortValue("PRODUCT#" + productUuid)
                .build();
        return Mono.fromFuture(table.getItem(key))
                .map(ProductMapper::toDomain);
    }

    @Override
    public Flux<Product> findAllByBranchUuid(String branchUuid) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(
                Key.builder()
                        .partitionValue("BRANCH#" + branchUuid)
                        .sortValue("PRODUCT#")
                        .build()
        );

        return Flux.from(table.query(queryConditional))
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteByUuid(String branchUuid, String productUuid) {
        Key key = Key.builder()
                .partitionValue("BRANCH#" + branchUuid)
                .sortValue("PRODUCT#" + productUuid)
                .build();
        return Mono.fromFuture(table.deleteItem(key)).then();
    }
}
