package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.Credit;
import com.nttdata.credit.model.CreditBalance;
import com.nttdata.credit.model.CreditRequest;
import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.CreditType;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.repository.CreditRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    CreditRepository repo;

    // Si tu servicio tiene más dependencias, añádelas como @Mock y @InjectMocks
    @InjectMocks
    CreditServiceImpl service;

    private static CreditDocument doc(String id, String customerId, CreditType type,
                                      double limit, double balance, String status) {
        var d = new CreditDocument();
        d.setId(id);
        d.setCustomerId(customerId);
        d.setType(type);
        d.setLimit(limit);
        d.setBalance(balance);
        d.setStatus(CreditStatus.valueOf(status));
        return d;
    }

    // ---------- create() ----------
    @Test
    @DisplayName("create(): PERSONAL OK cuando no existe ACTIVO")
    void create_personal_ok() {
        var req = new CreditRequest()
                .customerId("C001")
                .type(CreditType.PERSONAL)
                .limit(5000d)
                .interestAnnual(12.5);

        // existsByCustomerIdAndTypeAndStatus = false  → se puede crear
        when(repo.existsByCustomerIdAndTypeAndStatus("C001", CreditType.PERSONAL, CreditStatus.ACTIVE))
                .thenReturn(Mono.just(false));

        var saved = doc("CR-1", "C001", CreditType.PERSONAL, 5000d, 0d, "ACTIVE");
        when(repo.save(any(CreditDocument.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(req))
                .assertNext((Credit c) -> {
                    assertEquals("CR-1", c.getId());
                    assertEquals("C001", c.getCustomerId());
                    assertEquals(CreditType.PERSONAL, c.getType());
                    assertEquals(0d, c.getBalance());
                })
                .verifyComplete();

        verify(repo).existsByCustomerIdAndTypeAndStatus("C001", CreditType.PERSONAL, CreditStatus.ACTIVE);
        verify(repo).save(any(CreditDocument.class));
    }

    @Test
    @DisplayName("create(): PERSONAL CONFLICT cuando ya existe ACTIVO")
    void create_personal_conflict() {
        var req = new CreditRequest()
                .customerId("C001")
                .type(CreditType.PERSONAL)
                .limit(1000d)
                .interestAnnual(10d);

        when(repo.existsByCustomerIdAndTypeAndStatus("C001", CreditType.PERSONAL, CreditStatus.ACTIVE))
                .thenReturn(Mono.just(true));

        StepVerifier.create(service.create(req))
                .expectErrorSatisfies(ex -> {
                    // usa el tipo exacto que lances en tu servicio (IllegalState/ResponseStatus…)
                    assertTrue(ex instanceof RuntimeException);
                })
                .verify();

        verify(repo).existsByCustomerIdAndTypeAndStatus("C001", CreditType.PERSONAL, CreditStatus.ACTIVE);
        verify(repo, never()).save(any());
    }

    // ---------- applyPayment() ----------
    @Test
    @DisplayName("applyPayment(): reduce el balance (monto válido)")
    void applyPayment_ok() {
        var existing = doc("CR-2", "C001", CreditType.BUSINESS, 5000d, 100d, "ACTIVE");
        when(repo.findById("CR-2")).thenReturn(Mono.just(existing));
        when(repo.save(any(CreditDocument.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.applyPayment("CR-2", 30d, "pago"))
                .assertNext((CreditBalance b) -> {
                    assertEquals(70d, b.getBalance());
                    assertEquals(5000d, b.getLimit());
                    assertEquals(4930d, b.getAvailable());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("applyPayment(): monto inválido (<=0) lanza error")
    void applyPayment_invalidAmount() {
        StepVerifier.create(service.applyPayment("CR-X", 0d, null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    // ---------- applyCharge() ----------
    @Test
    @DisplayName("applyCharge(): tarjeta dentro del límite → OK")
    void applyCharge_card_ok() {
        var card = doc("CR-3", "C001", CreditType.CREDIT_CARD, 200d, 50d, "ACTIVE");
        when(repo.findById("CR-3")).thenReturn(Mono.just(card));
        when(repo.save(any(CreditDocument.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.applyCharge("CR-3", 20d, "shop", "nota"))
                .assertNext((CreditBalance b) -> {
                    assertEquals(70d, b.getBalance());
                    assertEquals(130d, b.getAvailable());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("applyCharge(): excede el límite de la tarjeta → error")
    void applyCharge_limitExceeded() {
        var card = doc("CR-4", "C001", CreditType.CREDIT_CARD, 120d, 100d, "ACTIVE");
        when(repo.findById("CR-4")).thenReturn(Mono.just(card));

        StepVerifier.create(service.applyCharge("CR-4", 50d, "shop", null))
                .expectError(IllegalStateException.class)
                .verify();

        verify(repo, never()).save(any());
    }

    // ---------- getBalance() ----------
    @Test
    @DisplayName("getBalance(): devuelve límite, balance y disponible")
    void getBalance_ok() {
        var d = doc("CR-5", "C001", CreditType.BUSINESS, 1000d, 250d, "ACTIVE");
        when(repo.findById("CR-5")).thenReturn(Mono.just(d));

        StepVerifier.create(service.getBalance("CR-5"))
                .assertNext((CreditBalance b) -> {
                    assertEquals(1000d, b.getLimit());
                    assertEquals(250d, b.getBalance());
                    assertEquals(750d, b.getAvailable());
                })
                .verifyComplete();
    }
}