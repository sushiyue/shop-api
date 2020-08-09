package com.fh.shop.api.order.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.consign.po.Consign;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderVo implements Serializable {

   private List<Consign> consignList = new ArrayList<>();

   private Cart cart;


}
