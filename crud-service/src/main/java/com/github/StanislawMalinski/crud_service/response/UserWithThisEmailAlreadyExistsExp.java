package com.github.stanislawmalinski.crud_service.response;

public class UserWithThisEmailAlreadyExistsExp extends Throwable {
    public UserWithThisEmailAlreadyExistsExp(String errorMsg){
        super(errorMsg);
    }
    public UserWithThisEmailAlreadyExistsExp() {
        super("The user with this email is already registered in our service.");
    }
}
