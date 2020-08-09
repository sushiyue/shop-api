package com.fh.shop.api.cart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fh.shop.api.util.BigdecimelJackSon;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart implements Serializable {

    private int totalNum;

    @JsonSerialize(using = BigdecimelJackSon.class)
    private BigDecimal totalPrice;

    private List<CartItem>cartItems = new ArrayList<>();
}
