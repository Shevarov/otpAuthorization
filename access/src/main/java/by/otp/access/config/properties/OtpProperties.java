package by.otp.access.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "otp")
public record OtpProperties(
        String keyPrefix,
        String attemptsKeyPrefix,
        Duration ttl,
        int codeLength,
        String messageTemplate,
        Verify verify
) {
    public record Verify(int maxAttempts, Duration attemptsTtl) {
    }
}
