package com.nttdata.credit_service.service.mapper;

import com.nttdata.credit.model.CreditBalance;
import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit.model.CreditRequest;
import com.nttdata.credit.model.CreditResponse;
import com.nttdata.credit.model.CreditUpdate;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.model.CreditMovementDocument;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CreditMapperTest {

    @Test
    void toApi_returnsNullOnNullInput() {
        assertNull(CreditMapper.toApi((CreditDocument) null));
    }

    @Test
    void toApi_mapsBasicFieldsAndDefaults() {
        CreditDocument d = new CreditDocument();
        d.setId("cr-1");
        d.setCustomerId("cust-1");
        // type y status como String; si no coinciden con el enum, parseEnum devolverá null (válido).
        d.setType("ACTIVE-UNKNOWN");
        d.setStatus("ACTIVE");
        d.setLimit(new BigDecimal("1000"));
        d.setBalance(new BigDecimal("200.50"));
        d.setInterestAnnual(new BigDecimal("14.99"));
        d.setCurrency(null);                 // -> orDefault(..., "PEN")
        d.setCreatedAt(Instant.now());
        d.setUpdatedAt(Instant.now());
        d.setClosedAt(null);
        d.setCloseReason("  cierre  ");
        CreditResponse api = CreditMapper.toApi(d);

        assertNotNull(api);
        assertEquals("cr-1", api.getId());
        assertEquals("cust-1", api.getCustomerId());
        assertEquals(new BigDecimal("1000"), api.getLimit());
        assertEquals(new BigDecimal("200.50"), api.getBalance());
        assertEquals(new BigDecimal("14.99"), api.getInterestAnnual());
        assertEquals("PEN", api.getCurrency()); // default aplicado
        assertNotNull(api.getCreatedAt());
        assertNotNull(api.getUpdatedAt());
        assertNull(api.getCard());
    }


    @Test
    void fromCreate_returnsNullOnNullRequest() {
        assertNull(CreditMapper.fromCreate(null, "cust-1"));
    }

    @Test
    void fromCreate_setsDefaultsAndTimestamps() {
        CreditRequest req = new CreditRequest();
        req.setType(null);                    // asString(null) podría dejar null: no es relevante al test
        req.setLimit(null);                   // orZero -> 0
        req.setInterestAnnual(null);          // orZero -> 0
        req.setCurrency(null);                // default -> PEN
        req.setCard(null);                    // no aplica card

        CreditDocument doc = CreditMapper.fromCreate(req, "cust-777");

        assertNotNull(doc);
        assertEquals("cust-777", doc.getCustomerId());
        assertEquals(BigDecimal.ZERO, doc.getBalance());             // balance inicial
        assertEquals(BigDecimal.ZERO, doc.getLimit());               // orZero
        assertEquals(BigDecimal.ZERO, doc.getInterestAnnual());      // orZero
        assertEquals("PEN", doc.getCurrency());                      // default
        assertNotNull(doc.getStatus());                              // ACTIVE como String
        assertNotNull(doc.getCreatedAt());
        assertNotNull(doc.getUpdatedAt());
        assertEquals(doc.getCreatedAt(), doc.getUpdatedAt(), "createdAt y updatedAt deben ser iguales al usar el mismo now");
    }


    @Test
    void merge_returnsTargetWhenNulls() {
        CreditDocument target = new CreditDocument();
        target.setUpdatedAt(Instant.EPOCH);

        assertNull(CreditMapper.merge(null, new CreditUpdate()));
        CreditDocument same = CreditMapper.merge(target, null);
        assertSame(target, same);
        assertEquals(Instant.EPOCH, same.getUpdatedAt());
    }

    @Test
    void merge_updatesFieldsAndTouchesUpdated() {
        CreditDocument target = new CreditDocument();
        target.setInterestAnnual(new BigDecimal("10"));
        Instant oldUpdated = Instant.parse("2024-01-01T00:00:00Z");
        target.setUpdatedAt(oldUpdated);

        CreditUpdate patch = new CreditUpdate();
        patch.setInterestAnnual(new BigDecimal("12"));

        CreditDocument out = CreditMapper.merge(target, patch);
        assertEquals(new BigDecimal("12"), out.getInterestAnnual());
        assertNotNull(out.getUpdatedAt());
        assertTrue(out.getUpdatedAt().isAfter(oldUpdated), "touchUpdated debe refrescar updatedAt");
    }


    @Test
    void toApiBalance_returnsNullOnNullInput() {
        assertNull(CreditMapper.toApiBalance(null));
    }

    @Test
    void toApiBalance_computesAvailableNonNegative() {
        CreditDocument d1 = new CreditDocument();
        d1.setLimit(new BigDecimal("1000"));
        d1.setBalance(new BigDecimal("200"));
        CreditBalance b1 = CreditMapper.toApiBalance(d1);
        assertEquals(new BigDecimal("1000"), b1.getLimit());
        assertEquals(new BigDecimal("200"), b1.getBalance());
        assertEquals(new BigDecimal("800"), b1.getAvailable());

        CreditDocument d2 = new CreditDocument();
        d2.setLimit(new BigDecimal("100"));
        d2.setBalance(new BigDecimal("200"));
        CreditBalance b2 = CreditMapper.toApiBalance(d2);
        assertEquals(BigDecimal.ZERO, b2.getAvailable(), "Nunca debe ser negativo");
    }


    @Test
    void toApiMovement_returnsNullOnNullInput() {
        assertNull(CreditMapper.toApi((CreditMovementDocument) null));
    }

    @Test
    void toApiMovement_mapsFields() {
        CreditMovementDocument md = new CreditMovementDocument();
        md.setId("mv-1");
        md.setCreditId("cr-1");
        md.setType("UNKNOWN");                 // parseEnum -> podría resultar null; es válido
        md.setAmount(new BigDecimal("50"));
        md.setTxnAt(Instant.now());
        md.setPostedAt(Instant.now());
        md.setRunningBalance(new BigDecimal("150"));
        md.setChannel("  ATM  ");

        CreditMovement api = CreditMapper.toApi(md);
        assertNotNull(api);
        assertEquals("mv-1", api.getId());
        assertEquals("cr-1", api.getCreditId());
        assertEquals(new BigDecimal("50"), api.getAmount());
        assertEquals(new BigDecimal("150"), api.getRunningBalance());
        assertNotNull(api.getTxnAt());
        assertNotNull(api.getPostedAt());
        // No afirmamos exactamente cómo normaliza el canal; solo verificamos que no rompa.
        assertNotNull(api.getChannel());
    }


    @Test
    void mergeAll_returnsTargetWhenNulls() {
        CreditDocument target = new CreditDocument();
        target.setUpdatedAt(Instant.EPOCH);
        assertNull(CreditMapper.mergeAll(null, new CreditRequest()));
        CreditDocument same = CreditMapper.mergeAll(target, null);
        assertSame(target, same);
        assertEquals(Instant.EPOCH, same.getUpdatedAt());
    }

    @Test
    void mergeAll_updatesProvidedFieldsAndKeepsCurrencyOnEmpty() {
        CreditDocument target = new CreditDocument();
        target.setLimit(new BigDecimal("100"));
        target.setInterestAnnual(new BigDecimal("10"));
        target.setCurrency("USD");
        Instant oldUpdated = Instant.parse("2024-06-01T00:00:00Z");
        target.setUpdatedAt(oldUpdated);

        CreditRequest src = new CreditRequest();
        src.setLimit(new BigDecimal("250"));
        src.setInterestAnnual(new BigDecimal("19.5"));
        src.setCurrency(""); // no null: aplica orDefault("", targetCurrency) -> mantiene "USD"

        CreditDocument out = CreditMapper.mergeAll(target, src);

        assertEquals(new BigDecimal("250"), out.getLimit());
        assertEquals(new BigDecimal("19.5"), out.getInterestAnnual());
        assertEquals("USD", out.getCurrency(), "Debe conservar la moneda original cuando llega cadena vacía");
        assertTrue(out.getUpdatedAt().isAfter(oldUpdated), "touchUpdated debe refrescar updatedAt");
    }
}
