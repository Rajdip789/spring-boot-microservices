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
public class PaymentStatusProducer {
    private final KafkaTemplate<String, PaymentStatus> kafkaTemplate;

    public void sendPaymentStatus(PaymentStatus paymentStatus) {
        log.info("Sending payment status event for finalizing the order");

        Message<PaymentStatus> message = MessageBuilder
                .withPayload(paymentStatus)
                .setHeader(KafkaHeaders.TOPIC, "payment.status")
                .build();

        kafkaTemplate.send(message);

        log.info("Payment status event sent to topic 'payment.status'");
    }
}
