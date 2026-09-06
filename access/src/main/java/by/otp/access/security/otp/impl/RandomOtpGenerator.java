package by.otp.access.security.otp.impl;

import by.otp.access.config.properties.OtpProperties;
import by.otp.access.security.otp.OtpGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Random;

@RefreshScope
@Component
@RequiredArgsConstructor
public class RandomOtpGenerator implements OtpGenerator {
    private final Random random;
    private final OtpProperties otpProperties;
    private String lengthStr;
    private int bound;

    @PostConstruct
    void init() {
        this.lengthStr = "%0" + otpProperties.codeLength() + "d";
        this.bound = (int) Math.pow(10, otpProperties.codeLength());
    }

    @Override
    public String generate() {
        return lengthStr.formatted(random.nextInt(bound));
    }
}
