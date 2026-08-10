package com.github.stanislawmalinski.crud_service.response_request;

public class ExpUsernameAlreadyExists extends Throwable {
    public ExpUsernameAlreadyExists(String errorMsg){
        super(errorMsg);
    }
    public ExpUsernameAlreadyExists(){
        super("User with this username already exists.");
    }
}
