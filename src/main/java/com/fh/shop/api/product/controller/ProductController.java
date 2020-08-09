package com.fh.shop.api.product.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.biz.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/product")
@Api(tags = "商品后台接口")
public class ProductController {

    @Resource(name="productService")
    private IProductService productService;

    @GetMapping
    @ApiOperation("查询热销商品接口")
    @check
    public ServerResponse findList(){
        return  productService.findList();
    }
}
