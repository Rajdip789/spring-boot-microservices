package com.rajdip14.ecommerce.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmationProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public void sendConfirmation(OrderConfirmation orderConfirmation) {
         log.info("Sending order confirmation event: {}", orderConfirmation.toString());

        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(TOPIC, "order.confirmed")
                .build();

        kafkaTemplate.send(message);

        log.info("Order confirmed event sent to topic 'order.confirmed'");
    }
}