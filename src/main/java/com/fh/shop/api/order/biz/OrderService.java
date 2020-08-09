package com.fh.shop.api.order.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.consign.po.Consign;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;

public interface OrderService {
    ServerResponse findList(Long id);

    ServerResponse addOrder(Consign consign, Long id);

    ServerResponse findOrder(OrderParam orderParam);

    void createOrder(OrderParam orderParam);

    ServerResponse getResult(Long id);
}
