package com.accesa.controller.validation;

public class AccountContainsMoneyException extends AccountException {
    public AccountContainsMoneyException() {
        super("Account still contains money!");
    }
}
