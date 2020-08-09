package com.fh.shop.api.cart.biz;

import com.fh.shop.api.common.ServerResponse;

import java.util.List;

public interface CartService {
    ServerResponse addCart(Long id, Long goodsId, int num);

    ServerResponse findCart(Long id);

    ServerResponse deleteCart(Long id,Long goodsId);

    ServerResponse findNum(Long id);

    ServerResponse bathDelete(Long id, List<Long> goodsIdArr);
}
