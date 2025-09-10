package com.nttdata.credit_service.repository;

import com.nttdata.credit_service.domain.LoanDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LoanRepository extends ReactiveMongoRepository<LoanDocument, String> {

    Flux<LoanDocument> findByCustomerId(String customerId);

    Mono<Boolean> existsByCustomerIdAndTypeAndStatus(String customerId, String type, String status);
}
