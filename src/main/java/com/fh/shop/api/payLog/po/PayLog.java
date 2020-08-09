package com.fh.shop.api.payLog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_payLog")
public class PayLog implements Serializable {

    @TableId(type = IdType.INPUT,value = "outTradeNo")
    private String outTradeNo;

    private Long memberId;

    private String orderId;

    private Date createDate;

    private Date payTime;

    private BigDecimal payMoney;

    private int payType;

    private int payStatus;

    private String transactionId;
}
