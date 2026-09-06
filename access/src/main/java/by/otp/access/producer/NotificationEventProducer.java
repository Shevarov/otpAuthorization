package by.otp.access.producer;

import by.otp.access.exception.NotificationDeliveryException;
import by.otp.commonLib.dto.NotificationMessageDto;

public interface NotificationEventProducer {

    /**
     * @throws NotificationDeliveryException if delivery not access
     */
    void send(NotificationMessageDto message);
}
