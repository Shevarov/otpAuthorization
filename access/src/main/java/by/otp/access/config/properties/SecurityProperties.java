package by.otp.access.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        Duration accessTokenLifetime,
        Duration refreshTokenLifetime,
        String defaultRoleName
) {
}
