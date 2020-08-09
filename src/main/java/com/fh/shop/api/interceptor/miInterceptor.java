package com.fh.shop.api.interceptor;

import com.fh.shop.api.annotation.mi;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.exception.TokenException;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class miInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if(!method.isAnnotationPresent(mi.class)){
            return true;
        }
        String header = request.getHeader("x-token");
        if(StringUtils.isEmpty(header)){
            throw new TokenException(ResponseEnum.TOKEN_IS_MISS);
        }
        boolean exists = RedisUtil.exists(header);
        if(!exists){
            throw new TokenException(ResponseEnum.TOKEN_IS_fei);
        }
        Long flag = RedisUtil.delete(header);
        if(flag==0){
           throw new TokenException(ResponseEnum.TOKEN_IS_del);
        }
        return true;
    }

}
