package com.deloitte.baseapp.configs.cache.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RedisService {

    @Value("${spring.redis.enabled}")
    private Boolean enabled;

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * TODO: separate into 2 categories of cold storage and warm storage
     *
     * @param key
     * @param value
     */
    public void setValue(final String key, final String value) {
        if (enabled)
            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(1));
    }

    /**
     * TODO: separate into 2 categories of cold storage and warm storage
     *
     * @param key
     */
    public String getValue(final String key) {
        if (!enabled) return null;

        return redisTemplate.opsForValue().get(key);
    }

}
