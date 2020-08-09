package com.fh.shop.api.cart.controller;

import com.fh.shop.api.cart.biz.CartService;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.member.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/cart")
@Api(tags = "购物车接口")
public class CartController {

    @Resource(name="CART")
    private CartService cartService;

    @PostMapping
    @ApiOperation("添加购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId",value = "商品的Id",required = true,type = "long",paramType = "query"),
            @ApiImplicitParam(name = "num",value = "商品的数量",required = true,type = "int",paramType = "query"),
            @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,type = "string",paramType = "header"),
    })
    public ServerResponse addCart(HttpServletRequest request,Long goodsId,int num){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        return cartService.addCart(memberVo.getId(),goodsId,num);
    }

    @GetMapping
    @ApiOperation("获取购物车中商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,type = "string",paramType = "header"),
    })
    public ServerResponse findCart(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        return cartService.findCart(id);
    }
    //删除
    @DeleteMapping("deleteCart")
    public ServerResponse deleteCart(HttpServletRequest request, long goodsId){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        return cartService.deleteCart(id,goodsId);
    }
    //批量删除
    @DeleteMapping("bathDelete")
    public ServerResponse bathDelete(HttpServletRequest request,@RequestParam("idArr") List<Long> goodsIdArr){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        return cartService.bathDelete(id,goodsIdArr);
    }
    @GetMapping("findNum")
    public ServerResponse findNum(HttpServletRequest request){
        MemberVo memberVo = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long id = memberVo.getId();
        return cartService.findNum(id);
    }
}
