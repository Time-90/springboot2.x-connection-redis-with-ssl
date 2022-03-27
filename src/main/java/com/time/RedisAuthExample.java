package com.time;

import com.time.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yonghao.tang
 * @version 1.0.0
 * @ClassName RedisTest.java
 * @Description
 * @createTime 2022年03月25日 15:13:00
 */
@SpringBootApplication
@RestController
public class RedisAuthExample {
    public static void main(String[] args) {
        SpringApplication.run(RedisAuthExample.class, args);
    }

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/get/{key}")
    public String get(@PathVariable String key) {
        return (String) redisUtil.get(key);
    }

    @GetMapping("/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisUtil.set(key, value, 3600);
        return (String) redisUtil.get(key);
    }
}
