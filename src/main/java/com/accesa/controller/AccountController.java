package com.accesa.controller;

import com.accesa.entity.Account;
import com.accesa.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public Flux<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Mono<ResponseEntity<Account>> getAccountById(@PathVariable String accountNumber) {
        return accountService.getAccountById(accountNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{accountNumber}")
    public Mono<ResponseEntity<Account>> updateAccount(
            @PathVariable String accountNumber,
            @RequestBody Account updatedAccount) {
        return accountService.getAccountById(accountNumber)
                .flatMap(existingAccount -> {
                    updatedAccount.setAccountNumber(existingAccount.getAccountNumber());
                    return accountService.updateAccount(updatedAccount);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{accountNumber}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable String accountNumber) {
        return accountService.deleteAccount(accountNumber)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
