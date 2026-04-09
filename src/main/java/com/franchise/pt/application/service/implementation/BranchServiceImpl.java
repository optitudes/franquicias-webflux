package com.franchise.pt.application.service.implementation;

import com.franchise.pt.application.service.interfaces.BranchService;
import com.franchise.pt.domain.model.Branch;
import com.franchise.pt.domain.repository.BranchRepository;
import com.franchise.pt.domain.repository.FranchiseRepository;
import com.franchise.pt.infrastructure.adapter.in.web.dto.BranchRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public BranchServiceImpl(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    @Override
    public Mono<Branch> create(String franchiseUuid, BranchRequest request) {
        return franchiseRepository.findByUuid(franchiseUuid)
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .uuid(UUID.randomUUID().toString())
                            .franchiseUuid(franchiseUuid)
                            .name(request.getName())
                            .build();
                    return branchRepository.save(branch);
                });
    }

    @Override
    public Flux<Branch> findAllByFranchise(String franchiseUuid) {
        return branchRepository.findAllByFranchiseUuid(franchiseUuid);
    }

    @Override
    public Mono<Branch> findByUuid(String franchiseUuid, String branchUuid) {
        return branchRepository.findByUuid(franchiseUuid, branchUuid)
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Branch not found")));
    }

    @Override
    public Mono<Branch> update(String franchiseUuid, String branchUuid, BranchRequest request) {
        return findByUuid(franchiseUuid, branchUuid)
                .map(existing -> {
                    existing.setName(request.getName());
                    return existing;
                })
                .flatMap(branchRepository::save);
    }

    @Override
    public Mono<Void> delete(String franchiseUuid, String branchUuid) {
        return findByUuid(franchiseUuid, branchUuid)
                .flatMap(b -> branchRepository.deleteByUuid(franchiseUuid, branchUuid));
    }
}
