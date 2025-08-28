package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.repository.CreditRepository;
import com.nttdata.credit_service.service.mapper.CreditMapper;
import com.nttdata.credit_service.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Implementación del servicio de créditos con programación reactiva
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private static final Logger log = LoggerFactory.getLogger(CreditServiceImpl.class);

    // Mensajes de error
    private static final String ERR_NOT_FOUND     = "NOT_FOUND";
    private static final String ERR_DUP_PERSONAL  = "PERSONAL_CREDIT_ALREADY_EXISTS";
    private static final String ERR_INVALID_AMT   = "INVALID_AMOUNT";
    private static final String ERR_LIMIT_EXCEEDED= "LIMIT_EXCEEDED";
    private static final String ERR_INVALID_LIMIT = "INVALID_LIMIT";

    // Repositorio reactivo que maneja la persistencia
    private final CreditRepository repo;

    // Crear un nuevo crédito
    @Override
    public Mono<Credit> create(CreditRequest req) {
        // Validaciones básicas de campos obligatorios
        if (isBlank(req.getCustomerId())) return Mono.error(new IllegalArgumentException("customerId required"));
        if (req.getType() == null)        return Mono.error(new IllegalArgumentException("type required"));
        if (nz(req.getLimit()) < 0)       return Mono.error(new IllegalArgumentException("limit must be >= 0"));
        if (nz(req.getInterestAnnual()) < 0) return Mono.error(new IllegalArgumentException("interestAnnual must be >= 0"));

        // Un cliente solo puede tener un crédito personal activo
        Mono<Void> rule = req.getType() == CreditType.PERSONAL
                ? repo.existsByCustomerIdAndTypeAndStatus(req.getCustomerId(), CreditType.PERSONAL, CreditStatus.ACTIVE)
                .flatMap(exists -> exists ? Mono.error(new IllegalStateException(ERR_DUP_PERSONAL)) : Mono.empty())
                : Mono.empty();

        // Guardar el crédito si pasó las validaciones
        return rule.then(repo.save(CreditMapper.toDoc(req)))
                .map(CreditMapper::toApi)
                .doOnSuccess(c -> log.info("credit created id={} customer={}", c.getId(), c.getCustomerId()));
    }

    // Listar todos los créditos, opcionalmente filtrados por customerId
    @Override
    public Flux<Credit> findAll(String customerId) {
        return (isBlank(customerId) ? repo.findAll() : repo.findByCustomerId(customerId))
                .map(CreditMapper::toApi);
    }

    // Buscar un crédito por ID
    @Override
    public Mono<Credit> getById(String id) {
        return repo.findById(id).map(CreditMapper::toApi);
    }

    // Actualizar un crédito existente
    @Override
    public Mono<Credit> update(String id, CreditUpdateRequest req) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERR_NOT_FOUND)))
                .flatMap(doc -> {
                    // No permitir bajar el límite por debajo del balance actual
                    if (req.getLimit() != null && req.getLimit() < nz(doc.getBalance())) {
                        return Mono.error(new IllegalStateException(ERR_INVALID_LIMIT));
                    }
                    CreditMapper.applyUpdate(doc, req);
                    return repo.save(doc);
                })
                .map(CreditMapper::toApi)
                .doOnSuccess(c -> log.info("credit updated id={}", id));
    }

    // Eliminar un crédito por ID
    @Override
    public Mono<Void> delete(String id) {
        return repo.deleteById(id).doOnSuccess(v -> log.info("credit deleted id={}", id));
    }

    // Aplicar un pago a un crédito (reduce el balance)
    @Override
    public Mono<CreditBalance> applyPayment(String id, double amount, String note) {
        if (amount <= 0) return Mono.error(new IllegalArgumentException(ERR_INVALID_AMT));

        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERR_NOT_FOUND)))
                .flatMap(doc -> {
                    doc.setBalance(Math.max(0.0, nz(doc.getBalance()) - amount));
                    return repo.save(doc);
                })
                .map(this::toBalance)
                .doOnSuccess(b -> log.info("payment applied id={} newBalance={}", id, b.getBalance()));
    }

    // Aplicar un cargo a un crédito
    @Override
    public Mono<CreditBalance> applyCharge(String id, double amount, String merchant, String note) {
        if (amount <= 0) return Mono.error(new IllegalArgumentException(ERR_INVALID_AMT));

        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERR_NOT_FOUND)))
                .flatMap(doc -> {
                    double next = nz(doc.getBalance()) + amount;
                    if (doc.getType() == CreditType.CREDIT_CARD && next > nz(doc.getLimit())) {
                        return Mono.error(new IllegalStateException(ERR_LIMIT_EXCEEDED));
                    }
                    doc.setBalance(next);
                    return repo.save(doc);
                })
                .map(this::toBalance)
                .doOnSuccess(b -> log.info("charge applied id={} newBalance={}", id, b.getBalance()));
    }

    // Consultar balance de un crédito
    @Override
    public Mono<CreditBalance> getBalance(String id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(ERR_NOT_FOUND)))
                .map(this::toBalance);
    }

    // Construye el objeto CreditBalance (limite, balance, disponible)
    private CreditBalance toBalance(CreditDocument d) {
        double limit = nz(d.getLimit());
        double bal   = nz(d.getBalance());
        return new CreditBalance()
                .limit(limit)
                .balance(bal)
                .available(Math.max(0.0, limit - bal));
    }

    // Helpers
    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
    private static double nz(Double d) { return d == null ? 0.0 : d; }

}
