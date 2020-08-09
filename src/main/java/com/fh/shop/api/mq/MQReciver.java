package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.config.MQConfig;
import com.fh.shop.api.exception.stockLessException;
import com.fh.shop.api.order.biz.OrderService;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.MailUtil;
import com.fh.shop.api.util.RedisUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class MQReciver {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private IProductMapper productMapper;

    @Resource(name="ORDER")
    private OrderService orderService;

    @RabbitListener(queues = MQConfig.GOODSQUEUE)
    public void hanldGoodsMessage(String info,Message message, Channel channel) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        try {
            MailMessage mailMessage = JSONObject.parseObject(info, MailMessage.class);
            mailUtil.sendMail(mailMessage.getEmail(),mailMessage.getTitle(),mailMessage.getContent());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(deliveryTag,false);
        }
    }

    @RabbitListener(queues = MQConfig.ORDERQUEUE)
    public void hanldOrderMessage(String msg, Message message, Channel channel) throws IOException {
        //手动ack删除消息队列中的消息
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        //从消息中间件取出消息，并进行处理
        OrderParam orderParam = JSONObject.parseObject(msg, OrderParam.class);
        Long memberId = orderParam.getMemberId();
        //通过会员的Id来获取redis中的购物车
        String JsonCart = RedisUtil.get(KeyUtil.buidCartKey(memberId));
        Cart cart = JSONObject.parseObject(JsonCart, Cart.class);
        if(cart == null){
            channel.basicAck(deliveryTag,false);
            return;
        }
        List<CartItem> cartItems = cart.getCartItems();
        //循环商品的Id饼放入集合中，循环与数据库中作对比，来看处库存
        List<Long>goodsIdList = cartItems.stream().map(x ->x.getGoodsId()).collect(Collectors.toList());
        //根据id 集合来查找对应的商品
        QueryWrapper<Product> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.in("id",goodsIdList);
        List<Product> list = productMapper.selectList(objectQueryWrapper);
        for (CartItem cartItem : cartItems) {
            for (Product product : list) {
                if(cartItem.getGoodsId().longValue()==product.getId().longValue()){
                    if(cartItem.getNum()>product.getStock()){
                        //提醒库存不足
                        RedisUtil.set(KeyUtil.buidStockless(memberId),"stock less");
                        channel.basicAck(deliveryTag,false);
                        return;
                    }
                }
            }
        }
        //创建订单
        try {
            orderService.createOrder(orderParam);
            channel.basicAck(deliveryTag,false);
        } catch (stockLessException e) {
            e.printStackTrace();
            //提醒库存不足
            RedisUtil.set(KeyUtil.buidStockless(memberId),"stock less");
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            e.printStackTrace();
            RedisUtil.set(KeyUtil.buidOrderError(memberId),"error");
            channel.basicAck(deliveryTag,false);
        }
    }
}
