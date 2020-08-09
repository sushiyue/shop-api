package com.fh.shop.api.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {

    private Long id;

    private String productName;

    private BigDecimal price;

    private String image;
}
