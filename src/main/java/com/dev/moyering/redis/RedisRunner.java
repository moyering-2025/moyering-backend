package com.dev.moyering.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRunner implements CommandLineRunner {

    private final RedisTestService redisTestService;

    @Override
    public void run(String... args) throws Exception {
        redisTestService.testSet();
        String val = redisTestService.testGet();
        System.out.println("🟢 Redis 값 = " + val);
    }
}
