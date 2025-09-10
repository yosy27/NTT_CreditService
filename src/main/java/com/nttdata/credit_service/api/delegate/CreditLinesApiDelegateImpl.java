package com.nttdata.credit_service.api.delegate;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.api.CreditLinesApiDelegate;
import com.nttdata.credit_service.service.CreditLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CreditLinesApiDelegateImpl implements com.nttdata.credit_service.api.CreditLinesApiDelegate {

    private final CreditLineService service;

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> updateCreditLine(String id, Mono<CreditLineRequest> creditLineRequest, ServerWebExchange exchange) {
        return creditLineRequest.flatMap(req -> service.updateCreditLine(id, req))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditMovement>> postCreditLinePayment(String id, Mono<CreditPaymentRequest> creditPaymentRequest, ServerWebExchange exchange) {
        return creditPaymentRequest.flatMap(b -> service.postPayment(id, b))
                .map(m -> ResponseEntity.created(URI.create("/credit-lines/" + id + "/movements/" + m.getId()))
                        .body(m));
    }

    @Override
    public Mono<ResponseEntity<CreditMovement>> postCreditLineCharge(String id, Mono<CreditChargeRequest> creditChargeRequest, ServerWebExchange exchange) {
        return creditChargeRequest.flatMap(b -> service.postCharge(id, b))
                .map(m -> ResponseEntity.created(URI.create("/credit-lines/" + id + "/movements/" + m.getId()))
                        .body(m));
    }

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> patchCreditLine(String id, Mono<CreditLineUpdate> creditLineUpdate, ServerWebExchange exchange) {
        return creditLineUpdate.flatMap(p -> service.patchCreditLine(id, p))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> getCreditLine(String id, ServerWebExchange exchange) {
        return service.getCreditLine(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditLineResponse>>> listCreditLines(String customerId, CreditStatus status, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(service.listCreditLines(customerId, status)));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCreditLine(String id, ServerWebExchange exchange) {
        return service.deleteCreditLine(id).thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> closeCreditLine(String id, ServerWebExchange exchange) {
        return service.closeCreditLine(id, null).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> createCreditLine(Mono<CreditLineRequest> creditLineRequest, ServerWebExchange exchange) {
        return creditLineRequest.flatMap(service::createCreditLine)
                .map(created -> ResponseEntity.created(URI.create("/credit-lines/" + created.getId()))
                        .body(created));
    }

    @Override
    public Mono<ResponseEntity<CreditLineResponse>> adjustCreditLineLimit(String id, Mono<AdjustCreditLineLimitRequest> adjustCreditLineLimitRequest, ServerWebExchange exchange) {
        return adjustCreditLineLimitRequest.flatMap(b -> service.adjustCreditLineLimit(id, b.getNewLimit(), b.getReason()))
               .map(ResponseEntity::ok);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return CreditLinesApiDelegate.super.getRequest();
    }
}
