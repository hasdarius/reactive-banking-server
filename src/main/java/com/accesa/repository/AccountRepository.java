package com.accesa.repository;

import com.accesa.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {
    Flux<Account> findAllBy(Pageable pageable);
    Mono<Account> findByAccountNumber(String accountNumber);
    Flux<Account> findAllByAccountHolder(String accountHolder, Pageable pageable);

    Mono<Long> countBy(String accountHolder);
}
