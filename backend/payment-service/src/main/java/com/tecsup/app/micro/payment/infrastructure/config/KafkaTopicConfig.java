package com.tecsup.app.micro.payment.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentRequestedTopic(
            @Value("${app.kafka.topics.payment-requested:payment.requested}") String topicName
    ) {
        return topic(topicName);
    }

    @Bean
    public NewTopic paymentApprovedTopic(
            @Value("${app.kafka.topics.payment-approved:payment.approved}") String topicName
    ) {
        return topic(topicName);
    }

    @Bean
    public NewTopic paymentRejectedTopic(
            @Value("${app.kafka.topics.payment-rejected:payment.rejected}") String topicName
    ) {
        return topic(topicName);
    }

    @Bean
    public NewTopic paymentRequestedDlqTopic(
            @Value("${app.kafka.topics.payment-requested:payment.requested}") String topicName
    ) {
        return topic(topicName + ".dlq");
    }

    private NewTopic topic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
