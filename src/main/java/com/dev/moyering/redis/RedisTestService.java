package com.dev.moyering.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void testSet() {
        redisTemplate.opsForValue().set("mytest", "hello-redis");
    }

    public String testGet() {
        return (String) redisTemplate.opsForValue().get("mytest");
    }
}
