package com.github.stanislawmalinski.crud_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T>{
    public static final String OK = "OK";
    public static final String FAILED = "FAILED";
    private String status = OK;
    private String message;
    private T data;
    private Object metadata;


}
