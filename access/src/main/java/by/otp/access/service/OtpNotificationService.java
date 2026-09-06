package by.otp.access.service;

import by.otp.access.config.properties.OtpProperties;
import by.otp.access.dto.otp.OtpChallenge;
import by.otp.access.producer.NotificationEventProducer;
import by.otp.commonLib.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpNotificationService {
    private final NotificationEventProducer notificationEventProducer;
    private final OtpProperties otpProperties;

    public void send(OtpChallenge challenge) {
        notificationEventProducer.send(NotificationMessageDto.builder()
                .contact(challenge.contact())
                .contactType(challenge.contactType())
                .message(otpProperties.messageTemplate() + challenge.code())
                .build());
    }
}
