package com.github.stanislawmalinski.crud_service.response_request;

public class ExpMatchDoesNotExists extends Throwable {
    public ExpMatchDoesNotExists(String errMsg){
        super(errMsg);
    }

    public ExpMatchDoesNotExists(){
        super("Match you are requesting doesn't exists.");
    }
}
