package com.fh.shop.api.payLog.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.config.WxConfig;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.payLog.biz.IPayLogService;
import com.github.wxpay.sdk.WXPay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/payLog")
@Api(tags = "支付日志")
public class PayLogController {

    @Resource(name = "payLogService")
    private IPayLogService payLogService;


    @PostMapping("createNative")
    @ApiOperation("创建native支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value ="头信息",type = "string",required = true,paramType = "header"),
    })
    public ServerResponse createNative(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = memberVo.getId();
        return payLogService.createNative(memberId);

    }

    @GetMapping("queryStatus")
    public ServerResponse queryStatus(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = memberVo.getId();
        return payLogService.queryStatus(memberId);
    }
}
