package com.nttdata.credit_service.service;

import com.nttdata.credit.model.Credit;
import com.nttdata.credit.model.CreditBalance;
import com.nttdata.credit.model.CreditRequest;
import com.nttdata.credit.model.CreditUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Servicio de dominio para la gestión de créditos
// Define las operaciones principales de negocio
public interface CreditService {
     // Crear un nuevo crédito a partir de un CreditRequest
     Mono<Credit> create(CreditRequest req);
     // Listar todos los créditos o, si se pasa customerId, solo los de ese cliente
     Flux<Credit> findAll(String customerId);
     // Buscar un crédito por su ID
     Mono<Credit> getById(String id);
     // Actualizar un crédito existente según CreditUpdateRequest
     Mono<Credit> update(String id, CreditUpdateRequest req);
     // Eliminar un crédito por su ID
     Mono<Void>  delete(String id);
     // Aplicar un pago al crédito
     Mono<CreditBalance> applyPayment(String id, double amount, String note);
     // Aplicar un cargo o consumo al crédito
     Mono<CreditBalance> applyCharge(String id, double amount, String merchant, String note);
     // Consultar los saldos del crédito
     Mono<CreditBalance> getBalance(String id);
}
