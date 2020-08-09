package com.fh.shop.api.brand.controller;

import com.fh.shop.api.brand.biz.IBrandService;
import com.fh.shop.api.brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/brand")
@Api(tags = "后台品牌接口")
public class BrandController {
    @Resource(name="brand")
    private IBrandService brandService;

    @GetMapping
    @ApiOperation("查询所有品牌接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",type = "string",required = true,paramType = "header")
    })
    public ServerResponse findList(){
        return brandService.findList();
    }

    @PostMapping
    @ApiOperation("增加品牌接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",type = "string",required = true,paramType = "header"),
            @ApiImplicitParam(name = "brandName",value = "品牌名",type = "string",required = false,paramType = "query")
    })
    public ServerResponse addBrand(@RequestBody Brand brand){
        return brandService.addBrand(brand);
    }
    @DeleteMapping("/{brandId}")
    @ApiOperation("删除品牌接口")
    public ServerResponse delete(@PathVariable("brandId") Long id) {
        return brandService.delete(id);
    }

    @PutMapping
    @ApiOperation("修改品牌接口")
    public ServerResponse update(@ApiParam(value = "品牌的json格式",required = true) @RequestBody Brand brand) {
        return brandService.update(brand);
    }

    @DeleteMapping
    @ApiOperation("批量删除品牌接口")
    public ServerResponse deleteBatch(String ids) {
        return brandService.deleteBatch(ids);
    }

}
