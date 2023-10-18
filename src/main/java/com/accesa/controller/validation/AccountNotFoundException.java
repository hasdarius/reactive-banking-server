package com.accesa.controller.validation;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException() {
        super("Account not found!");
    }
}