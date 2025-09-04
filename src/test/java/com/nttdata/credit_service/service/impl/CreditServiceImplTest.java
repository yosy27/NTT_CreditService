package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.model.CreditMovementDocument;
import com.nttdata.credit_service.repository.CreditMovementRepository;
import com.nttdata.credit_service.repository.CreditRepository;
import com.nttdata.credit_service.service.domain.CreditLedger;
import com.nttdata.credit_service.service.mapper.CreditMapper;
import com.nttdata.credit_service.service.policy.CreditCreationPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class CreditServiceImplTest {

    CreditRepository creditRepo;
    CreditMovementRepository movementRepo;
    CustomerClient customerClient;
    CreditCreationPolicy creationPolicy;
    CreditLedger ledger;
    CreditServiceImpl service;

    @BeforeEach
    void setup() {
        creditRepo = Mockito.mock(CreditRepository.class);
        movementRepo = Mockito.mock(CreditMovementRepository.class);
        customerClient = Mockito.mock(CustomerClient.class);
        creationPolicy = Mockito.mock(CreditCreationPolicy.class);
        ledger = Mockito.mock(CreditLedger.class);

        service = new CreditServiceImpl(
                creditRepo, movementRepo, customerClient, creationPolicy, ledger
        );
    }


    @Test
    void registerCredit_badRequest_whenRequiredMissing() {
        StepVerifier.create(service.registerCredit(new CreditRequest()))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void registerCredit_badRequest_whenBothCustomerIdAndDocument() {
        var req = baseCreateReq();
        req.setCustomerId("C1");
        req.setDocumentType(DocumentType.DNI);
        req.setDocumentNumber("12345678");

        StepVerifier.create(service.registerCredit(req))
                .expectError(ResponseStatusException.class).verify();

        verifyNoInteractions(creationPolicy);
    }

    @Test
    void registerCredit_notFound_whenCustomerIdUnknown() {
        var req = baseCreateReq();
        req.setCustomerId("CXXX");

        when(customerClient.existsCustomerId("CXXX")).thenReturn(Mono.just(false));

        StepVerifier.create(service.registerCredit(req))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void registerCredit_notFound_whenDocumentUnknown() {
        var req = baseCreateReq();
        req.setDocumentType(DocumentType.DNI);
        req.setDocumentNumber("12345678");

        when(customerClient.resolveCustomerId("DNI", "12345678")).thenReturn(Mono.empty());

        StepVerifier.create(service.registerCredit(req))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void registerCredit_happy_withCustomerId() {
        var req = baseCreateReq();
        req.setCustomerId("C1");

        when(customerClient.existsCustomerId("C1")).thenReturn(Mono.just(true));
        when(creationPolicy.validate("C1", CreditType.PERSONAL)).thenReturn(Mono.empty());

        var docSaved = CreditMapper.fromCreate(req, "C1");
        docSaved.setId("ID-1");
        when(creditRepo.save(Mockito.any(CreditDocument.class))).thenReturn(Mono.just(docSaved));

        StepVerifier.create(service.registerCredit(req))
                .assertNext(res -> {
                    assertEquals("ID-1", res.getId());
                    assertEquals("C1", res.getCustomerId());
                    assertEquals(CreditType.PERSONAL, res.getType());
                })
                .verifyComplete();

        Mockito.verify(creationPolicy).validate("C1", CreditType.PERSONAL);
    }

    @Test
    void registerCredit_happy_withDocument() {
        var req = baseCreateReq();
        req.setDocumentType(DocumentType.DNI);
        req.setDocumentNumber("88888888");

        when(customerClient.resolveCustomerId("DNI", "88888888")).thenReturn(Mono.just("C9"));
        when(creationPolicy.validate("C9", CreditType.PERSONAL)).thenReturn(Mono.empty());

        var docSaved = CreditMapper.fromCreate(req, "C9");
        docSaved.setId("ID-9");
        when(creditRepo.save(Mockito.any(CreditDocument.class))).thenReturn(Mono.just(docSaved));

        StepVerifier.create(service.registerCredit(req))
                .assertNext(res -> assertEquals("ID-9", res.getId()))
                .verifyComplete();
    }


    @Test
    void getCredit_notFound() {
        when(creditRepo.findById("X")).thenReturn(Mono.empty());
        StepVerifier.create(service.getCredit("X"))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void getCredit_ok() {
        var d = new CreditDocument(); d.setId("C1"); d.setType("PERSONAL");
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));
        StepVerifier.create(service.getCredit("C1"))
                .assertNext(r -> assertEquals("C1", r.getId()))
                .verifyComplete();
    }


    @Test
    void patchCredit_notFound() {
        when(creditRepo.findById("C1")).thenReturn(Mono.empty());
        StepVerifier.create(service.patchCredit("C1", new CreditUpdate()))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void patchCredit_ok_savesMergedDoc() {
        var d = new CreditDocument(); d.setId("C1"); d.setInterestAnnual(BigDecimal.ONE);
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));
        when(creditRepo.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        var patch = new CreditUpdate(); patch.setInterestAnnual(BigDecimal.TEN);

        StepVerifier.create(service.patchCredit("C1", patch))
                .assertNext(r -> assertEquals(BigDecimal.TEN, r.getInterestAnnual()))
                .verifyComplete();
    }


    @Test
    void getDebtStatusByCustomer_customerNotFound() {
        when(customerClient.existsCustomerId("C1")).thenReturn(Mono.just(false));
        StepVerifier.create(service.getDebtStatusByCustomer("C1"))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void getDebtStatusByCustomer_ok_aggregatesDelinquentBalance() {
        when(customerClient.existsCustomerId("C1")).thenReturn(Mono.just(true));

        var a = new CreditDocument(); a.setStatus("DELINQUENT"); a.setBalance(new BigDecimal("30"));
        var b = new CreditDocument(); b.setStatus("ACTIVE");      b.setBalance(new BigDecimal("50"));
        var c = new CreditDocument(); c.setStatus("DELINQUENT"); c.setBalance(new BigDecimal("20"));

        when(creditRepo.findByCustomerId("C1"))
                .thenReturn(Flux.fromIterable(List.of(a, b, c)));

        StepVerifier.create(service.getDebtStatusByCustomer("C1"))
                .assertNext(s -> {
                    assertTrue(s.getHasOverdue());
                    assertEquals(0, s.getTotalOverdueAmount().compareTo(new BigDecimal("50")));
                })
                .verifyComplete();
    }


    @Test
    void adjustLimit_conflict_whenNewLimitLowerThanBalance() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(new BigDecimal("200"));

        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        StepVerifier.create(service.adjustLimit("C1", new BigDecimal("100"), "reason"))
                .expectErrorSatisfies(err -> assertEquals(409, ((ResponseStatusException) err).getStatus().value()))
                .verify();
    }

    @Test
    void adjustLimit_ok() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(new BigDecimal("100"));

        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));
        when(creditRepo.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(service.adjustLimit("C1", new BigDecimal("500"), "reason"))
                .assertNext(r -> assertEquals(new BigDecimal("500"), r.getLimit()))
                .verifyComplete();
    }


    @Test
    void closeCredit_conflict_whenBalancePositive() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(new BigDecimal("10")); d.setStatus("ACTIVE");
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        StepVerifier.create(service.closeCredit("C1", "any"))
                .expectErrorSatisfies(err -> assertEquals(409, ((ResponseStatusException) err).getStatus().value()))
                .verify();
    }

    @Test
    void closeCredit_idempotent_whenAlreadyClosed() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(BigDecimal.ZERO); d.setStatus(CreditStatus.CLOSED.name());
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        StepVerifier.create(service.closeCredit("C1", "any"))
                .assertNext(r -> assertEquals(CreditStatus.CLOSED, r.getStatus()))
                .verifyComplete();

    }

    @Test
    void closeCredit_ok_setsFields() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(BigDecimal.ZERO); d.setStatus("ACTIVE");
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));
        when(creditRepo.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(service.closeCredit("C1", "done"))
                .assertNext(r -> {
                    assertEquals(CreditStatus.CLOSED, r.getStatus());
                    assertEquals("done", r.getCloseReason());
                })
                .verifyComplete();
    }


    @Test
    void listMovements_withTypeFilter() {
        var m = new CreditMovementDocument(); m.setId("M1"); m.setType("PAYMENT"); m.setTxnAt(Instant.now());
        when(movementRepo.findByCreditIdAndTypeOrderByTxnAtDesc("C1", "PAYMENT"))
                .thenReturn(Flux.just(m));

        StepVerifier.create(service.listMovements("C1", "PAYMENT"))
                .expectNextCount(1).verifyComplete();
    }

    @Test
    void listMovements_all() {
        var m1 = new CreditMovementDocument(); m1.setId("M1"); m1.setTxnAt(Instant.now());
        var m2 = new CreditMovementDocument(); m2.setId("M2"); m2.setTxnAt(Instant.now());
        when(movementRepo.findByCreditIdOrderByTxnAtDesc("C1"))
                .thenReturn(Flux.just(m1, m2));

        StepVerifier.create(service.listMovements("C1", null))
                .expectNextCount(2).verifyComplete();
    }


    @Test
    void listCredits_filtersTypeStatusAndIncludeClosed() {
        var a = doc("A", "CUST", "PERSONAL", "ACTIVE");
        var b = doc("B", "CUST", "BUSINESS", "CLOSED");
        var c = doc("C", "CUST", "PERSONAL", "ACTIVE");

        when(creditRepo.findAll()).thenReturn(Flux.just(a, b, c));

        StepVerifier.create(service.listCredits(null, CreditType.PERSONAL, CreditStatus.ACTIVE, true))
                .expectNextCount(2).verifyComplete(); // A y C
    }


    @Test
    void getBalance_notFound() {
        when(creditRepo.findById("X")).thenReturn(Mono.empty());
        StepVerifier.create(service.getBalance("X"))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void getBalance_ok() {
        var d = new CreditDocument(); d.setId("C1"); d.setLimit(new BigDecimal("100")); d.setBalance(new BigDecimal("35"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        StepVerifier.create(service.getBalance("C1"))
                .assertNext(b -> {
                    assertEquals(new BigDecimal("100"), b.getLimit());
                    assertEquals(new BigDecimal("35"), b.getBalance());
                    assertEquals(new BigDecimal("65"), b.getAvailable());
                })
                .verifyComplete();
    }


    @Test
    void updateCredit_notFound() {
        when(creditRepo.findById("X")).thenReturn(Mono.empty());
        StepVerifier.create(service.updateCredit("X", new CreditRequest()))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void updateCredit_ok_mergesAndSaves() {
        var d = new CreditDocument(); d.setId("C1"); d.setInterestAnnual(BigDecimal.ONE);
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));
        when(creditRepo.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        var req = new CreditRequest(); req.setInterestAnnual(new BigDecimal("9"));

        StepVerifier.create(service.updateCredit("C1", req))
                .assertNext(r -> assertEquals(new BigDecimal("9"), r.getInterestAnnual()))
                .verifyComplete();

        // verificación de updatedAt seteado
        ArgumentCaptor<CreditDocument> captor = ArgumentCaptor.forClass(CreditDocument.class);
        Mockito.verify(creditRepo).save(captor.capture());
        assertNotNull(captor.getValue().getUpdatedAt());
    }


    @Test
    void deleteCredit_noopWhenNotExists() {
        when(creditRepo.existsById("C1")).thenReturn(Mono.just(false));
        StepVerifier.create(service.deleteCredit("C1")).verifyComplete();
        verifyNoInteractions(movementRepo);
    }

    @Test
    void deleteCredit_cleansMovementsThenDeletes() {
        when(creditRepo.existsById("C1")).thenReturn(Mono.just(true));
        when(movementRepo.findByCreditIdOrderByTxnAtDesc("C1"))
                .thenReturn(Flux.just(new CreditMovementDocument(), new CreditMovementDocument()));
        when(movementRepo.deleteAll(anyList())).thenReturn(Mono.empty());
        when(creditRepo.deleteById("C1")).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteCredit("C1")).verifyComplete();

        Mockito.verify(movementRepo).deleteAll(anyList());
        Mockito.verify(creditRepo).deleteById("C1");
    }


    @Test
    void applyPayment_badRequest_whenAmountNullOrZero() {
        when(creditRepo.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(service.applyPayment("C1", new CreditPaymentRequest()))
                .expectErrorSatisfies(err -> {
                    var rse = (ResponseStatusException) err;
                    assertEquals(400, rse.getStatus().value());
                })
                .verify();

        verifyNoInteractions(ledger);
    }

    @Test
    void applyPayment_notFound_whenCreditNotExists() {
        when(creditRepo.findById("C1")).thenReturn(Mono.empty());
        var req = new CreditPaymentRequest(); req.setAmount(BigDecimal.ONE);
        StepVerifier.create(service.applyPayment("C1", req))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void applyPayment_conflict_whenExceedsBalance() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(new BigDecimal("50"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        var req = new CreditPaymentRequest(); req.setAmount(new BigDecimal("80"));

        StepVerifier.create(service.applyPayment("C1", req))
                .expectErrorSatisfies(err -> assertEquals(409, ((ResponseStatusException) err).getStatus().value()))
                .verify();
    }

    @Test
    void applyPayment_ok_delegatesToLedger() {
        var d = new CreditDocument(); d.setId("C1"); d.setBalance(new BigDecimal("50"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        var req = new CreditPaymentRequest(); req.setAmount(new BigDecimal("20"));
        var mv = new CreditMovement(); mv.setCreditId("C1"); mv.setAmount(new BigDecimal("20")); mv.setType(CreditMovement.TypeEnum.PAYMENT);
        when(ledger.updateBalanceAndRecord(any(), eq(new BigDecimal("30")), eq("PAYMENT"), eq(new BigDecimal("20")), isNull()))
                .thenReturn(Mono.just(mv));

        StepVerifier.create(service.applyPayment("C1", req))
                .expectNext(mv).verifyComplete();
    }


    @Test
    void applyCharge_badRequest_whenAmountInvalid() {
        when(creditRepo.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(service.applyCharge("C1", new CreditChargeRequest()))
                .expectErrorSatisfies(err -> {
                    var rse = (ResponseStatusException) err;
                    assertEquals(400, rse.getStatus().value());
                })
                .verify();

        verifyNoInteractions(ledger);
    }

    @Test
    void applyCharge_notFound_whenCreditNotExists() {
        when(creditRepo.findById("C1")).thenReturn(Mono.empty());
        var req = new CreditChargeRequest(); req.setAmount(BigDecimal.ONE);
        StepVerifier.create(service.applyCharge("C1", req))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void applyCharge_card_limitExceeded_422() {
        var d = new CreditDocument();
        d.setId("C1"); d.setType("CREDIT_CARD"); d.setLimit(new BigDecimal("100")); d.setBalance(new BigDecimal("95"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        var req = new CreditChargeRequest(); req.setAmount(new BigDecimal("10"));

        StepVerifier.create(service.applyCharge("C1", req))
                .expectErrorSatisfies(err -> assertEquals(422, ((ResponseStatusException) err).getStatus().value()))
                .verify();
    }

    @Test
    void applyCharge_nonCard_exceedsLimit_409() {
        var d = new CreditDocument();
        d.setId("C1"); d.setType("PERSONAL"); d.setLimit(new BigDecimal("100")); d.setBalance(new BigDecimal("90"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        var req = new CreditChargeRequest(); req.setAmount(new BigDecimal("20"));

        StepVerifier.create(service.applyCharge("C1", req))
                .expectErrorSatisfies(err -> assertEquals(409, ((ResponseStatusException) err).getStatus().value()))
                .verify();
    }

    @Test
    void applyCharge_ok_delegatesToLedger() {
        var d = new CreditDocument();
        d.setId("C1"); d.setType("PERSONAL"); d.setLimit(new BigDecimal("200")); d.setBalance(new BigDecimal("50"));
        when(creditRepo.findById("C1")).thenReturn(Mono.just(d));

        var req = new CreditChargeRequest(); req.setAmount(new BigDecimal("25")); req.setChannel("APP");

        var mv = new CreditMovement(); mv.setCreditId("C1"); mv.setAmount(new BigDecimal("25")); mv.setType(CreditMovement.TypeEnum.CHARGE);
        when(ledger.updateBalanceAndRecord(any(), eq(new BigDecimal("75")), eq("CHARGE"), eq(new BigDecimal("25")), eq("APP")))
                .thenReturn(Mono.just(mv));

        StepVerifier.create(service.applyCharge("C1", req))
                .expectNext(mv).verifyComplete();
    }


    @Test
    void searchByDocument_notFound_whenCustomerNotResolved() {
        when(customerClient.resolveCustomerId("DNI", "123")).thenReturn(Mono.empty());
        StepVerifier.create(service.searchByDocument("DNI", "123"))
                .expectError(ResponseStatusException.class).verify();
    }

    @Test
    void searchByDocument_ok() {
        var d1 = new CreditDocument(); d1.setId("A");
        var d2 = new CreditDocument(); d2.setId("B");

        when(customerClient.resolveCustomerId("DNI", "123")).thenReturn(Mono.just("C1"));
        when(creditRepo.findByCustomerId("C1")).thenReturn(Flux.just(d1, d2));

        StepVerifier.create(service.searchByDocument("DNI", "123"))
                .expectNextCount(2).verifyComplete();
    }


    private CreditRequest baseCreateReq() {
        var r = new CreditRequest();
        r.setType(CreditType.PERSONAL);
        r.setLimit(BigDecimal.TEN);
        r.setInterestAnnual(BigDecimal.ONE);
        r.setCurrency("PEN");
        return r;
    }

    private CreditDocument doc(String id, String cust, String type, String status) {
        var d = new CreditDocument();
        d.setId(id);
        d.setCustomerId(cust);
        d.setType(type);
        d.setStatus(status);
        return d;
    }
}
