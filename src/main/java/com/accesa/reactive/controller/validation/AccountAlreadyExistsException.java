package com.accesa.reactive.controller.validation;

public class AccountAlreadyExistsException extends AccountException {
    public AccountAlreadyExistsException() {
        super("Account with same account number already exists!");
    }
}
