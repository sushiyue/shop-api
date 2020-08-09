package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.annotation.check;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.MD5Util;
import com.fh.shop.api.util.MailUtil;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //处理跨域访问
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        //允许访问自定义的handler
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,context-type,x-token");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT,POST,DELETE,GET");
        //处理特殊请求options
        String options = request.getMethod();
        if(options.equalsIgnoreCase("options")){
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //如果方法上哟哟自定义注解则放过
        if(method.isAnnotationPresent(check.class)){
            return true;
        }
        //获取头信息
        String header = request.getHeader("x-auth");
        //判断头信息是否为空
        if(StringUtils.isEmpty(header)){
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_IS_EIXIS);
        }
        //判断头信息是否完整
        String[] headArr = header.split("\\.");
        if(headArr.length!=2){
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_IS_BUWANZHENG);
        }
        //获取Base64后的用户信息
        String Base64MemJson = headArr[0];
        //获取Base64后的签名
        String Base64Sign = headArr[1];
        //将得到的用户信息进行签名生成新的签名与得到签名对比【验签】
        String newSign = MD5Util.sign(Base64MemJson, MD5Util.SECRET);
        //在将新签名进行Base64编译
        String newBase64Sign = Base64.getEncoder().encodeToString(newSign.getBytes("utf-8"));
        if(!newBase64Sign.equals(Base64Sign)){
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_IS_CHANGE);
        }
        //判断登录是否超时
        //首先获取用户信息，将Base64位的用户信息·进行解码再进行new String转为java对象
        String MemberJSON = new String(Base64.getDecoder().decode(Base64MemJson), "utf-8");
        MemberVo memberVo = JSONObject.parseObject(MemberJSON, MemberVo.class);
        //判断redis中有没有该数据
        boolean exists = RedisUtil.exists(KeyUtil.buidMember(memberVo.getUuid(), memberVo.getId()));
        if(!exists){
            Cookie cookie = new Cookie("fh-token","");
            cookie.setMaxAge(0);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
            throw new GlobalException(ResponseEnum.LOGIN_HANDLER_TIME_OUT);
        }
        //没有超时，则续命
        RedisUtil.expire(KeyUtil.buidMember(memberVo.getUuid(),memberVo.getId()),KeyUtil.sconds);
        //将用户信息存入request中方便后续使用
        request.setAttribute(SystemConstant.CURR_MEMBER,memberVo);
        return true;
    }
}
