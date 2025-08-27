package com.nttdata.credit_service.repository;

import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.CreditType;
import com.nttdata.credit_service.model.CreditDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo para la entidad CreditDocument.
 */
public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {
    Flux<CreditDocument> findByCustomerId(String customerId);
    Mono<Boolean> existsByCustomerIdAndTypeAndStatus(String customerId, CreditType type, CreditStatus status);
}
