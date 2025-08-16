package com.nttdata.credit_service.service;

import com.nttdata.credit.model.Credit;
import com.nttdata.credit.model.CreditRequest;
import com.nttdata.credit.model.CreditUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

 //Servicio de dominio para la gestión de créditos.
public interface CreditService {
    Mono<Credit> create(CreditRequest req);
    Flux<Credit> findAll();
    Mono<Credit> getById(String id);
    Mono<Credit> update(String id, CreditUpdateRequest req);
    Mono<Void> delete(String id);
}
