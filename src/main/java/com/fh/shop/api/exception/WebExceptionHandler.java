package com.fh.shop.api.exception;

import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//表明异常处理器
@ControllerAdvice
public class WebExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    public ServerResponse handlerGlobalException(GlobalException e){
        ResponseEnum responseEnum = e.getResponseEnum();
        return ServerResponse.error(responseEnum);
    }

    @ResponseBody
    @ExceptionHandler
    public ServerResponse handlerTokenException(TokenException e){
        ResponseEnum responseEnum = e.getResponseEnum();
        return ServerResponse.error(responseEnum);
    }
}
