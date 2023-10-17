package com.accesa.controller.validation;

public class AccountContainsMoneyException extends RuntimeException {
    public AccountContainsMoneyException() {
        super("Account still contains money!");
    }
}
