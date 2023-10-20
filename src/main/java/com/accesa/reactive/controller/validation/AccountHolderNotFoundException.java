package com.accesa.reactive.controller.validation;

public class AccountHolderNotFoundException extends AccountException {
    public AccountHolderNotFoundException() {
        super("Account holder not found!");
    }
}
