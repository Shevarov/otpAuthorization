package by.otp.access.producer.impl;

import by.otp.access.config.properties.NotificationProperties;
import by.otp.access.exception.NotificationDeliveryException;
import by.otp.access.producer.NotificationEventProducer;
import by.otp.commonLib.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationEventProducer implements NotificationEventProducer {
    private final NotificationProperties notificationProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(NotificationMessageDto message) {
        try {
            kafkaTemplate.send(notificationProperties.topic(), message.getContact(), message)
                    .get(notificationProperties.ackTimeout().toMillis(), TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            log.error("Kafka rejected notification for contact {}", message.getContact(), e.getCause());
            throw new NotificationDeliveryException(
                    "Failed to deliver notification to " + message.getContact(), e.getCause());
        } catch (TimeoutException e) {
            log.error("Kafka ack timed out for contact {}", message.getContact());
            throw new NotificationDeliveryException(
                    "Timed out delivering notification to " + message.getContact(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NotificationDeliveryException(
                    "Interrupted while delivering notification to " + message.getContact(), e);
        }
    }
}
