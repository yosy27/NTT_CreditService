//package com.nttdata.credit_service.api;
//
//import com.nttdata.credit.api.CreditsApiDelegate;
//import com.nttdata.credit.model.*;
//import com.nttdata.credit_service.service.CreditService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//import java.util.Optional;
//
////Implementación del contrato OpenAPI para créditos
//@Component
//@RequiredArgsConstructor
//public class CreditsApiDelegateImpl  implements CreditsApiDelegate {
//
//    private static final Logger log = LoggerFactory.getLogger(CreditsApiDelegateImpl.class);
//
//    private final CreditService service;
//
//    @Override
//    public Optional<NativeWebRequest> getRequest() {
//        return CreditsApiDelegate.super.getRequest();
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> adjustLimit(String id, Mono<InlineObject> inlineObject, ServerWebExchange exchange) {
//        return inlineObject
//                .flatMap(body -> service.adjustLimit(id, body.getNewLimit(), body.getReason()))
//                .map(ResponseEntity::ok);    }
//
//    @Override
//    public Mono<ResponseEntity<CreditMovement>> applyCharge(String id, Mono<CreditChargeRequest> creditChargeRequest, ServerWebExchange exchange) {
//        return creditChargeRequest
//                .flatMap(body -> service.applyCharge(id, body))
//                .map(mv -> ResponseEntity
//                        .created(location("/credits/" + id + "/movements/" + mv.getId()))
//                        .body(mv)
//                );
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditMovement>> applyPayment(String id, Mono<CreditPaymentRequest> creditPaymentRequest, ServerWebExchange exchange) {
//        return creditPaymentRequest
//                .flatMap(body -> service.applyPayment(id, body))
//                .map(mv -> ResponseEntity
//                        .created(location("/credits/" + id + "/movements/" + mv.getId()))
//                        .body(mv)
//                );
//    }
//
//    @Override
//    public Mono<ResponseEntity<OverdueStatus>> getDebtStatusByCustomer(String customerId, ServerWebExchange exchange) {
//        return service.getDebtStatusByCustomer(customerId)
//                .map(ResponseEntity::ok);
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> closeCredit(String id, Mono<CreditCloseRequest> creditCloseRequest, ServerWebExchange exchange) {
//        return creditCloseRequest
//                .defaultIfEmpty(new CreditCloseRequest())
//                .flatMap(body -> service.closeCredit(id, body.getReason()))
//                .map(ResponseEntity::ok);    }
//
//    @Override
//    public Mono<ResponseEntity<CreditBalance>> getBalance(String id, ServerWebExchange exchange) {
//        return service.getBalance(id)
//                .map(ResponseEntity::ok);
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> getCredit(String id, ServerWebExchange exchange) {
//        return service.getCredit(id)
//                .map(ResponseEntity::ok);
//    }
//
//
//    @Override
//    public Mono<ResponseEntity<Flux<CreditMovement>>> listCreditMovements(String id, String type, ServerWebExchange exchange) {
//        Flux<CreditMovement> stream = service.listMovements(id, type);
//        return Mono.just(ResponseEntity.ok(stream));
//    }
//
//    @Override
//    public Mono<ResponseEntity<Flux<CreditResponse>>> listCredits(String customerId, CreditType type, CreditStatus status, Boolean includeClosed, ServerWebExchange exchange) {
//        Flux<CreditResponse> stream = service.listCredits(customerId, type, status, includeClosed);
//        return Mono.just(ResponseEntity.ok(stream));    }
//
//    @Override
//    public Mono<ResponseEntity<Void>> deleteCredit(String id, ServerWebExchange exchange) {
//        return service.deleteCredit(id)
//                .thenReturn(ResponseEntity.noContent().build());
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> updateCredit(String id, Mono<CreditRequest> creditRequest, ServerWebExchange exchange) {
//        return creditRequest
//                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(
//                        org.springframework.http.HttpStatus.BAD_REQUEST, "body is required")))
//                .flatMap(req -> service.updateCredit(id, req))
//                .map(ResponseEntity::ok);
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> patchCredit(String id, Mono<CreditUpdate> creditUpdate, ServerWebExchange exchange) {
//        return creditUpdate
//                .flatMap(patch -> service.patchCredit(id, patch))
//                .map(ResponseEntity::ok);
//    }
//
//    @Override
//    public Mono<ResponseEntity<CreditResponse>> registerCredit(Mono<CreditRequest> creditCreate, ServerWebExchange exchange) {
//        return creditCreate
//                .flatMap(service::registerCredit)
//                .map(created -> ResponseEntity
//                        .created(location("/credits/" + created.getId()))
//                        .body(created)
//                );
//    }
//
//    @Override
//    public Mono<ResponseEntity<Flux<CreditResponse>>> searchCreditsByDocument(DocumentType documentType, String documentNumber, ServerWebExchange exchange) {
//        String typeStr = (documentType != null) ? documentType.getValue() : null;
//        Flux<CreditResponse> stream = service.searchByDocument(typeStr, documentNumber);
//        return Mono.just(ResponseEntity.ok(stream));
//    }
//
//
//    private static URI location(String path) {
//        // Devolvemos Location relativo; si prefieres absoluto, compón desde exchange.getRequest().getURI()
//        return URI.create(path);
//    }
//}
