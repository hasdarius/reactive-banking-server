package com.accesa.service;

import com.accesa.controller.validation.AccountAlreadyExistsException;
import com.accesa.controller.validation.AccountContainsMoneyException;
import com.accesa.controller.validation.AccountNotFoundException;
import com.accesa.entity.Account;
import com.accesa.repository.AccountRepository;
import com.accesa.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<Account> createAccount(Account account) {
        return accountRepository.save(account)
                .onErrorResume(Exception.class, error -> Mono.error(AccountAlreadyExistsException::new));
    }

    public Mono<Page<Account>> getAllAccounts(Pageable pageable) {
        return accountRepository
                .findAllBy(pageable)
                .collectList()
                .zipWith(accountRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    public Mono<Page<Account>> getAllAccountsByAccountHolder(String accountHolder, Pageable pageable) {
        return accountRepository
                .findAllByAccountHolder(accountHolder, pageable)
                .collectList()
                .zipWith(accountRepository.countBy(accountHolder))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable,tuple.getT2()));
    }

    public Mono<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber);
    }

    public Mono<Double> getBalanceForAccountNumber(String accountNumber) {
        return transactionRepository
                .findAllByAccountNumber(accountNumber, Pageable.unpaged())
                .map(transaction -> transaction.getDepositAmount() != null ? transaction.getDepositAmount() : -transaction.getWithdrawalAmount())
                .switchIfEmpty(Mono.error(AccountNotFoundException::new))
                .reduce(0.0, Double::sum);
    }

    public Mono<Double> getTotalBalanceForAccountHolder(String accountHolder) {
        return accountRepository
                .findAllByAccountHolder(accountHolder, Pageable.unpaged())
                .flatMap(account -> getBalanceForAccountNumber(account.getAccountNumber()))
                .switchIfEmpty(Mono.error(AccountNotFoundException::new))
                .reduce(0.0, Double::sum);

    }

    @Transactional
    public Mono<Void> deleteAccount(String accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(AccountNotFoundException::new))
                .flatMap(account -> getDeleteAccountMono(accountNumber, account));
    }

    private Mono<Void> getDeleteAccountMono(String accountNumber, Account account) {

        return getBalanceForAccountNumber(accountNumber)
                .flatMap(value -> {
                    if (value != 0.0)
                        return Mono.error(AccountContainsMoneyException::new);
                    return transactionRepository.deleteAllByAccountNumber(accountNumber)
                            .then(accountRepository.deleteById(account.getId()));
                });
    }
}
