package by.otp.notification.processor.impl;

import by.otp.commonLib.dto.NotificationMessageDto;
import by.otp.commonLib.enumeration.ContactType;
import by.otp.notification.processor.NotificationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fallback processor used when no processor is registered for a given
 * {@link ContactType}. Registered under the {@code null} key so it becomes
 * the default in {@link by.otp.notification.processor.NotificationProcessorFactory}.
 */
@Component
public class FailsafeNotificationProcessor implements NotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FailsafeNotificationProcessor.class);

    @Override
    public void processEvent(NotificationMessageDto message) {
        logger.error("Contact type {} is not supported", message.getContactType());
        throw new UnsupportedOperationException(
                "Contact type %s is not supported".formatted(message.getContactType()));
    }

    @Override
    public ContactType getContactType() {
        return null;
    }
}
