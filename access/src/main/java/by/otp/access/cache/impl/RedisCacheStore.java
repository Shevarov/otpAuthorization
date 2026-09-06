package by.otp.access.cache.impl;

import by.otp.access.cache.CacheStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cache.provider", havingValue = "redis", matchIfMissing = true)
public class RedisCacheStore implements CacheStore {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void put(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public long increment(String key, Duration ttlIfAbsent) {
        Long value = redisTemplate.opsForValue().increment(key);
        long count = value == null ? 0L : value;
        if (count == 1L) {
            redisTemplate.expire(key, ttlIfAbsent);
        }
        return count;
    }
}
