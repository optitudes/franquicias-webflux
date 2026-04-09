package com.franchise.pt.application.service.implementation;

import com.franchise.pt.application.service.interfaces.FranchiseService;
import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.application.repository.FranchiseRepository;
import com.franchise.pt.infrastructure.inbound.api.dto.FranchiseRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;

    public FranchiseServiceImpl(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    @Override
    public Mono<Franchise> create(FranchiseRequest request) {
        Franchise franchise = Franchise.builder()
                .uuid(UUID.randomUUID().toString())
                .name(request.getName())
                .build();
        return franchiseRepository.save(franchise);
    }

    @Override
    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll();
    }

    @Override
    public Mono<Franchise> findByUuid(String uuid) {
        return franchiseRepository.findByUuid(uuid)
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Franchise not found")));
    }

    @Override
    public Mono<Franchise> update(String uuid, FranchiseRequest request) {
        return findByUuid(uuid)
                .map(existing -> {
                    existing.setName(request.getName());
                    return existing;
                })
                .flatMap(franchiseRepository::save);
    }

    @Override
    public Mono<Void> delete(String uuid) {
        return findByUuid(uuid) // verify it exists
                .flatMap(f -> franchiseRepository.deleteByUuid(uuid));
    }
}
