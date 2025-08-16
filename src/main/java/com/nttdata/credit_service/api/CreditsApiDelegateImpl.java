package com.nttdata.credit_service.api;

import com.nttdata.credit.api.CreditsApiDelegate;
import com.nttdata.credit.model.*;
import com.nttdata.credit_service.service.CreditService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


 //Implementación del contrato OpenAPI para créditos
@Service
public class CreditsApiDelegateImpl  implements CreditsApiDelegate {

    private final CreditService service;
    public CreditsApiDelegateImpl(CreditService service) { this.service = service; }

    @Override
    public Mono<ResponseEntity<Credit>> registerCredit(Mono<CreditRequest> body, ServerWebExchange ex) {
        return body.flatMap(service::create)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }

    @Override
    public Mono<ResponseEntity<Flux<Credit>>> listCredits(ServerWebExchange ex) {
        // Firma con Flux según tu interfaz generada
        return Mono.just(ResponseEntity.ok(service.findAll()));
    }

    @Override
    public Mono<ResponseEntity<Credit>> getCredit(String id, ServerWebExchange ex) {
        return service.getById(id).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Credit>> updateCredit(String id, Mono<CreditUpdateRequest> body, ServerWebExchange ex) {
        return body.flatMap(req -> service.update(id, req))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> "NOT_FOUND".equals(e.getMessage()),
                        e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCredit(String id, ServerWebExchange ex) {
        return service.delete(id).thenReturn(ResponseEntity.noContent().build());
    }
}
