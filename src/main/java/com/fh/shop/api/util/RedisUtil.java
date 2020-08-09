package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    //判断是否存在该数据
    public static boolean exists(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }
    //设置过期时间，这里给登录续命
    public static void expire(String key,int Sconds){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            jedis.expire(key,Sconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }
    //在缓存中存数据
    public static void set(String key,String value){
        Jedis jedis =null;
        try {
             jedis = RedisPool.getRource();
             jedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();;
            }
        }
    }
    //在缓存中存数据并设置过期时间
    public static void setex(String key,String value,int souceds){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            jedis.setex(key,souceds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }
    public static String get(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            String s = jedis.get(key);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }
    }
    public static void del(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }

    }

    public static Long delete(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRource();
            return jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null!=jedis){
                jedis.close();
            }
        }

    }
}
