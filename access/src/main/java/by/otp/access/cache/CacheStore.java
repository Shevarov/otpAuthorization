package by.otp.access.cache;

import java.time.Duration;
import java.util.Optional;

/**
 * Abstraction over a simple TTL-based key/value store, used for OTP codes
 * and rate-limit counters. Allows the storage backend (Redis, in-memory,
 * or anything else) to be swapped via configuration without touching the
 * business logic that depends on it.
 */
public interface CacheStore {

    void put(String key, String value, Duration ttl);

    Optional<String> get(String key);

    void delete(String key);

    boolean exists(String key);

    /**
     * Atomically increments the counter stored at {@code key} (starting from
     * 0 if absent) and returns the new value. If this call creates the key,
     * {@code ttlIfAbsent} is applied to it.
     */
    long increment(String key, Duration ttlIfAbsent);
}
