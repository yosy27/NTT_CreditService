package com.nttdata.credit_service.service.common;

import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.domain.CreditLineDocument;
import com.nttdata.credit_service.domain.LoanDocument;
import com.nttdata.credit_service.repository.CreditLineRepository;
import com.nttdata.credit_service.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

///Políticas de admisión comunes a cualquier producto de crédito.
///- Verifica que el cliente exista.
///- Bloquea la admisión si el cliente tiene ALGÚN producto en estado DELINQUENT
@Component
@RequiredArgsConstructor
public class CommonAdmissionPolicy {

    private final CustomerClient customerClient;
    private final LoanRepository loanRepo;
    private final CreditLineRepository lineRepo;

    //Asegura que el cliente es admisible para crear productos de crédito.
    public Mono<Void> ensureCustomerAdmissible(String customerId) {
        return customerClient.existsCustomerId(customerId)
                // Si no existe -> 404
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
                // Chequear morosidad consolidada (loans + credit-lines)
                .then(hasAnyDelinquent(customerId)
                        .flatMap(hasDelinquent -> hasDelinquent
                                ? Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CREDIT_DELINQUENT_LOCK"))
                                : Mono.empty()));
    }


    //Devuelve true si el cliente tiene ALGÚN producto (préstamo o línea) en DELINQUENT.
    private Mono<Boolean> hasAnyDelinquent(String customerId) {
        // Loans -> Flux<String> de estados
        Flux<String> loanStatuses =
                loanRepo.findByCustomerId(customerId).map(LoanDocument::getStatus);

        // Credit lines -> Flux<String> de estados
        Flux<String> lineStatuses =
                lineRepo.findByCustomerId(customerId).map(CreditLineDocument::getStatus);

        // Unimos ambos y preguntamos si HAY algún "DELINQUENT"
        return Flux.concat(loanStatuses, lineStatuses)
                .any(status -> "DELINQUENT".equalsIgnoreCase(status));
    }
}
