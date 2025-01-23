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
public class OrderStatusProducer {

    private final KafkaTemplate<String, OrderStatus> kafkaTemplate;

    public void sendStatus(OrderStatus orderStatus) {
         log.info("Sending order confirmation event: {}", orderStatus.toString());

        Message<OrderStatus> message = MessageBuilder
                .withPayload(orderStatus)
                .setHeader(TOPIC, "order.status")
                .build();

        kafkaTemplate.send(message);

        log.info("Order status event sent to topic 'order.status'");
    }
}