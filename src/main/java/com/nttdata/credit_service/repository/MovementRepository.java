package com.nttdata.credit_service.repository;

import com.nttdata.credit_service.domain.MovementDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<MovementDocument, String> {
    Flux<MovementDocument> findByCreditIdOrderByTxnAtDesc(String creditId);
    Flux<MovementDocument> findByCreditIdAndTypeOrderByTxnAtDesc(String creditId, String type);
}
