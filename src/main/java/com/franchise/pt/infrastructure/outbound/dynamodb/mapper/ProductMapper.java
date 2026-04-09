package com.franchise.pt.infrastructure.outbound.dynamodb.mapper;

import com.franchise.pt.domain.model.Product;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.ProductEntity;

public class ProductMapper {

    private static final String PK_PREFIX = "BRANCH#";
    private static final String SK_PREFIX = "PRODUCT#";

    public static ProductEntity toEntity(Product product) {
        if (product == null) return null;
        return ProductEntity.builder()
                .uuid(PK_PREFIX + product.getBranchUuid())
                .sk(SK_PREFIX + product.getUuid())
                .productUuid(product.getUuid())
                .branchUuid(product.getBranchUuid())
                .name(product.getName())
                .stock(product.getStock() != null ? product.getStock() : 0)
                .build();
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .uuid(entity.getProductUuid())
                .name(entity.getName())
                .stock(entity.getStock() != null ? entity.getStock() : 0)
                .branchUuid(entity.getBranchUuid())
                .build();
    }
}
