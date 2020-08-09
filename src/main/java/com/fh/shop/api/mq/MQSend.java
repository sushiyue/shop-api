package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class MQSend {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendGoodsMessage(String info) {
        amqpTemplate.convertAndSend(MQConfig.GOODSEXCHANGE,MQConfig.GOODSKEY,info);
    }
}
