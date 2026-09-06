package by.otp.access.cache.impl;

import by.otp.access.cache.CacheStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(name = "cache.provider", havingValue = "memory")
public class InMemoryCacheStore implements CacheStore {

    private record Entry(String value, Instant expiresAt) {
        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }

    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public void put(String key, String value, Duration ttl) {
        store.put(key, new Entry(value, Instant.now().plus(ttl)));
    }

    @Override
    public Optional<String> get(String key) {
        Entry entry = store.get(key);
        if (entry == null || entry.isExpired()) {
            store.remove(key);
            return Optional.empty();
        }
        return Optional.of(entry.value());
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }

    @Override
    public boolean exists(String key) {
        return get(key).isPresent();
    }

    @Override
    public synchronized long increment(String key, Duration ttlIfAbsent) {
        long next = get(key).map(Long::parseLong).orElse(0L) + 1;
        Instant expiresAt = store.containsKey(key) && !store.get(key).isExpired()
                ? store.get(key).expiresAt()
                : Instant.now().plus(ttlIfAbsent);
        store.put(key, new Entry(String.valueOf(next), expiresAt));
        return next;
    }
}
