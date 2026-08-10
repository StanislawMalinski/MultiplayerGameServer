package com.github.stanislawmalinski.crud_service.response_request;

public class ExpUserDoesNotExists extends Throwable {
    public ExpUserDoesNotExists(String errorMsg){
        super(errorMsg);
    }
    public ExpUserDoesNotExists() {
        super("The user you are requesting doesn't exists.");
    }
}
