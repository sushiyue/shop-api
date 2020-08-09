package com.fh.shop.api.cart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fh.shop.api.util.BigdecimelJackSon;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItem implements Serializable {
    private Long goodsId;

    private String goodsName;


    private int num;
    @JsonSerialize(using = BigdecimelJackSon.class)
    private BigDecimal price;
    @JsonSerialize(using = BigdecimelJackSon.class)
    private BigDecimal subPrice;

    private String image;

}
