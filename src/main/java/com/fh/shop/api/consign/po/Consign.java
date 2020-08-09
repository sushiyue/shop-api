package com.fh.shop.api.consign.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Consign implements Serializable {

    private Long id;

    private Long memberId;

    private String consignee;

    private String address;

    private String phone;

    private String email;

    private String addressName;

    private Integer status;
}
