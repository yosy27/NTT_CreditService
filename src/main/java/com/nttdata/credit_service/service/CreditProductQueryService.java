package com.nttdata.credit_service.service;

import com.nttdata.credit.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

//Servicio de dominio para la gestión de créditos

public interface CreditProductQueryService {

     /** GET /credits/overdue/{customerId} */
     Mono<OverdueStatus> getDebtStatusByCustomer(String customerId);

     /** GET /credit-products/{id} */
     Mono<CreditProductSummary> getCreditProduct(String productId);

     /** GET /credit-products/{id}/balance */
     Mono<CreditBalance> getProductBalance(String productId);

     /** GET /credit-products/{id}/movements?type&limit */
     Flux<CreditMovement> listProductMovements(String productId, String type, Integer limit);

     /** GET /customers/{customerId}/credit-products?kind&status */
     Flux<CreditProductSummary> listCreditProductsByCustomer(String customerId, String kind, CreditStatus status);
}

