package by.otp.access.service;

import by.otp.access.cache.CacheStore;
import by.otp.access.config.properties.OtpProperties;
import by.otp.access.exception.OtpBlockedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpCacheService {
    private final CacheStore cacheStore;
    private final PasswordEncoder encoder;
    private final OtpProperties otpProperties;

    public void storeOtp(String contact, String rawCode, Duration ttl) {
        cacheStore.put(otpKey(contact), encoder.encode(rawCode), ttl);
    }

    public boolean verifyOtpAndRemove(String contact, String rawCode) {
        if (isBlocked(contact)) {
            throw new OtpBlockedException();
        }

        Optional<String> hashed = cacheStore.get(otpKey(contact));
        if (hashed.isEmpty() || !encoder.matches(rawCode, hashed.get())) {
            registerFailedAttempt(contact);
            return false;
        }

        cacheStore.delete(attemptsKey(contact));
        cacheStore.delete(otpKey(contact));
        return true;
    }

    private void registerFailedAttempt(String contact) {
        long attempts = cacheStore.increment(attemptsKey(contact), otpProperties.verify().attemptsTtl());
        if (attempts >= otpProperties.verify().maxAttempts()) {
            throw new OtpBlockedException();
        }
    }

    private boolean isBlocked(String contact) {
        return cacheStore.get(attemptsKey(contact))
                .map(Long::parseLong)
                .map(attempts -> attempts >= otpProperties.verify().maxAttempts())
                .orElse(false);
    }

    private String otpKey(String contact) {
        return otpProperties.keyPrefix() + contact;
    }

    private String attemptsKey(String contact) {
        return otpProperties.attemptsKeyPrefix() + contact;
    }

    public void invalidate(String contact) {
        cacheStore.delete(otpKey(contact));
    }
}
