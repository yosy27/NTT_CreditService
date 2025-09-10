package com.nttdata.credit_service.service.loans;

import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.LoanType;
import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.domain.enums.CustomerType;
import com.nttdata.credit_service.repository.LoanRepository;
import com.nttdata.credit_service.service.common.CommonAdmissionPolicy;
import com.nttdata.credit_service.service.policy.BusinessRules;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.nttdata.credit_service.shared.util.Numbers.parseEnum;


@RequiredArgsConstructor
public class LoanCreationPolicy {

    private final CommonAdmissionPolicy common;
    private final CustomerClient customerClient;
    private final LoanRepository loanRepo;


// Valida si el cliente puede crear un préstamo del tipo loanType
    public Mono<Void> validate(String customerId, LoanType loanType) {
        return common.ensureCustomerAdmissible(customerId)
                .then(customerClient.getCustomerSummary(customerId))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
                .flatMap(summary -> {
                    var customerType = parseEnum(CustomerType.class, summary.getType()); // PERSONAL | BUSINESS

                    // Compatibilidad de producto
                    if (!BusinessRules.isLoanAllowed(customerType, loanType)) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.UNPROCESSABLE_ENTITY, "PRODUCT_NOT_ALLOWED_FOR_CUSTOMER_TYPE"));
                    }

                    // Regla: PERSONAL solo 1 loan PERSONAL ACTIVE
                    if (customerType == CustomerType.PERSONAL && loanType == LoanType.PERSONAL) {
                        return loanRepo.existsByCustomerIdAndTypeAndStatus(
                                        customerId, LoanType.PERSONAL.name(), CreditStatus.ACTIVE.name())
                                .flatMap(exists -> exists
                                        ? Mono.error(new ResponseStatusException(
                                        HttpStatus.CONFLICT, "CREDIT_PERSONAL_LIMIT"))
                                        : Mono.empty());
                    }

                    return Mono.empty();
                });
    }

}
