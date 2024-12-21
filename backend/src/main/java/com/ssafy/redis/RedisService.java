package com.ssafy.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {
        if(redisTemplate.opsForValue().get(key) == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void setDataWithExpiration(String key, String value, Long time) {
        if(this.getData(key) != null) {
            this.deleteData(key);
        }

        Duration expireDuration = Duration.ofMillis(time);
        redisTemplate.opsForValue().set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
