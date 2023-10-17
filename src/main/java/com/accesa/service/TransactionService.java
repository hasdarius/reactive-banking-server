package com.accesa.service;

import com.accesa.controller.validation.AccountNotFoundException;
import com.accesa.entity.Account;
import com.accesa.entity.Transaction;
import com.accesa.repository.AccountRepository;
import com.accesa.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ReentrantLock accountLock = new ReentrantLock();

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Mono<Transaction> createTransaction(Transaction transaction) {
        return accountRepository
                .findByAccountNumber(transaction.getAccountNumber())
                .flatMap(account -> getTransactionMono(transaction, account))
                .switchIfEmpty(Mono.error(new AccountNotFoundException()));
    }

    public Flux<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findAllByAccountNumber(accountNumber);
    }

    public Flux<Transaction> getAllByAccountNumberAndTransactionDateBetween(String accountNumber, LocalDate from, LocalDate to) {
        return transactionRepository.findAllByAccountNumberAndTransactionDateBetween(accountNumber, from, to);
    }

    public Mono<Void> deleteTransaction(Long id) {
        return transactionRepository.deleteById(id);
    }

    private Mono<Transaction> getTransactionMono(Transaction transaction, Account account) {
        accountLock.lock();
        try {
            double amountUpdated = transaction.getDepositAmount() != null ? transaction.getDepositAmount() : -transaction.getWithdrawalAmount();
            account.setTotalBalance(account.getTotalBalance() + amountUpdated);

            return accountRepository
                    .save(account)
                    .then(transactionRepository.save(transaction));
        } finally {
            accountLock.unlock();
        }
    }
}

