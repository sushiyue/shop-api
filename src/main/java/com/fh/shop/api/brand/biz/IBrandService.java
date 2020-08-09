package com.fh.shop.api.brand.biz;

import com.fh.shop.api.brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;

public interface IBrandService {
    ServerResponse findList();

    ServerResponse addBrand(Brand brand);

    ServerResponse delete(Long id);

    ServerResponse update(Brand brand);

    ServerResponse deleteBatch(String ids);
}
