//package com.nttdata.credit_service.service.policy;
//
//import com.nttdata.credit.model.CreditStatus;
//import com.nttdata.credit.model.CreditType;
//import com.nttdata.credit_service.client.CustomerClient;
//import com.nttdata.credit_service.model.enums.CustomerType;
//import com.nttdata.credit_service.repository.CreditRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.publisher.Mono;
//
//import static com.nttdata.credit_service.model.util.BusinessRules.isProductAllowed;
//import static com.nttdata.credit_service.model.util.CreditUtils.parseEnum;
//
//@Component
//@RequiredArgsConstructor
//public class CreditCreationPolicy {
//
//    private final CustomerClient customerClient;
//    private final CreditRepository creditRepo;
//
//    public Mono<Void> validate(String customerId, CreditType creditType) {
//        return customerClient.getCustomerSummary(customerId)
//                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
//                .flatMap(cus -> {
//                    var customerType = parseEnum(CustomerType.class, cus.getType()); // PERSONAL | BUSINESS
//                    // 1) Compatibilidad de producto por tipo de cliente
//                    if (!isProductAllowed(customerType, creditType)) {
//                        return Mono.error(new ResponseStatusException(
//                                HttpStatus.UNPROCESSABLE_ENTITY, "PRODUCT_NOT_ALLOWED_FOR_CUSTOMER_TYPE"));
//                    }
//                    // 2) Bloqueo por morosidad (si tiene algún crédito DELINQUENT)
//                    return creditRepo.findByCustomerId(customerId)
//                            .filter(c -> "DELINQUENT".equalsIgnoreCase(c.getStatus()))
//                            .hasElements()
//                            .flatMap(hasDelinquent -> {
//                                if (hasDelinquent) {
//                                    return Mono.error(new ResponseStatusException(
//                                            HttpStatus.CONFLICT, "CREDIT_DELINQUENT_LOCK"));
//                                }
//                                // 3) Regla académica: máx. 1 crédito PERSONAL ACTIVO (solo para cliente PERSONAL)
//                                if (customerType == CustomerType.PERSONAL && creditType == CreditType.PERSONAL) {
//                                    return creditRepo.existsByCustomerIdAndTypeAndStatus(
//                                                    customerId, CreditType.PERSONAL.name(), CreditStatus.ACTIVE.name())
//                                            .flatMap(exists -> exists
//                                                    ? Mono.error(new ResponseStatusException(
//                                                    HttpStatus.CONFLICT, "CREDIT_PERSONAL_LIMIT"))
//                                                    : Mono.empty());
//                                }
//                                return Mono.empty();
//                            });
//                });
//    }
//}
