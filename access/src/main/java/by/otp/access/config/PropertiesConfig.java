package by.otp.access.config;

import by.otp.access.config.properties.NotificationProperties;
import by.otp.access.config.properties.OtpProperties;
import by.otp.access.config.properties.SecurityProperties;
import by.otp.access.config.properties.ValidationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SecurityProperties.class,
        OtpProperties.class,
        NotificationProperties.class,
        ValidationProperties.class
})
public class PropertiesConfig {
}
