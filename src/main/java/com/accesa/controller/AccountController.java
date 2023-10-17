package com.accesa.controller;

import com.accesa.controller.validation.AccountContainsMoneyException;
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

    @GetMapping
    public Flux<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Mono<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createAccount(@RequestBody Account account) {
        return accountService
                .createAccount(account)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{accountNumber}")
    public Mono<ResponseEntity<String>> deleteAccount(@PathVariable String accountNumber) {
        return accountService.deleteAccount(accountNumber)
                .then(Mono.just(ResponseEntity.ok().<String>build()))
                .onErrorResume(AccountContainsMoneyException.class, error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())));
    }
}
