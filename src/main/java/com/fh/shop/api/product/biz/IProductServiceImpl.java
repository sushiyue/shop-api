package com.fh.shop.api.product.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.product.vo.ProductVo;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class IProductServiceImpl implements IProductService {

    @Autowired
    private IProductMapper productMapper;

    @Value("${stock.less}")
    private long stock;
    @Override
    public ServerResponse findList() {
        String productList = RedisUtil.get("productList");
        if(StringUtils.isNotBlank(productList)){
            List<ProductVo> list = JSONObject.parseArray(productList, ProductVo.class);
            return ServerResponse.success(list);
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper();
        queryWrapper.select("id","productName","price","image");
        queryWrapper.eq("isHot", 1);
        queryWrapper.eq("status", 1);
        List<Product> list = productMapper.selectList(queryWrapper);
        List<ProductVo>list1 = new ArrayList();
        for (Product product:list) {
            ProductVo productVo = new ProductVo();
            productVo.setId(product.getId());
            productVo.setProductName(product.getProductName());
            productVo.setPrice(product.getPrice());
            productVo.setImage(product.getImage());
            list1.add(productVo);
        }
        String s = JSONObject.toJSONString(list1);
        RedisUtil.setex("productList",s,10*60);
        return ServerResponse.success(list1);
    }

    @Override
    public List<Product> findPorduct() {
        QueryWrapper<Product> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lt("stock",stock);
        List<Product> products = productMapper.selectList(objectQueryWrapper);
        return products;
    }
}
