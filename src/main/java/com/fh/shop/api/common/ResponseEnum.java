package com.fh.shop.api.common;

public enum ResponseEnum {
     TOKEN_IS_MISS(5000,"头信息为空"),
    TOKEN_IS_fei(5001,"非法访问"),
    TOKEN_IS_del(5002,"重复提交了"),

    ORDER_STOCK_LESS(4000,"库存不足"),
    ORDER_WAIT(4001,"消息排队中，请稍等"),
    ORDER_IS_ERROR(4002,"下订单失败"),
    ORDER_IS_TIME_OUT(4003,"超时未支付"),

    CART_PRODUCT_COUNT_ERROR(3002,"对不起，请您合理填写商品数量"),
    CART_PRODUCT_STATUS_NOT(3001,"商品已下架"),
    CART_PRODUCT_NULL(3000,"商品不存在"),

    LOGIN_HANDLER_IS_EIXIS(2003,"头信息为空"),
    LOGIN_HANDLER_IS_BUWANZHENG(2004,"头信息不完整"),
    LOGIN_HANDLER_IS_CHANGE(2005,"用户信息被篡改"),
    LOGIN_HANDLER_TIME_OUT(2006,"登录超时"),
    LOGIN_MEMBER_IS_EIXIS(2000,"会员名或密码为空"),
    LOGIN_MEMBER_NAME_IS_NOTEIXIS(2001,"会员名不存在"),
    LOGIN_MEMBER_PASSWORD_IS_ERROR(2002,"密码错误"),

    REG_PHONE_IS_EIXIS(1003,"手机号已存在"),
    REG_EMAIL_IS_EIXIS(1002,"邮箱已存在"),
    REG_MEMNAME_IS_EIXIS(1001,"会员已存在"),
   REG_MEMBER_IS_NULL(1000,"会员不能为空");

    private int code;
    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
