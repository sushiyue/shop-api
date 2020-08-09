package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.product.biz.IProductService;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.BigdecimalUtil;
import com.fh.shop.api.util.KeyUtil;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Service("CART")
public class CartServiceImpl implements CartService{

    @Autowired
    private IProductMapper productMapper;

    @Override
    public ServerResponse addCart(Long id, Long goodsId, int num) {
        Product product = productMapper.selectById(goodsId);
        if(product==null){
            return ServerResponse.error(ResponseEnum.CART_PRODUCT_NULL);
        }
        if(product.getStatus()!=SystemConstant.PROFUCT_STATUS){
            return ServerResponse.error(ResponseEnum.CART_PRODUCT_STATUS_NOT);
        }
        //判断商品是否有对应的购物车
        //看看redis中是否有这个id的购物车
        String cartKey = KeyUtil.buidCartKey(id);
        String cartJson = RedisUtil.get(cartKey);
        //判断是否有购物车
        if(StringUtils.isNotBlank(cartJson)){
            //判断购物车中是否有我们添加的物品，如果有则直接放入，更新【数量，小计】，更新总数量，总价格
            Cart cart = JSONObject.parseObject(cartJson, Cart.class);
            List<CartItem> CartItems = cart.getCartItems();
            CartItem cartItem =null;
            for (CartItem cartItems : CartItems) {
                if(cartItems.getGoodsId().longValue()==goodsId.longValue()){
                    cartItem = cartItems;
                    //已经找到就终止
                    break;
                }
            }
            //更新【数量，小计】，更新总数量，总价格
            if(cartItem!=null){
                cartItem.setNum(cartItem.getNum()+num);
                int num1 = cartItem.getNum();
                if(num1<=0){
                    CartItems.remove(cartItem);
                }
                BigDecimal subPrice = BigdecimalUtil.mull(num1 + "", cartItem.getPrice().toString());
                cartItem.setSubPrice(subPrice);
                editCart(id,cart);
            }else{//如果购物车中没有我们添加的商品，则新建商品
                //建一个空的商品并将添加进来的商品信息赋值
                if(num<=0){
                    return ServerResponse.error(ResponseEnum.CART_PRODUCT_COUNT_ERROR);
                }
                CartItem cartItem1 = buidCartItem(num, product);
                cart.getCartItems().add(cartItem1);
                editCart(id,cart);
            }


        }//如果没有，则新建购物车，并更新总数量，总价格
        else{
            //创建购物车
            Cart cart = new Cart();
            if(num<=0){
                return ServerResponse.error(ResponseEnum.CART_PRODUCT_COUNT_ERROR);
            }
            //创建商品
            CartItem cartItem1 = buidCartItem(num, product);
            //将商品添加到购物车中
            cart.getCartItems().add(cartItem1); //初始化总数量
            //更新购物车
            editCart(id,cart);
        }

        return ServerResponse.success();
    }

    private CartItem buidCartItem(int num, Product product) {

        CartItem cartItem1 = new CartItem();
        cartItem1.setNum(num);
        cartItem1.setGoodsId(product.getId());
        cartItem1.setPrice(product.getPrice());
        cartItem1.setImage(product.getImage());
        cartItem1.setGoodsName(product.getProductName());
        BigDecimal subPrice = BigdecimalUtil.mull(num + "", product.getPrice().toString());
        cartItem1.setSubPrice(subPrice);
        return cartItem1;
    }

    private void editCart(Long id, Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        String cartKey = KeyUtil.buidCartKey(id);
        //组合cart并放入redis中
        //初始化一个总数量
        int totalNum = 0;
        //初始化一个总价格
        BigDecimal totalPrice = new BigDecimal("0");
        for (CartItem item : cartItems) {
            //每循环一次商品集合;如0+1，0+2就是1+2
            totalNum += item.getNum();
            totalPrice = BigdecimalUtil.add(totalPrice.toString(), item.getSubPrice().toString());
        }
        cart.setTotalNum(totalNum);
        cart.setTotalPrice(totalPrice);
        String JsonCart = JSONObject.toJSONString(cart);
        RedisUtil.set(cartKey, JsonCart);
    }

    @Override
    public ServerResponse findCart(Long id) {
        String cartKey = KeyUtil.buidCartKey(id);
        String cart = RedisUtil.get(cartKey);
        Cart cart1 = JSONObject.parseObject(cart, Cart.class);

        return ServerResponse.success(cart1);
    }

    @Override
    public ServerResponse deleteCart(Long id,Long goodsId) {
        String cartKey = KeyUtil.buidCartKey(id);
        String CartJson = RedisUtil.get(cartKey);
        Cart cart = JSONObject.parseObject(CartJson, Cart.class);
        List<CartItem> CartItems = cart.getCartItems();
        CartItem cartItem =null;
        for (CartItem cartItems : CartItems) {
            if(cartItems.getGoodsId().longValue()==goodsId.longValue()){
                cartItem = cartItems;
                break;
            }
        }
        if(cartItem!=null){
            //删除需要更新 总数量和总价格
            CartItems.remove(cartItem);
            //初始化一个总数量
            int totalNum = 0;
            //初始化一个总价格
            BigDecimal totalPrice = new BigDecimal("0");
            for (CartItem item : CartItems) {
                //每循环一次商品集合;如0+1，0+2就是1+2
                totalNum += item.getNum();
                totalPrice = BigdecimalUtil.add(totalPrice.toString(), item.getSubPrice().toString());
            }
            cart.setTotalNum(totalNum);
            cart.setTotalPrice(totalPrice);
            String JsonCart = JSONObject.toJSONString(cart);
            RedisUtil.set(cartKey, JsonCart);

        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findNum(Long id) {
        String cartKey = KeyUtil.buidCartKey(id);
        String cartJson = RedisUtil.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        if(cart==null){
            return ServerResponse.success(0);
        }
        return ServerResponse.success(cart.getTotalNum());
    }

    @Override
    public ServerResponse bathDelete(Long id, List<Long> goodsIdArr) {
        String cartKey = KeyUtil.buidCartKey(id);
        String cartJson = RedisUtil.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        List<CartItem> CartItems = cart.getCartItems();
        for (int i = 0; i <goodsIdArr.size() ; i++) {
            Long goodsId = goodsIdArr.get(i);
            CartItem cartItem =null;
            for (CartItem cartItems : CartItems) {
                if(goodsId.longValue()==cartItems.getGoodsId().longValue()){
                    cartItem = cartItems;
                    break;
                }
            }
            if(cartItem!=null){
                CartItems.remove(cartItem);
            }
        }
        int totalNum =0;
        BigDecimal totalPrice = new BigDecimal("0");
        for (CartItem cartItem : CartItems) {
            totalNum+=cartItem.getNum();
            totalPrice = BigdecimalUtil.add(totalPrice.toString(),cartItem.getSubPrice().toString());
        }
        cart.setTotalNum(totalNum);
        cart.setTotalPrice(totalPrice);
        String JsonCart = JSONObject.toJSONString(cart);
        RedisUtil.set(cartKey,JsonCart);
        return ServerResponse.success();
    }
}
