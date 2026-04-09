package com.franchise.pt.application.service.implementation;

import com.franchise.pt.application.service.interfaces.FranchiseService;
import com.franchise.pt.domain.model.Franchise;
import com.franchise.pt.application.repository.FranchiseRepository;
import com.franchise.pt.application.repository.BranchRepository;
import com.franchise.pt.application.repository.ProductRepository;
import com.franchise.pt.infrastructure.inbound.api.dto.BranchMaxStockResponse;
import com.franchise.pt.infrastructure.inbound.api.dto.FranchiseRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public FranchiseServiceImpl(FranchiseRepository franchiseRepository,
                                BranchRepository branchRepository,
                                ProductRepository productRepository) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
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
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Franquicia no encontrada")));
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
        return findByUuid(uuid)
                .flatMap(f -> franchiseRepository.deleteByUuid(uuid));
    }

    @Override
    public Flux<BranchMaxStockResponse> findMaxStockProductsPerBranch(String franchiseUuid) {
        return findByUuid(franchiseUuid)
                .flatMapMany(franchise -> branchRepository.findAllByFranchiseUuid(franchiseUuid))
                .flatMap(branch -> productRepository.findAllByBranchUuid(branch.getUuid())
                        .reduce((p1, p2) -> {
                            int stock1 = p1.getStock() != null ? p1.getStock() : 0;
                            int stock2 = p2.getStock() != null ? p2.getStock() : 0;
                            return stock1 >= stock2 ? p1 : p2;
                        })
                        .map(maxProduct -> BranchMaxStockResponse.builder()
                                .productUuid(maxProduct.getUuid())
                                .name(maxProduct.getName())
                                .stock(maxProduct.getStock() != null ? maxProduct.getStock() : 0)
                                .branchUuid(branch.getUuid())
                                .branchName(branch.getName())
                                .build()
                        )
                );
    }
}
