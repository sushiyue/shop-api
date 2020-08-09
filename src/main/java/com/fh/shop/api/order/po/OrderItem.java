package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_order_detatail")
public class OrderItem {

    private Long id;

    private String orderId;

    private Long memberId;

    private Long goodsId;

    private String goodsName;

    private BigDecimal price;

    private BigDecimal subPrice;

    private int num;

    private String image;

}
