package com.fh.shop.api.exception;

import com.fh.shop.api.common.ResponseEnum;

public class GlobalException extends RuntimeException{
    private ResponseEnum responseEnum;

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public GlobalException(ResponseEnum responseEnum){
       this.responseEnum =responseEnum;
    }
}
