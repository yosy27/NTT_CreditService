package com.nttdata.credit_service.repository;

import com.nttdata.credit_service.model.CreditDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para la entidad CreditDocument.
 */
@Repository
public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {
    Mono<CreditDocument> findById(String customerId);
    Flux<CreditDocument> findByCustomerId(String customerId);
    Mono<Boolean> existsByCustomerIdAndTypeAndStatus(String customerId, String type, String status);

}
