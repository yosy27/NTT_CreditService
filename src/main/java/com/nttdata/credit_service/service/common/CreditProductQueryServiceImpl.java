package com.nttdata.credit_service.service.common;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.domain.MovementDocument;
import com.nttdata.credit_service.repository.CreditLineRepository;
import com.nttdata.credit_service.repository.LoanRepository;
import com.nttdata.credit_service.repository.MovementRepository;
import com.nttdata.credit_service.service.CreditProductQueryService;
import com.nttdata.credit_service.service.mapper.CreditProductMapper;
import com.nttdata.credit_service.service.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.nttdata.credit_service.service.mapper.CreditProductMapper.*;
import static com.nttdata.credit_service.shared.util.Numbers.nz;

@Service
@RequiredArgsConstructor
public class CreditProductQueryServiceImpl implements CreditProductQueryService {

    private final LoanRepository loanRepo;
    private final CreditLineRepository lineRepo;
    private final MovementRepository movementRepo;

    @Override
    public Mono<OverdueStatus> getDebtStatusByCustomer(String customerId) {
        Mono<BigDecimal> loanOverdue = loanRepo.findByCustomerId(customerId)
                .filter(doc -> "DELINQUENT".equalsIgnoreCase(doc.getStatus()))
                .map(doc -> nz(doc.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Mono<BigDecimal> lineOverdue = lineRepo.findByCustomerId(customerId)
                .filter(doc -> "DELINQUENT".equalsIgnoreCase(doc.getStatus()))
                .map(doc -> nz(doc.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Mono.zip(loanOverdue, lineOverdue, BigDecimal::add)
                .map(total -> {
                    OverdueStatus dto = new OverdueStatus();
                    dto.setHasOverdue(total.compareTo(BigDecimal.ZERO) > 0);
                    dto.setTotalOverdueAmount(total);
                    return dto;
                });
    }

    @Override
    public Mono<CreditProductSummary> getCreditProduct(String productId) {
        return loanRepo.findById(productId)
                .map(CreditProductMapper::toSummary)
                .switchIfEmpty(lineRepo.findById(productId).map(CreditProductMapper::toSummary))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")));
    }

    @Override
    public Mono<CreditBalance> getProductBalance(String productId) {
        return loanRepo.findById(productId)
                .map(doc -> toBalance(nz(doc.getLimit()), nz(doc.getBalance())))
                .switchIfEmpty(lineRepo.findById(productId)
                        .map(doc -> toBalance(nz(doc.getLimit()), nz(doc.getBalance()))))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")));
    }

    @Override
    public Flux<CreditMovement> listProductMovements(String productId, String type, Integer limit) {
        int n = (limit == null) ? 10 : Math.max(1, Math.min(limit, 100)); // 1..100 (por contrato)
        Flux<MovementDocument> stream = (type == null || type.isBlank())
                ? movementRepo.findByCreditIdOrderByTxnAtDesc(productId)
                : movementRepo.findByCreditIdAndTypeOrderByTxnAtDesc(productId, type.trim());

        return stream
                .take(n)
                .map(CreditProductMapper::toMovement);
    }

    @Override
    public Flux<CreditProductSummary> listCreditProductsByCustomer(String customerId, String kind, CreditStatus status) {
        final String st = (status != null) ? status.name() : null;
        final String k  = (kind == null) ? "" : kind.trim().toUpperCase(); // "", "LOAN", "CREDIT_LINE"

        Flux<CreditProductSummary> loans = loanRepo.findByCustomerId(customerId)
                .filter(doc -> st == null || st.equals(doc.getStatus()))
                .map(CreditProductMapper::toSummary);

        Flux<CreditProductSummary> lines = lineRepo.findByCustomerId(customerId)
                .filter(doc -> st == null || st.equals(doc.getStatus()))
                .map(CreditProductMapper::toSummary);

        if ("LOAN".equals(k))  return loans;
        if ("CREDIT_LINE".equals(k)) return lines;
        return Flux.concat(loans, lines);
    }
}
