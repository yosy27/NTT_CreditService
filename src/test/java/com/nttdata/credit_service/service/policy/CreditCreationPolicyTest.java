//package com.nttdata.credit_service.service.policy;
//
//import com.nttdata.credit.model.CreditStatus;
//import com.nttdata.credit_service.client.customer.dto.CustomerSummaryDto;
//import com.nttdata.credit_service.model.CreditDocument;
//import com.nttdata.credit_service.model.util.BusinessRules;
//import com.nttdata.credit_service.service.policy.CreditCreationPolicy;
//import com.nttdata.credit_service.client.CustomerClient;
//import com.nttdata.credit_service.repository.CreditRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//import com.nttdata.credit.model.CreditType;
//import com.nttdata.credit_service.model.enums.CustomerType;
//
//@ExtendWith(MockitoExtension.class)
//public class CreditCreationPolicyTest {
//
//    CustomerClient customerClient;
//    CreditRepository creditRepo;
//
//    CreditCreationPolicy policy;
//
//    @BeforeEach
//    void setup() {
//        customerClient = mock(CustomerClient.class);
//        creditRepo = mock(CreditRepository.class);
//        policy = new CreditCreationPolicy(customerClient, creditRepo);
//    }
//
//    private void mockCustomer(String customerId, String type) {
//        when(customerClient.getCustomerSummary(customerId))
//                .thenAnswer(inv -> {
//                    if (type == null) return Mono.<CustomerSummaryDto>empty(); // TIPADO
//                    CustomerSummaryDto cs = mock(CustomerSummaryDto.class);    // mock del DTO real
//                    when(cs.getType()).thenReturn(type);                       // "PERSONAL"/"BUSINESS"
//                    return Mono.just(cs);
//                });
//    }
//
//    @Test
//    void customer_not_found() {
//        String customerId = "C1";
//        mockCustomer(customerId, null); // Mono.empty()
//
//        StepVerifier.create(policy.validate(customerId, CreditType.PERSONAL))
//                .expectErrorSatisfies(ex -> {
//                    assertThat(ex).isInstanceOf(ResponseStatusException.class);
//                    ResponseStatusException rse = (ResponseStatusException) ex;
//                    assertThat(rse.getStatus().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
//                    assertThat(rse.getReason()).isEqualTo("CUSTOMER_NOT_FOUND");
//                })
//                .verify();
//
//        verifyNoInteractions(creditRepo);
//    }
//
//    @Test
//    void product_not_allowed_for_customer_type() {
//        String customerId = "C2";
//        when(customerClient.getCustomerSummary(customerId)).thenAnswer(inv -> {
//            CustomerSummaryDto cs = mock(CustomerSummaryDto.class);
//            when(cs.getType()).thenReturn("BUSINESS");
//            return Mono.just(cs);
//        });
//
//        try (MockedStatic<BusinessRules> mocked = mockStatic(BusinessRules.class)) {
//            mocked.when(() -> BusinessRules.isProductAllowed(CustomerType.BUSINESS, CreditType.PERSONAL))
//                    .thenReturn(false);
//
//            StepVerifier.create(policy.validate(customerId, CreditType.PERSONAL))
//                    .expectErrorSatisfies(ex -> {
//                        ResponseStatusException rse = (ResponseStatusException) ex;
//                        // Boot 2: rse.getStatus() | Boot 3: rse.getStatusCode()
//                        assertThat(rse.getStatus().value()).isEqualTo(422);
//                        assertThat(rse.getReason()).isEqualTo("PRODUCT_NOT_ALLOWED_FOR_CUSTOMER_TYPE");
//                    })
//                    .verify();
//        }
//
//        verifyNoInteractions(creditRepo);
//    }
//
//    @Test
//    void credit_delinquent_lock() {
//        String customerId = "C3";
//        mockCustomer(customerId, "PERSONAL"); // CustomerSummaryDto con type PERSONAL
//
//        // Morosidad: el repo devuelve al menos un crédito con status DELINQUENT
//        CreditDocument delinquent = new CreditDocument();
//        delinquent.setStatus("DELINQUENT");
//        when(creditRepo.findByCustomerId(customerId))
//                .thenReturn(Flux.just(delinquent));
//
//        StepVerifier.create(policy.validate(customerId, CreditType.PERSONAL))
//                .expectErrorSatisfies(ex -> {
//                    ResponseStatusException rse = (ResponseStatusException) ex;
//                    assertThat(rse.getStatus().value()).isEqualTo(HttpStatus.CONFLICT.value());
//                    assertThat(rse.getReason()).isEqualTo("CREDIT_DELINQUENT_LOCK");
//                })
//                .verify();
//
//        verify(creditRepo).findByCustomerId(customerId);
//        // No debe consultar límite si ya hubo lock por morosidad
//        verify(creditRepo, never())
//                .existsByCustomerIdAndTypeAndStatus(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void credit_personal_limit() {
//        String customerId = "C4";
//        mockCustomer(customerId, "PERSONAL");
//
//        // Sin morosidad
//        when(creditRepo.findByCustomerId(customerId))
//                .thenReturn(Flux.empty());
//
//        // Ya tiene 1 PERSONAL ACTIVO
//        when(creditRepo.existsByCustomerIdAndTypeAndStatus(
//                customerId, CreditType.PERSONAL.name(), CreditStatus.ACTIVE.name()))
//                .thenReturn(Mono.just(true));
//
//        StepVerifier.create(policy.validate(customerId, CreditType.PERSONAL))
//                .expectErrorSatisfies(ex -> {
//                    ResponseStatusException rse = (ResponseStatusException) ex;
//                    assertThat(rse.getStatus().value()).isEqualTo(HttpStatus.CONFLICT.value());
//                    assertThat(rse.getReason()).isEqualTo("CREDIT_PERSONAL_LIMIT");
//                })
//                .verify();
//
//        verify(creditRepo).findByCustomerId(customerId);
//        verify(creditRepo).existsByCustomerIdAndTypeAndStatus(
//                customerId, CreditType.PERSONAL.name(), CreditStatus.ACTIVE.name());
//    }
//
//    @Test
//    void happy_path_ok() {
//        String customerId = "C5";
//        mockCustomer(customerId, "BUSINESS");
//
//        // Sin morosidad
//        when(creditRepo.findByCustomerId(customerId))
//                .thenReturn(Flux.empty());
//
//        // Para BUSINESS/BUSINESS no se evalúa el límite PERSONAL ACTIVO,
//        // por lo tanto NO mockeamos ni verificamos existsBy...ACTIVE.
//
//        // (Opcional pero recomendable) Asegura compatibilidad explícitamente
//        try (MockedStatic<BusinessRules> mocked = mockStatic(BusinessRules.class)) {
//            mocked.when(() -> BusinessRules.isProductAllowed(CustomerType.BUSINESS, CreditType.BUSINESS))
//                    .thenReturn(true);
//
//            StepVerifier.create(policy.validate(customerId, CreditType.BUSINESS))
//                    .verifyComplete();
//        }
//
//        verify(creditRepo).findByCustomerId(customerId);
//        // Asegura que NO se consultó el límite de PERSONAL ACTIVO
//        verify(creditRepo, never()).existsByCustomerIdAndTypeAndStatus(
//                anyString(), anyString(), eq(CreditStatus.ACTIVE.name()));
//    }
//}
