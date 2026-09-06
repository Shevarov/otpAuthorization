package by.otp.notification.processor.impl;

import by.otp.commonLib.dto.NotificationMessageDto;
import by.otp.commonLib.enumeration.ContactType;
import by.otp.notification.processor.NotificationProcessor;
import by.otp.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationProcessorImpl implements NotificationProcessor {

    private static final String OTP_EMAIL_SUBJECT = "Your verification code";

    private final EmailService emailService;

    @Override
    public void processEvent(NotificationMessageDto message) {
        emailService.sendEmail(message.getContact(), OTP_EMAIL_SUBJECT, message.getMessage());
    }

    @Override
    public ContactType getContactType() {
        return ContactType.EMAIL;
    }
}
