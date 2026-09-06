package by.otp.access.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "notification")
public record NotificationProperties(String topic, Duration ackTimeout) {
}
