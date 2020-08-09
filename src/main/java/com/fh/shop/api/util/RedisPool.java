package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private RedisPool(){

    }
    private static JedisPool jedisPool;

    private static  void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMinIdle(500);
        jedisPoolConfig.setMaxIdle(500);
        jedisPool = new JedisPool(jedisPoolConfig,"192.168.91.140",7020);
    }
    //静态块，只在jvm加载类，保证只创建一个连接池【连接池单例】
    //只能用静态方法
    static {
        initPool();
    }

    public static Jedis getRource(){
        return jedisPool.getResource();
    }
}
