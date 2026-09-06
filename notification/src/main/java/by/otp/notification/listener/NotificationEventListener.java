package by.otp.notification.listener;

import by.otp.commonLib.dto.NotificationMessageDto;
import by.otp.commonLib.enumeration.ContactType;
import by.otp.notification.processor.NotificationProcessor;
import by.otp.notification.processor.NotificationProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationProcessorFactory notificationProcessorFactory;

    @KafkaListener(
            topics = "${spring.kafka.topics.notification.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=by.otp.commonLib.dto.NotificationMessageDto"})
    public void consumeNotificationEvent(NotificationMessageDto message, Acknowledgment ack) {
        ContactType contactType = message.getContactType();
        NotificationProcessor processor = notificationProcessorFactory.getProcessor(contactType);
        processor.processEvent(message);
        ack.acknowledge();
    }
}
