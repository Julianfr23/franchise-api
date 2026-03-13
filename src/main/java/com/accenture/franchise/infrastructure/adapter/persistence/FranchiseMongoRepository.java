package com.accenture.franchise.infrastructure.adapter.persistence;

import com.accenture.franchise.infrastructure.entity.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseMongoRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
