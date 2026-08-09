package com.github.stanislawmalinski.crud_service.response;

import com.github.stanislawmalinski.crud_service.models.User;
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

    public static <T> Response<T> of(T data){
        Response<T> tmp = new Response<>();
        tmp.setData(data);
        return tmp;
    }
}
