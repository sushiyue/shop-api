package com.fh.shop.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String GOODSEXCHANGE = "goodsExchange";
    public static final String GOODSQUEUE = "goods-queue";
    public static final String GOODSKEY = "goods";

    public static final String ORDEREXCHANGE = "orderExchange";
    public static final String ORDERQUEUE = "order-queue";
    public static final String RDERKEY = "order";
    @Bean
    public DirectExchange goodsExchange(){
      return new DirectExchange(GOODSEXCHANGE,true,false);
  }
    @Bean
    public Queue goodsQueue(){
      return new Queue(GOODSQUEUE,true);
  }
    @Bean
    public Binding goodsBingding(){
      return BindingBuilder.bind(goodsQueue()).to(goodsExchange()).with(GOODSKEY);
  }
  @Bean
  public DirectExchange orderExchange(){
        return new DirectExchange(ORDEREXCHANGE,true,false);
  }

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDERQUEUE,true);
    }

    @Bean
    public Binding orderBingding(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(RDERKEY);
    }
}
