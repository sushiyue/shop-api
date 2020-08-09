package com.fh.shop.api.order.controller;

import com.fh.shop.api.annotation.mi;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.consign.po.Consign;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.order.biz.OrderService;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.vo.OrderVo;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
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

@RestController
@RequestMapping("api/order")
@Api(tags = "订单接口")
public class OrderController {

    @Resource(name="ORDER")
    private OrderService orderService;

    @GetMapping("findList")

    public ServerResponse findList(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        return  orderService.findList(memberVo.getId());
    };

    //添加
    @PostMapping("addOrder")
    @ApiOperation("添加收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "consignee",value ="收货人",type = "string",required = true,paramType = "query"),
            @ApiImplicitParam(name = "address",value ="详细地址",type = "string",required = true,paramType = "query"),
            @ApiImplicitParam(name = "phone",value ="手机号",type = "string",required = true,paramType = "query"),
            @ApiImplicitParam(name = "email",value ="邮箱号",type = "string",required = true,paramType = "query"),
            @ApiImplicitParam(name = "addressName",value ="地址别名",type = "string",required = true,paramType = "query"),
            @ApiImplicitParam(name = "x-auth",value ="头信息",type = "string",required = true,paramType = "header"),
    })
    public ServerResponse addOrder(HttpServletRequest request, Consign consign){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        return orderService.addOrder(consign,memberVo.getId());
    }

    @PostMapping("findOrder")
    @ApiOperation("生成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value ="头信息",type = "string",required = true,paramType = "header"),
            @ApiImplicitParam(name = "consignId",value = "收件人的Id",required = true,type = "long",paramType = "query"),
            @ApiImplicitParam(name = "payType",value = "支付类型",type = "int",required =true,paramType = "query")
    })
    @mi
    public ServerResponse findOrder(HttpServletRequest request, OrderParam orderParam){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        orderParam.setMemberId(id);
        return  orderService.findOrder(orderParam);
    }

    @GetMapping("getResult")
    public ServerResponse getResult(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();

        return orderService.getResult(id);
    }
}
