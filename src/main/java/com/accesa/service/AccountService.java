package com.accesa.service;

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
        account.setTotalBalance(0);
        return accountRepository.save(account);
    }

    public Flux<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Mono<Account> getAccountById(String accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public Mono<Account> updateAccount(Account updatedAccount) {
        return accountRepository.save(updatedAccount);
    }

    @Transactional
    public Mono<Void> deleteAccount(String accountNumber) {
        return accountRepository.findById(accountNumber)
                .flatMap(account -> transactionRepository.deleteAllByAccountNumber(accountNumber)
                        .then(accountRepository.deleteById(accountNumber)));
    }
}
