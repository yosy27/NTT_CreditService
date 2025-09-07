//package com.nttdata.credit_service.api;
//
//import com.nttdata.credit.model.*;
//import com.nttdata.credit_service.service.CreditService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.math.BigDecimal;
//import java.net.URI;
//import java.time.OffsetDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.http.HttpStatus.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CreditsApiDelegateImplTest {
//
//    @Mock
//    private CreditService service;
//
//    @InjectMocks
//    private CreditsApiDelegateImpl api;
//
//    private final ServerWebExchange exchange = null;
//
//    private CreditResponse credit(String id) {
//        CreditResponse c = new CreditResponse();
//        c.setId(id);
//        c.setCustomerId("CUST1");
//        c.setType(CreditType.CREDIT_CARD);
//        c.setLimit(new BigDecimal("1000.00"));
//        c.setBalance(new BigDecimal("250.50"));
//        c.setStatus(CreditStatus.ACTIVE);
//        c.setCreatedAt(OffsetDateTime.now());
//        return c;
//    }
//
//    private CreditMovement movement(String id) {
//        CreditMovement m = new CreditMovement();
//        m.setId(id);
//        m.setType(CreditMovement.TypeEnum.PAYMENT);
//        m.setAmount(new BigDecimal("123.45"));
//        m.setChannel("test");
//        m.setPostedAt(OffsetDateTime.now());
//        return m;
//    }
//
//    @BeforeEach
//    void setup() {
//    }
//
//    @Test
//    void registerCredit_creates201WithLocation() {
//        CreditRequest req = new CreditRequest();
//        req.setCustomerId("CUST1");
//        req.setType(CreditType.CREDIT_CARD);
//        req.setLimit(new BigDecimal("1000"));
//
//        CreditResponse created = credit("CR1");
//
//        given(service.registerCredit(any(CreditRequest.class)))
//                .willReturn(Mono.just(created));
//
//        Mono<ResponseEntity<CreditResponse>> result =
//                api.registerCredit(Mono.just(req), exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(CREATED);
//                    assertThat(resp.getHeaders().getLocation())
//                            .isEqualTo(URI.create("/credits/CR1"));
//                    assertThat(resp.getBody()).isNotNull();
//                    assertThat(resp.getBody().getId()).isEqualTo("CR1");
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void getCredit_ok() {
//        given(service.getCredit("CR1")).willReturn(Mono.just(credit("CR1")));
//
//        Mono<ResponseEntity<CreditResponse>> result = api.getCredit("CR1", exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    assertThat(resp.getBody()).isNotNull();
//                    assertThat(resp.getBody().getId()).isEqualTo("CR1");
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void updateCredit_ok() {
//        CreditRequest req = new CreditRequest();
//        req.setType(CreditType.PERSONAL);
//        req.setLimit(new BigDecimal("2000"));
//
//        CreditResponse updated = credit("CR1");
//        updated.setType(CreditType.PERSONAL);
//        updated.setLimit(new BigDecimal("2000"));
//
//        given(service.updateCredit(eq("CR1"), any(CreditRequest.class)))
//                .willReturn(Mono.just(updated));
//
//        Mono<ResponseEntity<CreditResponse>> result =
//                api.updateCredit("CR1", Mono.just(req), exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    assertThat(resp.getBody()).isNotNull();
//                    assertThat(resp.getBody().getType()).isEqualTo(CreditType.PERSONAL);
//                    assertThat(resp.getBody().getLimit()).isEqualByComparingTo("2000");
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void deleteCredit_noContent204() {
//        given(service.deleteCredit("CR1")).willReturn(Mono.empty());
//
//        Mono<ResponseEntity<Void>> result = api.deleteCredit("CR1", exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> assertThat(resp.getStatusCode()).isEqualTo(NO_CONTENT))
//                .verifyComplete();
//    }
//
//    @Test
//    void adjustLimit_ok() {
//        InlineObject body = new InlineObject();
//        body.setNewLimit(new BigDecimal("3000"));
//        body.setReason("Risk review");
//
//        CreditResponse adjusted = credit("CR1");
//        adjusted.setLimit(new BigDecimal("3000"));
//
//        given(service.adjustLimit(eq("CR1"), eq(new BigDecimal("3000")), eq("Risk review")))
//                .willReturn(Mono.just(adjusted));
//
//        Mono<ResponseEntity<CreditResponse>> result =
//                api.adjustLimit("CR1", Mono.just(body), exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    assertThat(resp.getBody()).isNotNull();
//                    assertThat(resp.getBody().getLimit()).isEqualByComparingTo("3000");
//                })
//                .verifyComplete();
//    }
//
//
//    @Test
//    void listCredits_okFlux() {
//        CreditResponse c1 = credit("CR1");
//        CreditResponse c2 = credit("CR2");
//
//        given(service.listCredits(eq("CUST1"), eq(CreditType.CREDIT_CARD), eq(CreditStatus.ACTIVE), eq(Boolean.TRUE)))
//                .willReturn(Flux.just(c1, c2));
//
//        Mono<ResponseEntity<Flux<CreditResponse>>> wrapper =
//                api.listCredits("CUST1", CreditType.CREDIT_CARD, CreditStatus.ACTIVE, true, exchange);
//
//        StepVerifier.create(wrapper)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    StepVerifier.create(resp.getBody())
//                            .expectNext(c1, c2)
//                            .verifyComplete();
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void listCreditMovements_okFlux() {
//        CreditMovement m1 = movement("MV1");
//        CreditMovement m2 = movement("MV2");
//
//        given(service.listMovements(eq("CR1"), eq("ALL")))
//                .willReturn(Flux.just(m1, m2));
//
//        Mono<ResponseEntity<Flux<CreditMovement>>> wrapper =
//                api.listCreditMovements("CR1", "ALL", exchange);
//
//        StepVerifier.create(wrapper)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    StepVerifier.create(resp.getBody())
//                            .expectNext(m1, m2)
//                            .verifyComplete();
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void applyCharge_created201WithLocation() {
//        CreditChargeRequest req = new CreditChargeRequest();
//        req.setAmount(new BigDecimal("50.00"));
//        req.setNote("fee");
//
//        CreditMovement mv = movement("MV10");
//        mv.setType(CreditMovement.TypeEnum.CHARGE);
//
//        given(service.applyCharge(eq("CR1"), any(CreditChargeRequest.class)))
//                .willReturn(Mono.just(mv));
//
//        Mono<ResponseEntity<CreditMovement>> result =
//                api.applyCharge("CR1", Mono.just(req), exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(CREATED);
//                    assertThat(resp.getHeaders().getLocation())
//                            .isEqualTo(URI.create("/credits/CR1/movements/MV10"));
//                    assertThat(resp.getBody()).isEqualTo(mv);
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void applyPayment_created201WithLocation() {
//        CreditPaymentRequest req = new CreditPaymentRequest();
//        req.setAmount(new BigDecimal("80.00"));
//        req.setNote("payment");
//
//        CreditMovement mv = movement("MV20");
//        mv.setType(CreditMovement.TypeEnum.PAYMENT);
//
//        given(service.applyPayment(eq("CR1"), any(CreditPaymentRequest.class)))
//                .willReturn(Mono.just(mv));
//
//        Mono<ResponseEntity<CreditMovement>> result =
//                api.applyPayment("CR1", Mono.just(req), exchange);
//
//        StepVerifier.create(result)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(CREATED);
//                    assertThat(resp.getHeaders().getLocation())
//                            .isEqualTo(URI.create("/credits/CR1/movements/MV20"));
//                    assertThat(resp.getBody()).isEqualTo(mv);
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void searchCreditsByDocument_okFlux() {
//        CreditResponse c1 = credit("CR1");
//        CreditResponse c2 = credit("CR2");
//
//        given(service.searchByDocument(eq(DocumentType.DNI.getValue()), eq("12345678")))
//                .willReturn(Flux.just(c1, c2));
//
//        Mono<ResponseEntity<Flux<CreditResponse>>> wrapper =
//                api.searchCreditsByDocument(DocumentType.DNI, "12345678", exchange);
//
//        StepVerifier.create(wrapper)
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(OK);
//                    StepVerifier.create(resp.getBody())
//                            .expectNext(c1, c2)
//                            .verifyComplete();
//                })
//                .verifyComplete();
//    }
//}
