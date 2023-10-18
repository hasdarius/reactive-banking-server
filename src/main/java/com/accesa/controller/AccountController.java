package com.accesa.controller;

import com.accesa.controller.validation.AccountAlreadyExistsException;
import com.accesa.controller.validation.AccountContainsMoneyException;
import com.accesa.controller.validation.AccountNotFoundException;
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

    @GetMapping("/number/{accountNumber}")
    public Mono<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @GetMapping("/holder/{accountHolder}")
    public Flux<Account> getAllAccountsByAccountHolder(@PathVariable String accountHolder) {
        return accountService.getAllAccountsByAccountHolder(accountHolder);
    }

    @GetMapping(path = "/balance/number/{accountNumber}")
    public Mono<ResponseEntity<Double>> getBalanceForAccountNumber(@PathVariable String accountNumber) {
        return accountService
                .getBalanceForAccountNumber(accountNumber)
                .flatMap(result -> Mono.just(ResponseEntity.ok(result)))
                .onErrorReturn(AccountNotFoundException.class, ResponseEntity.notFound().build()); // simply catch any error and return status 404
    }

    @GetMapping(path = "balance/holder/{accountHolder}")
    public Mono<ResponseEntity<Double>> getBalanceForAccountHolder(@PathVariable String accountHolder) {
        return accountService
                .getTotalBalanceForAccountHolder(accountHolder)
                .flatMap(result -> Mono.just(ResponseEntity.ok(result)))
                .onErrorReturn(AccountNotFoundException.class, ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createAccount(@RequestBody Account account) {
        return accountService
                .createAccount(account)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(AccountAlreadyExistsException.class, error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())));
    }

    @DeleteMapping("/{accountNumber}")
    public Mono<ResponseEntity<Object>> deleteAccount(@PathVariable String accountNumber) {
        return accountService.deleteAccount(accountNumber)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorReturn(AccountNotFoundException.class, ResponseEntity.notFound().build())
                .onErrorResume(AccountContainsMoneyException.class, error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())));
    }
}
