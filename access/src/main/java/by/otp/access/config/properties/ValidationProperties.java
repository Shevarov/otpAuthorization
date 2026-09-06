package by.otp.access.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation")
public record ValidationProperties(String phonePattern, String emailPattern) {
}
