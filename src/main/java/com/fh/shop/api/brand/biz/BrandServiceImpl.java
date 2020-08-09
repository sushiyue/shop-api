package com.fh.shop.api.brand.biz;

import com.fh.shop.api.brand.mapper.IBrandMapper;
import com.fh.shop.api.brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service("brand")
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl implements IBrandService{

    @Autowired
    private IBrandMapper brandMapper;

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findList() {
        List<Brand> list = brandMapper.findList();
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse addBrand(Brand brand) {
        brandMapper.addBrand(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse delete(Long id) {
        brandMapper.delete(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse update(Brand brand) {
        brandMapper.update(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
        if(StringUtils.isNotBlank(ids)){
            String[] arr = ids.split(",");
            List<Long> list = Arrays.stream(arr).map(x -> Long.parseLong(x)).collect((Collectors.toList()));
            brandMapper.deleteBatch(list);
        }
        return ServerResponse.success();
    }
}
