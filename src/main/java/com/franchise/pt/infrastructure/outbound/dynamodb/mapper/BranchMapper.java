package com.franchise.pt.infrastructure.outbound.dynamodb.mapper;

import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.infrastructure.outbound.dynamodb.entity.BranchEntity;

public class BranchMapper {

    private static final String PK_PREFIX = "FRANCHISE#";
    private static final String SK_PREFIX = "BRANCH#";

    public static BranchEntity toEntity(Branch branch) {
        if (branch == null) return null;
        return BranchEntity.builder()
                .uuid(PK_PREFIX + branch.getFranchiseUuid())
                .sk(SK_PREFIX + branch.getUuid())
                .branchUuid(branch.getUuid())
                .franchiseUuid(branch.getFranchiseUuid())
                .name(branch.getName())
                .build();
    }

    public static Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;
        return Branch.builder()
                .uuid(entity.getBranchUuid())
                .name(entity.getName())
                .franchiseUuid(entity.getFranchiseUuid())
                .build();
    }
}
