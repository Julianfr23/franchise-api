package com.accenture.franchise.infrastructure.adapter.persistence;

import com.accenture.franchise.domain.model.Franchise;
import com.accenture.franchise.domain.repository.FranchiseRepository;
import com.accenture.franchise.infrastructure.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseMongoRepository mongoRepository;
    private final FranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return mongoRepository.save(mapper.toDocument(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return mongoRepository.findAll()
                .map(mapper::toDomain);
    }
}
