package com.rajdip14.ecommerce.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestProducer {

    private final KafkaTemplate<String,PaymentRequest> kafkaTemplate;

    public void sendPaymentRequest(PaymentRequest paymentRequest) {
        log.info("Sending order created event for further payment processing");

        Message<PaymentRequest> message = MessageBuilder
                .withPayload(paymentRequest)
                .setHeader(KafkaHeaders.TOPIC, "order.created")
                //.setHeader(KafkaHeaders.KEY, paymentRequest.orderId())   used to provide partition key
                .build();

        kafkaTemplate.send(message);

        log.info("Order created event sent to topic 'order.created'");
    }
}
