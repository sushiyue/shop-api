package com.fh.shop.api;


import com.fh.shop.api.config.WxConfig;
import com.github.wxpay.sdk.WXPay;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PayTest {

    @Test
    public void test1() throws Exception {

        WxConfig config = new WxConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        //商品标题
        data.put("body", "飞狐乐购");
        //订单编号
        data.put("out_trade_no", "201609091054111");
        //默认人民币
        //订单价格这里指的是分
        data.put("total_fee", "100");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2() throws Exception {
        WxConfig config = new WxConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", "201609091054111");

        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
