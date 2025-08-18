package com.nttdata.credit_service.api;

import com.nttdata.credit.api.CreditsApiDelegate;
import com.nttdata.credit.model.*;
import com.nttdata.credit_service.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Optional;

//Implementación del contrato OpenAPI para créditos
@Service
@RequiredArgsConstructor
public class CreditsApiDelegateImpl  implements CreditsApiDelegate {

    private static final Logger log = LoggerFactory.getLogger(CreditsApiDelegateImpl.class);
    private static final String HDR_CORR = "X-Correlation-Id";

    private final CreditService service;

    @Override
    // Registrar un nuevo crédito
    public Mono<ResponseEntity<Credit>> registerCredit(Mono<CreditRequest> creditRequest, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return creditRequest
                .flatMap(service::create)
                .map(c -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(withCorr(corrId))
                        .body(c));
    }

    @Override
    // Listar todos los créditos, opcionalmente filtrados por customerId
    public Mono<ResponseEntity<Flux<Credit>>> listCredits(String customerId, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        Flux<Credit> body = service.findAll(customerId);
        return Mono.just(ResponseEntity.ok().headers(withCorr(corrId)).body(body));
    }

    @Override
    // Obtener crédito por id
    public Mono<ResponseEntity<Credit>> getCredit(String id, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return service.getById(id)
                .map(c -> ResponseEntity.ok().headers(withCorr(corrId)).body(c))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).headers(withCorr(corrId)).build());
    }

    @Override
    // Actualizar crédito por id
    public Mono<ResponseEntity<Credit>> updateCredit(String id, Mono<CreditUpdateRequest> creditUpdateRequest, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return creditUpdateRequest
                .flatMap(req -> service.update(id, req))
                .map(c -> ResponseEntity.ok().headers(withCorr(corrId)).body(c))
                .onErrorResume(e -> "NOT_FOUND".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).headers(withCorr(corrId)).build()));
    }

    @Override
    // Eliminar crédito por id
    public Mono<ResponseEntity<Void>> deleteCredit(String id, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return service.delete(id)
                .thenReturn(new ResponseEntity<Void>(withCorr(corrId), HttpStatus.NO_CONTENT));
    }

    @Override
    // Aplicar un pago a un crédito
    public Mono<ResponseEntity<CreditBalance>> applyPayment(String id, Mono<CreditPaymentRequest> creditPaymentRequest, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return creditPaymentRequest
                .flatMap(req -> service.applyPayment(id, req.getAmount(), req.getNote()))
                .map(bal -> ResponseEntity.ok().headers(withCorr(corrId)).body(bal))
                .onErrorResume(e -> "NOT_FOUND".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).headers(withCorr(corrId)).build()));
    }

    @Override
    // Aplicar un cargo o consumo
    public Mono<ResponseEntity<CreditBalance>> applyCharge(String id, Mono<CreditChargeRequest> creditChargeRequest, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return creditChargeRequest
                .flatMap(req -> service.applyCharge(id, req.getAmount(), req.getMerchant(), req.getNote()))
                .map(bal -> ResponseEntity.ok().headers(withCorr(corrId)).body(bal))
                .onErrorResume(e -> "LIMIT_EXCEEDED".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.unprocessableEntity().headers(withCorr(corrId)).build()))
                .onErrorResume(e -> "NOT_FOUND".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).headers(withCorr(corrId)).build()));
    }

    @Override
    // Consultar balance de un crédito
    public Mono<ResponseEntity<CreditBalance>> getBalance(String id, ServerWebExchange exchange) {
        final String corrId = resolveCorrId(exchange);
        return service.getBalance(id)
                .map(bal -> ResponseEntity.ok().headers(withCorr(corrId)).body(bal))
                .onErrorResume(e -> "NOT_FOUND".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).headers(withCorr(corrId)).build()));
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return CreditsApiDelegate.super.getRequest();
    }

    // ------- helpers -------
    // Agregar corrId a las respuestas
    private HttpHeaders withCorr(String corrId) {
        HttpHeaders h = new HttpHeaders();
        if (corrId != null) h.add(HDR_CORR, corrId);
        return h;
    }

    // Resolver corrId de la petición entrante, o usar el reqId de WebFlux
    private String resolveCorrId(ServerWebExchange ex) {
        String id = ex.getRequest().getHeaders().getFirst(HDR_CORR);
        return id != null ? id : ex.getRequest().getId();
    }
}
