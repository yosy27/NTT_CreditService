package com.nttdata.credit_service.repository;

import com.nttdata.credit_service.domain.CreditLineDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditLineRepository extends ReactiveMongoRepository<CreditLineDocument, String> {
    Flux<CreditLineDocument> findByCustomerId(String customerId);

}
