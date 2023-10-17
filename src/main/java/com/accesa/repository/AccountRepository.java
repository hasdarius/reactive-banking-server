package com.accesa.repository;

import com.accesa.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    Mono<Account> findByAccountNumber(String accountNumber);
}
