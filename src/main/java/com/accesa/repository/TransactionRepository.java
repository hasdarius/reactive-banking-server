package com.accesa.repository;

import com.accesa.entity.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    Flux<Transaction> findAllByAccountNumber(String accountNumber);
    Mono<Void> deleteAllByAccountNumber(String accountNumber);
    Flux<Transaction> findAllByAccountNumberAndTransactionDateBetween(String accountNumber, LocalDate after, LocalDate before);
}