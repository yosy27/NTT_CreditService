package com.nttdata.credit_service.service;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.model.CreditMovementDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

//Servicio de dominio para la gestión de créditos
// Define las operaciones principales de negocio
public interface CreditService {

     //Crea un nuevo crédito en el sistema a partir de una solicitud.
     Mono<Credit> registerCredit(CreditCreate req);

     //Recupera la información de un crédito  por su id
     Mono<Credit> getCredit(String id);

     //Aplica cambios parciales a un crédito existente
     Mono<Credit> patchCredit(String id, CreditUpdate patch);

     //Permite ajustar (subir/bajar) el límite de un crédito.
     Mono<Credit> adjustLimit(String id, BigDecimal newLimit, String reason);

     //Cierra un crédito existente
     Mono<Credit> closeCredit(String id, String reason);

     //Lista los movimientos (cargos, pagos, ajustes, etc.) asociados a un crédito.
     Flux<CreditMovement> listMovements(String creditId, String type);

     //Lista todos los créditos de un cliente, con filtros.
     Flux<Credit> listCredits(String customerId, CreditType type, CreditStatus status, Boolean includeClosed);

     //Obtiene el saldo de un crédito
     Mono<CreditBalance> getBalance(String id);

     // Aplica un pago a un crédito.
     Mono<CreditMovement> applyPayment(String id, CreditPaymentRequest req);

     //Aplica un cargo al crédito (consumo, comisión, interés)
     Mono<CreditMovement> applyCharge(String id, CreditChargeRequest req);

     //Busca créditos asociados a un documento de cliente
     Flux<Credit> searchByDocument(String documentType, String documentNumber);

     Mono<OverdueStatus> getDebtStatusByCustomer(String customerId);


}

