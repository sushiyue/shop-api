package com.fh.shop.api.member.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Member implements Serializable {

    private Long id;

    private String memName;//  【会员名】

    private String password;//    【密码】

    private String realName;//    【真实姓名】

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;//    【出生日期】

    private String email;//	    【邮箱】

    private String phone;//       【电话】

    private Long shengId;

    private Long shiId;

    private Long xianId;

    private String areaName;
}
