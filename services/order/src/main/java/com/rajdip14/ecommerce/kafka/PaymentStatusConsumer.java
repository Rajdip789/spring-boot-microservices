package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.exception.BusinessException;
import com.rajdip14.ecommerce.order.Order;
import com.rajdip14.ecommerce.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.rajdip14.ecommerce.order.OrderStatusType;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusConsumer {

    private final OrderStatusProducer orderStatusProducer;
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment.status")
    public void consumePaymentStatus(PaymentStatus paymentStatus) {

        log.info(String.format("Consuming the message from payment.status Topic:: %s", paymentStatus.status()));

         Order order = orderRepository
                 .findById(paymentStatus.orderId())
                 .orElseThrow(() -> new BusinessException(
                         format("Their is some issue, order not found with ID:: %s", paymentStatus.orderId())
                 ));

        OrderStatusType newStatus = paymentStatus.status().equals("SUCCESS") ? OrderStatusType.CONFIRMED : OrderStatusType.FAILED;
        order.setStatus(newStatus);

        log.info("Payment status processed: {}", newStatus);

        orderStatusProducer.sendStatus(
                new OrderStatus(
                        order.getId(),
                        order.getReference(),
                        order.getTotalAmount(),
                        order.getPaymentMethod(),
                        newStatus
                )
        );

        // Note: The rollback for failed payment should be implemented elsewhere,
        // perhaps in a separate method or service to keep this method clean.

         orderRepository.save(order);
    }
}
