package com.fh.shop.api.member.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/member")
@Api(tags = "会员接口")
public class MemberController {

    @Resource(name="memberService")
    private IMemberService memberService;

    @PostMapping
    @check
    @ApiOperation("会员注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memName", value = "会员名", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "birthday", value = "生日", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "shengId", value = "省id", type = "long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "shiId", value = "市Id", type = "long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "xianId", value = "县Id", type = "long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "areaName", value = "地区名", type = "string", required = false, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", type = "string", required = true, paramType = "query")
    })
    public ServerResponse addMember(Member member){
       return memberService.addMember(member);
    }
    @GetMapping("validaterMemName")
    @check
    public ServerResponse validaterMemName(String memName){
        return   memberService.validaterMemName(memName);

    }
    @GetMapping("validaterPhone")
    @check
    public ServerResponse validaterPhone(String phone){
        return   memberService.validaterPhone(phone);

    }
    @GetMapping("validaterEmail")
    @check
    public ServerResponse validaterEmail(String email){
        return   memberService.validaterEmail(email);

    }
    @check
    @PostMapping("login")
    @ApiOperation("会员登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memName", value = "会员名", type = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", type = "string", required = true, paramType = "query")
    })
    public ServerResponse login(String memName,String password){
        return memberService.login(memName,password);
    }

    @GetMapping("findMember")
    @ApiOperation("获取会员信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头信息",type = "string",required = true,paramType = "header")
    })
    public ServerResponse findMember(HttpServletRequest request){
        MemberVo memberVo = (MemberVo)request.getAttribute(SystemConstant.CURR_MEMBER);
        return ServerResponse.success(memberVo);
    }

    @GetMapping("logout")
    @ApiOperation("会员退出接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头信息",type = "string",required = true,paramType = "header")
    })
    public ServerResponse logout(HttpServletRequest request){
        MemberVo memberVo = (MemberVo)request.getAttribute(SystemConstant.CURR_MEMBER);
        RedisUtil.del(KeyUtil.buidMember(memberVo.getUuid(),memberVo.getId()));
        return ServerResponse.success();
    }
}
