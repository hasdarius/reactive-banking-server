package com.accesa.repository;

import com.accesa.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {
    Mono<Account> findByAccountNumber(String accountNumber);
    Flux<Account> findAllByAccountHolder(String accountHolder);
}
