package com.fh.shop.api;

import com.fh.shop.api.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class RedisTests {

    @Test
    public void add(){
        RedisUtil.set("username","liyijie");
        String username = RedisUtil.get("username");
        System.out.println(username);
    }
    @Test
    public void del(){
        RedisUtil.del("username");
    }
}
