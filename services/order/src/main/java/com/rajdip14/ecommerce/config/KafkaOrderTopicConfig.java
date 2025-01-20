package com.rajdip14.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaOrderTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order.created")
//                .partitions(3)
//                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderConfirmationTopic() {
        return TopicBuilder
                .name("order.confirmation")
                .build();
    }
}
