package com.accesa.reactive.service;

import com.accesa.reactive.controller.validation.AccountNotFoundException;
import com.accesa.reactive.entity.Transaction;
import com.accesa.reactive.repository.AccountRepository;
import com.accesa.reactive.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Mono<Page<Transaction>> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAllBy(pageable)
                .collectList()
                .zipWith(transactionRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable,tuple.getT2()));
    }

    @Transactional
    public Mono<Transaction> createTransaction(Transaction transaction) {
        return accountRepository
                .findByAccountNumber(transaction.getAccountNumber())
                .flatMap(account -> transactionRepository.save(transaction))
                .switchIfEmpty(Mono.error(new AccountNotFoundException()));
    }

    public Mono<Page<Transaction>> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        return transactionRepository
                .findAllByAccountNumber(accountNumber, pageable)
                .collectList()
                .zipWith(transactionRepository.countByAccountNumber(accountNumber))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable,tuple.getT2()));
    }

    public Mono<Page<Transaction>> getAllByAccountNumberAndTransactionDateBetween(String accountNumber,
                                                                            LocalDate from,
                                                                            LocalDate to,
                                                                            Pageable pageable) {
        return transactionRepository
                .findAllByAccountNumberAndTransactionDateBetween(accountNumber, from, to, pageable)
                .collectList()
                .zipWith(transactionRepository.countByAccountNumberAndTransactionDateBetween(accountNumber, from, to))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable,tuple.getT2()));
    }

    public Mono<Void> deleteTransaction(Long id) {
        return transactionRepository.deleteById(id);
    }
}

