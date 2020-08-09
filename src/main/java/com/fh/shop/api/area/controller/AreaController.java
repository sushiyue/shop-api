package com.fh.shop.api.area.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.area.biz.IAreaService;
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
@RequestMapping("api/area")
@Api(tags = "地区后台接口")
public class AreaController {
    @Resource(name="areaService")
    private IAreaService areaService;

    @GetMapping
    @check
    @ApiOperation("根据pid来查询地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "地区的父id",type = "long",required = false,paramType = "query")
    })
    public ServerResponse findList(Long id){
        return areaService.findList(id);
    }
}
