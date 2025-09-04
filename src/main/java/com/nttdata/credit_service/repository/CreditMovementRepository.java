package com.nttdata.credit_service.repository;

import com.nttdata.credit_service.model.CreditMovementDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditMovementRepository extends ReactiveMongoRepository<CreditMovementDocument, String> {
    Flux<CreditMovementDocument> findByCreditIdOrderByTxnAtDesc(String creditId);
    Flux<CreditMovementDocument> findByCreditIdAndTypeOrderByTxnAtDesc(String creditId, String type);

}
