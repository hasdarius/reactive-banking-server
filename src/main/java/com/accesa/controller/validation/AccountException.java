package com.accesa.controller.validation;

public class AccountException extends RuntimeException{
    public AccountException(String errorMessage){
        super(errorMessage);
    }
}
