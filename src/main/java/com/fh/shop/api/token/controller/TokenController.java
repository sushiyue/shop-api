package com.fh.shop.api.token.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.token.biz.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/token")
@Api(tags = "生成token")
public class TokenController {
   @Resource(name = "TOKEN")
    private TokenService tokenService;

   @GetMapping
   @ApiOperation("创建token")
   public ServerResponse createToken(){
       return tokenService.createToken();
   }
}
