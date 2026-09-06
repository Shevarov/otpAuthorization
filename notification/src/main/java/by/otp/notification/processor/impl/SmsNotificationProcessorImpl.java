package by.otp.notification.processor.impl;

import by.otp.commonLib.dto.NotificationMessageDto;
import by.otp.commonLib.enumeration.ContactType;
import by.otp.notification.processor.NotificationProcessor;
import by.otp.notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsNotificationProcessorImpl implements NotificationProcessor {

    private final SmsService smsService;

    @Override
    public void processEvent(NotificationMessageDto message) {
        smsService.sendSms(message.getContact(), message.getMessage());
    }

    @Override
    public ContactType getContactType() {
        return ContactType.PHONE;
    }
}
