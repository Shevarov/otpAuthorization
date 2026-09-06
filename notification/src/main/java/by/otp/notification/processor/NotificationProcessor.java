package by.otp.notification.processor;

import by.otp.commonLib.dto.NotificationMessageDto;
import by.otp.commonLib.enumeration.ContactType;

/**
 * Delivers a notification message over a specific channel (SMS, email, ...).
 * Implementations are picked up automatically by {@link NotificationProcessorFactory}.
 */
public interface NotificationProcessor {
    void processEvent(NotificationMessageDto message);
    ContactType getContactType();
}
