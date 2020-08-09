package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.consign.po.Consign;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class
Order implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private Long memberId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private int payType;

    private int totalNum;

    private BigDecimal totalPrice;

    private int status;

    private Long consignId;

    private String consignee;

    private String address;

    private String phone;


}
