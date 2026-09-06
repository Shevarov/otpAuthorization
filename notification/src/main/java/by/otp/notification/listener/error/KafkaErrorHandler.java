package by.otp.notification.listener.error;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

public class KafkaErrorHandler implements CommonErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(KafkaErrorHandler.class);

    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        handle(thrownException, consumer);
    }

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        handle(thrownException, consumer);
        return true;
    }

    private void handle(Exception exception, Consumer<?, ?> consumer) {
        if (exception instanceof RecordDeserializationException ex) {
            logger.error("Encountered RecordDeserializationException while reading a message", ex);
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else {
            logger.error("Encountered unknown exception while reading a message", exception);
        }
    }
}
