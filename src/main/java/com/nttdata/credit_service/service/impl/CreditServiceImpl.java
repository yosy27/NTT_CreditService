package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.persistence.CreditDocument;
import com.nttdata.credit_service.persistence.CreditRepository;
import com.nttdata.credit_service.service.CreditMapper;
import com.nttdata.credit_service.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Implementación del servicio de créditos.
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository repo;

    @Override
    public Mono<Credit> create(CreditRequest req) {
        Mono<Void> rule =
                CreditType.PERSONAL.equals(req.getType())
                        ? repo.existsByCustomerIdAndTypeAndStatus(req.getCustomerId(), CreditType.PERSONAL, CreditStatus.ACTIVE)
                        .flatMap(exists -> exists
                                ? Mono.error(new IllegalStateException("PERSONAL_CREDIT_ALREADY_EXISTS"))
                                : Mono.empty())
                        : Mono.empty();

        return rule.then(
                repo.save(CreditMapper.toDoc(req)).map(CreditMapper::toApi)
        );
    }

    @Override
    public Flux<Credit> findAll() {
        return repo.findAll().map(CreditMapper::toApi);
    }

    @Override
    public Mono<Credit> getById(String id) {
        return repo.findById(id).map(CreditMapper::toApi);
    }

    @Override
    public Mono<Credit> update(String id, CreditUpdateRequest req) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("NOT_FOUND")))
                .flatMap(doc -> {
                    CreditMapper.applyUpdate(doc, req);
                    return repo.save(doc);
                })
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repo.deleteById(id);
    }
}
