package com.accesa.controller;

import com.accesa.controller.validation.AccountNotFoundException;
import com.accesa.entity.Transaction;
import com.accesa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public Mono<Page<Transaction>> getAllTransactions(Pageable pageable) {
        return transactionService.getAllTransactions(pageable);
    }

    @GetMapping(path = "all/{accountNumber}")
    public Mono<Page<Transaction>> getAllTransactionsByAccountNumber(@PathVariable("accountNumber") String accountNumber, Pageable pageable) {
        return transactionService.getTransactionsByAccountNumber(accountNumber, pageable);
    }

    @GetMapping(path = "{accountNumber}")
    public Mono<Page<Transaction>> getAllTransactionsByAccountNumberBetween(@PathVariable("accountNumber") String accountNumber,
                                                                      @RequestParam("from") LocalDate from,
                                                                      @RequestParam("to") LocalDate to,
                                                                      Pageable pageable) {
        return transactionService.getAllByAccountNumberAndTransactionDateBetween(accountNumber, from, to, pageable);
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> createTransaction(@RequestBody Transaction transaction) {
        return transactionService
                .createTransaction(transaction)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(AccountNotFoundException.class, error -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping(path = "{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable("id") Long id) {
        return transactionService
                .deleteTransaction(id)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}

