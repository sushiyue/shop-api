package com.fh.shop.api.category.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.category.mapper.ICategoryMapper;
import com.fh.shop.api.category.po.Category;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private ICategoryMapper categoryMapper;


    @Override
    public ServerResponse findList() {
        String categoryList = RedisUtil.get("categoryList");
        if(StringUtils.isNotBlank(categoryList)){
            List<Category> categories = JSONArray.parseArray(categoryList, Category.class);
            return ServerResponse.success(categories);
        }
        List<Category> categories = categoryMapper.selectList(null);
        String cate = JSONObject.toJSONString(categories);
        RedisUtil.setex("categoryList",cate,5*60);
        return ServerResponse.success(categories);
    }

    @Override
    public ServerResponse findcateList(Long id) {
        QueryWrapper<Category> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("pid",id);
        List<Category> categories = categoryMapper.selectList(objectQueryWrapper);
        return ServerResponse.success(categories);
    }
}
