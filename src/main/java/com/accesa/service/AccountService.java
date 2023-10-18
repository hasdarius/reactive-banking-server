package com.accesa.service;

import com.accesa.controller.validation.AccountAlreadyExistsException;
import com.accesa.controller.validation.AccountContainsMoneyException;
import com.accesa.controller.validation.AccountNotFoundException;
import com.accesa.entity.Account;
import com.accesa.repository.AccountRepository;
import com.accesa.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
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

    public Flux<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Flux<Account> getAllAccountsByAccountHolder(String accountHolder) {
        return accountRepository.findAllByAccountHolder(accountHolder);
    }

    public Mono<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber);
    }

    public Mono<Double> getBalanceForAccountNumber(String accountNumber) {
        return transactionRepository
                .findAllByAccountNumber(accountNumber)
                .map(transaction -> transaction.getDepositAmount() != null ? transaction.getDepositAmount() : -transaction.getWithdrawalAmount())
                .switchIfEmpty(Mono.error(AccountNotFoundException::new))
                .reduce(0.0, Double::sum);
    }

    public Mono<Double> getTotalBalanceForAccountHolder(String accountHolder) {
        return accountRepository
                .findAllByAccountHolder(accountHolder)
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
