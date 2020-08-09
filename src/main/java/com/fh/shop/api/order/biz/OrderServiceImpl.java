package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.config.MQConfig;
import com.fh.shop.api.consign.biz.ConsignService;
import com.fh.shop.api.consign.mapper.ConsignMapper;
import com.fh.shop.api.consign.po.Consign;
import com.fh.shop.api.exception.stockLessException;
import com.fh.shop.api.order.mapper.OrderItemMapper;
import com.fh.shop.api.order.mapper.OrderMapper;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.order.po.OrderItem;
import com.fh.shop.api.order.vo.OrderVo;
import com.fh.shop.api.payLog.mapper.IPayLogMapper;
import com.fh.shop.api.payLog.po.PayLog;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("ORDER")
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Resource(name="consign")
    private ConsignService consignService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProductMapper productMapper;

    @Autowired
    private ConsignMapper consignMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IPayLogMapper payLogMapper;

    @Override
    public ServerResponse findList(Long id) {
        List<Consign> list = consignService.findList(id);
        String JsonCart = RedisUtil.get(KeyUtil.buidCartKey(id));
        Cart cart = JSONObject.parseObject(JsonCart, Cart.class);
        OrderVo order = new OrderVo();
        order.setConsignList(list);
        order.setCart(cart);
        return ServerResponse.success(order);
    }

    @Override
    public ServerResponse addOrder(Consign consign, Long id) {
        consign.setMemberId(id);
        consignService.addConsign(consign,id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findOrder(OrderParam orderParam) {
        //为了提高性能，将订单发送到消息队列中之前呢我们要将redis中的标志位删除
        Long memberId = orderParam.getMemberId();
        RedisUtil.del(KeyUtil.buidOrderKey(memberId));
        RedisUtil.del(KeyUtil.buidStockless(memberId));
        RedisUtil.del(KeyUtil.buidOrderError(memberId));
        String orderJson = JSONObject.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConfig.ORDEREXCHANGE,MQConfig.RDERKEY,orderJson);
        return ServerResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();
        String JsonCart = RedisUtil.get(KeyUtil.buidCartKey(memberId));
        Cart cart = JSONObject.parseObject(JsonCart, Cart.class);
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            Long goodsId = cartItem.getGoodsId();
            int num = cartItem.getNum();
            int row = productMapper.updateStock(goodsId,num);
            if(row==0){
               throw new stockLessException("stock less");
            }
        }
        Long consignId = orderParam.getConsignId();
        Consign consign = consignMapper.selectById(consignId);
        //插入订单表
        Order order = new Order();
        String orderId = IdWorker.getIdStr();
        order.setId(orderId);
        order.setAddress(consign.getAddress());
        order.setConsignee(consign.getConsignee());
        order.setConsignId(consignId);
        order.setCreateDate(new Date());
        order.setPhone(consign.getPhone());
        order.setMemberId(memberId);
        order.setTotalNum(cart.getTotalNum());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(SystemConstant.order.WEIT_PAY);//未支付
        order.setPayType(orderParam.getPayType());
        orderMapper.insert(order);
        //插入订单明细、
        List<CartItem> cartItems1 = cart.getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems1) {
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodsId(cartItem.getGoodsId());
            orderItem.setGoodsName(cartItem.getGoodsName());
            orderItem.setImage(cartItem.getImage());
            orderItem.setMemberId(memberId);
            orderItem.setNum(cartItem.getNum());
            orderItem.setOrderId(orderId);
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setSubPrice(cartItem.getSubPrice());
            orderItems.add(orderItem);
        }
        //批量添加
        orderItemMapper.BathInsert(orderItems);
        //插入支付日志表
        PayLog payLog = new PayLog();
        String ourTradeno = IdWorker.getIdStr();
        payLog.setOutTradeNo(ourTradeno);
        payLog.setMemberId(memberId);
        payLog.setOrderId(orderId);
        payLog.setCreateDate(new Date());
        payLog.setPayMoney(cart.getTotalPrice());
        payLog.setPayType(SystemConstant.pay_status.WEIT_PAY);
        payLog.setPayType(orderParam.getPayType());
        payLogMapper.insert(payLog);
        //将支付日志存入redis
        String payLogJson = JSONObject.toJSONString(payLog);
        RedisUtil.set(KeyUtil.buidPayLogKey(memberId),payLogJson);
        //删除redis中购物车支付的商品
        RedisUtil.del(KeyUtil.buidCartKey(memberId));
        //创建订单
        RedisUtil.set(KeyUtil.buidOrderKey(memberId),"ok");
    }

    @Override
    public ServerResponse getResult(Long id) {
        if(RedisUtil.exists(KeyUtil.buidStockless(id))){
            RedisUtil.del(KeyUtil.buidStockless(id));
            return ServerResponse.error(ResponseEnum.ORDER_STOCK_LESS);
        }
        if(RedisUtil.exists(KeyUtil.buidOrderKey(id))){
            RedisUtil.del(KeyUtil.buidOrderKey(id));
            return ServerResponse.success();
        }
        if(RedisUtil.exists(KeyUtil.buidOrderError(id))){
            return ServerResponse.error(ResponseEnum.ORDER_IS_ERROR);
        }
        return ServerResponse.error(ResponseEnum.ORDER_WAIT);
    }
}
