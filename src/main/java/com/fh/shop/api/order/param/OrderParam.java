package com.fh.shop.api.order.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderParam implements Serializable {

    private Long memberId;

    private Long consignId;

    private int payType;
}
