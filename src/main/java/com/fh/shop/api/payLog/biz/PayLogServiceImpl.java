package com.fh.shop.api.payLog.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.config.WxConfig;
import com.fh.shop.api.order.mapper.OrderMapper;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.payLog.mapper.IPayLogMapper;
import com.fh.shop.api.payLog.po.PayLog;
import com.fh.shop.api.util.BigdecimalUtil;
import com.fh.shop.api.util.DateUtil;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payLogService")
public class PayLogServiceImpl implements IPayLogService {

    @Autowired
    private IPayLogMapper payLogMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ServerResponse createNative(Long memberId) {
        String PayLogJson = RedisUtil.get(KeyUtil.buidPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(PayLogJson, PayLog.class);
        String outTradeNo = payLog.getOutTradeNo();
        BigDecimal payMoney = payLog.getPayMoney();
        String orderId = payLog.getOrderId();
        WxConfig config = new WxConfig();
      try{
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            //商品标题
            data.put("body", "飞狐乐购");
            //订单编号
            data.put("out_trade_no", outTradeNo);
            //默认人民币
            //订单价格这里指的是分
            int money = BigdecimalUtil.mull(payMoney.toString(), "100").intValue();
            data.put("total_fee", money + "");
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            String date = DateUtil.addmin(new Date(), 2, DateUtil.FULLTIME);
            data.put("time_expire",date);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if(!return_code.equals("SUCCESS")){
                return ServerResponse.error(99999,return_msg);
            }
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("err_code_des");
            if(!result_code.equals("SUCCESS")){
                return ServerResponse.error(99999,err_code_des);
            }
            //证明上述两个参数都成功才能证明code_url有信息
            String code_url = resp.get("code_url");
            Map<String,String> result_data = new HashMap();
            result_data.put("code_url",code_url);
            result_data.put("orderId",orderId);
            result_data.put("money",payMoney.toString());
            return ServerResponse.success(result_data);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }
    }

    @Override
    public ServerResponse queryStatus(Long memberId) {
        String PayLogJson = RedisUtil.get(KeyUtil.buidPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(PayLogJson, PayLog.class);
        String outTradeNo = payLog.getOutTradeNo();
        String orderId = payLog.getOrderId();
        WxConfig config = new WxConfig();
        try {
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", outTradeNo);
        int count = 0;
         while (true){
             Map<String, String> resp = wxpay.orderQuery(data);
             System.out.println(resp);
             String return_code = resp.get("return_code");
             String return_msg = resp.get("return_msg");
             if(!return_code.equals("SUCCESS")){
                 return ServerResponse.error(99999,return_msg);
             }
             String result_code = resp.get("result_code");
             String err_code_des = resp.get("err_code_des");
             if(!result_code.equals("SUCCESS")){
                 return ServerResponse.error(99999,err_code_des);
             }
             String trade_state = resp.get("trade_state");
             if(trade_state.equals("SUCCESS")){
                 //支付成功
                 String transaction_id = resp.get("transaction_id");
                 //更新订单表中数据
                 Order order = new Order();
                 order.setId(orderId);
                 order.setStatus(SystemConstant.order.PAY_SUCCESS);
                 order.setPayTime(new Date());
                 orderMapper.updateById(order);
                 //更新支付日志表
                 PayLog payLog1 = new PayLog();
                 payLog1.setPayStatus(SystemConstant.pay_status.PAY_SUCCESS);
                 payLog1.setPayTime(new Date());
                 payLog1.setTransactionId(transaction_id);
                 payLog1.setOutTradeNo(outTradeNo);
                 payLogMapper.updateById(payLog1);
                 //收尾，将redis中支付日志删除
                 RedisUtil.del(KeyUtil.buidPayLogKey(memberId));
                 return ServerResponse.success();
             }
             Thread.sleep(2000);
             count++;
             if(count>60){
               return ServerResponse.error(ResponseEnum.ORDER_IS_TIME_OUT);
             }
         }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }
}
