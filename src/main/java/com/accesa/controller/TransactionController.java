package com.accesa.controller;
import com.accesa.entity.Transaction;
import com.accesa.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
    public Flux<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    @GetMapping(path = "{accountNumber}/all")
    public Flux<Transaction> getAllTransactionsByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return transactionService.getTransactionsByAccountNumber(accountNumber);
    }

    @GetMapping(path = "{accountNumber}")
    public Flux<Transaction> getAllTransactionsByAccountNumberBetween(@PathVariable("accountNumber") String accountNumber,
                                                                      @RequestParam("from") LocalDate from,
                                                                      @RequestParam("to") LocalDate to ) {
        return transactionService.getAllByAccountNumberAndTransactionDateBetween(accountNumber, from, to);
    }

    @PostMapping
    public Mono<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @DeleteMapping(path = "{id}")
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return transactionService.deleteTransaction(id);
    }
}

