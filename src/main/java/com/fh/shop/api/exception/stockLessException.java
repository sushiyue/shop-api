package com.fh.shop.api.exception;


public class stockLessException extends RuntimeException{
    public stockLessException(String msg){
        super(msg);
    }
}
