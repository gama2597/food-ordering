package com.tecsup.app.micro.order.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderCreatedTopic(@Value("${app.kafka.topics.order-created:order.created}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentRequestedTopic(@Value("${app.kafka.topics.payment-requested:payment.requested}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentApprovedTopic(@Value("${app.kafka.topics.payment-approved:payment.approved}") String topicName) {
        return topic(topicName);
    }

    @Bean
    public NewTopic paymentRejectedTopic(@Value("${app.kafka.topics.payment-rejected:payment.rejected}") String topicName) {
        return topic(topicName);
    }

    @Bean
    public NewTopic deliveryAssignedTopic(@Value("${app.kafka.topics.delivery-assigned:delivery.assigned}") String topicName) {
        return topic(topicName);
    }

    @Bean
    public NewTopic deliveryStartedTopic(@Value("${app.kafka.topics.delivery-started:delivery.started}") String topicName) {
        return topic(topicName);
    }

    @Bean
    public NewTopic deliveryDeliveredTopic(@Value("${app.kafka.topics.delivery-delivered:delivery.delivered}") String topicName) {
        return topic(topicName);
    }

    @Bean
    public NewTopic paymentApprovedDlqTopic(@Value("${app.kafka.topics.payment-approved:payment.approved}") String topicName) {
        return topic(topicName + ".dlq");
    }

    @Bean
    public NewTopic paymentRejectedDlqTopic(@Value("${app.kafka.topics.payment-rejected:payment.rejected}") String topicName) {
        return topic(topicName + ".dlq");
    }

    @Bean
    public NewTopic deliveryAssignedDlqTopic(@Value("${app.kafka.topics.delivery-assigned:delivery.assigned}") String topicName) {
        return topic(topicName + ".dlq");
    }

    @Bean
    public NewTopic deliveryStartedDlqTopic(@Value("${app.kafka.topics.delivery-started:delivery.started}") String topicName) {
        return topic(topicName + ".dlq");
    }

    @Bean
    public NewTopic deliveryDeliveredDlqTopic(@Value("${app.kafka.topics.delivery-delivered:delivery.delivered}") String topicName) {
        return topic(topicName + ".dlq");
    }

    private NewTopic topic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
