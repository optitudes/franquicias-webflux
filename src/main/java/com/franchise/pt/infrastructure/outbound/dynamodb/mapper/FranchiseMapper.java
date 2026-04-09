package com.franchise.pt.infrastructure.outbound.dynamodb.mapper;

import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.FranchiseEntity;

public class FranchiseMapper {
    
    private static final String PK_PREFIX = "FRANCHISE#";
    private static final String SK_METADATA = "METADATA";

    public static FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseEntity.builder()
                .uuid(PK_PREFIX + franchise.getUuid())
                .sk(SK_METADATA)
                .franchiseUuid(franchise.getUuid())
                .name(franchise.getName())
                .build();
    }

    public static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .uuid(entity.getFranchiseUuid())
                .name(entity.getName())
                .build();
    }
}
