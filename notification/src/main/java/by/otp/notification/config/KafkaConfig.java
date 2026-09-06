package by.otp.notification.config;

import by.otp.notification.listener.error.KafkaErrorHandler;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic notificationTopic(@Value("${spring.kafka.topics.notification.name}") String name,
                                       @Value("${spring.kafka.topics.notification.partitions}") int partitions,
                                       @Value("${spring.kafka.topics.notification.replicas}") int replicas) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new KafkaErrorHandler();
    }
}
