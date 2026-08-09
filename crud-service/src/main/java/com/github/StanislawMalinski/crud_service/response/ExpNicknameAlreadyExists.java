package com.github.stanislawmalinski.crud_service.response;

public class ExpNicknameAlreadyExists extends Throwable {
    public ExpNicknameAlreadyExists(String errMsg){
        super(errMsg);
    }

    public ExpNicknameAlreadyExists(){
        super("User with this nickname already exists");
    }
}
