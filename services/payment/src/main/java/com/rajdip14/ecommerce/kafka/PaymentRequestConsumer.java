package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.exception.PaymentProcessingException;
import com.rajdip14.ecommerce.payment.PaymentMapper;
import com.rajdip14.ecommerce.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestConsumer {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusProducer statusProducer;

    @KafkaListener(topics = "order.created")
    @Transactional
    public void consumePaymentRequest(PaymentRequest paymentRequest) {
        log.info("Consuming message from order.created Topic: {}", paymentRequest);

        try {
            var payment = paymentMapper.toPayment(paymentRequest);
            paymentRepository.save(payment);

            sendStatus(paymentRequest, "SUCCESS");
            log.info("Payment processed successfully for Order ID: {}", paymentRequest.orderId());

        } catch (Exception e) {
            log.error("Error processing payment for Order ID: {}: {}", paymentRequest.orderId(), e.getMessage());
            sendStatus(paymentRequest, "FAILED");
            throw new PaymentProcessingException("Failed to process payment");
        }
    }

    private void sendStatus(PaymentRequest request, String status) {
        statusProducer.sendPaymentStatus(
                new PaymentStatus(
                        request.orderId(),
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        status,
                        request.customer()
                )
        );
        log.info("Payment status event sent: {}", status);
    }
}

