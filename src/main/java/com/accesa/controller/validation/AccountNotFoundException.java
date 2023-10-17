package com.accesa.controller.validation;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Account not found!");
    }
}