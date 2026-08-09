package com.github.stanislawmalinski.crud_service.response;

public class ExpUserWithThisEmailAlreadyExists extends Throwable {
    public ExpUserWithThisEmailAlreadyExists(String errorMsg){
        super(errorMsg);
    }
    public ExpUserWithThisEmailAlreadyExists() {
        super("The user with this email is already registered in our service.");
    }
}
