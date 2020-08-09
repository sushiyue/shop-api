package com.fh.shop.api.category.biz;

import com.fh.shop.api.common.ServerResponse;

public interface ICategoryService {
    ServerResponse findList();

    ServerResponse findcateList(Long id);
}
