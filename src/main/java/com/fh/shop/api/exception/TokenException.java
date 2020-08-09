package com.fh.shop.api.exception;

import com.fh.shop.api.common.ResponseEnum;

public class TokenException extends RuntimeException{
    private ResponseEnum responseEnum;

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public TokenException(ResponseEnum responseEnum){
       this.responseEnum =responseEnum;
    }
}
