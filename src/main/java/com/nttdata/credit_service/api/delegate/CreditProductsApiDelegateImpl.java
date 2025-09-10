package com.nttdata.credit_service.api.delegate;


import com.nttdata.credit.model.*;
import com.nttdata.credit_service.api.CommonApiDelegate;
import com.nttdata.credit_service.service.CreditProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CreditProductsApiDelegateImpl implements CommonApiDelegate{

    private final CreditProductQueryService query;

    @Override
    public Mono<ResponseEntity<OverdueStatus>> getDebtStatusByCustomer(String customerId, ServerWebExchange exchange) {
        return query.getDebtStatusByCustomer(customerId)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditProductSummary>> getCreditProduct(String id, ServerWebExchange exchange) {
        return query.getCreditProduct(id)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditBalance>> getCreditProductBalance(String id, ServerWebExchange exchange) {
        return query.getProductBalance(id)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditMovement>>> listCreditProductMovements(String id, String type, Integer limit, ServerWebExchange exchange) {
        Flux<CreditMovement> stream = query.listProductMovements(id, type, limit);
        return Mono.just(ResponseEntity.ok(stream));
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditProductSummary>>> listCreditProductsByCustomer(String customerId, String kind, CreditStatus status, ServerWebExchange exchange) {
        Flux<CreditProductSummary> stream =
                query.listCreditProductsByCustomer(customerId, kind, status);
        return Mono.just(ResponseEntity.ok(stream));
    }


}
