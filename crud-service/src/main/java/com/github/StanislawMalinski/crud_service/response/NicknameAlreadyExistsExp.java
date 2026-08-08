package com.github.stanislawmalinski.crud_service.response;

public class NicknameAlreadyExistsExp extends Throwable {
    public NicknameAlreadyExistsExp(String errMsg){
        super(errMsg);
    }

    public NicknameAlreadyExistsExp(){
        super("User with this nickname already exists");
    }
}
