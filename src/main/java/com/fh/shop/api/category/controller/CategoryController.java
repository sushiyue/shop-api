package com.fh.shop.api.category.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.category.biz.ICategoryService;
import com.fh.shop.api.common.ServerResponse;
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
@RequestMapping("api/category")
@Api(tags = "分类后台接口")
public class CategoryController {

    @Resource(name="categoryService")
    private ICategoryService categoryService;

    @GetMapping
    @ApiOperation("查询分类接口")
    @check
    public ServerResponse findList(){
        return categoryService.findList();
    }
}
