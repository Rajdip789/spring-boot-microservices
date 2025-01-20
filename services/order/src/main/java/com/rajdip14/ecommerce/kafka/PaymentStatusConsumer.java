package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.exception.BusinessException;
import com.rajdip14.ecommerce.order.Order;
import com.rajdip14.ecommerce.order.OrderRepository;
import com.rajdip14.ecommerce.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusConsumer {

    private final OrderProducer orderProducer;
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment.status")
    public void consumePaymentStatus(PaymentStatus paymentStatus) {

        log.info(String.format("Consuming the message from payment.status Topic:: %s", paymentStatus.status()));

         Order order = orderRepository
                 .findById(paymentStatus.orderId())
                 .orElseThrow(() -> new BusinessException(
                         format("Their is some issue, order not found with ID:: %s", paymentStatus.orderId())
                 ));

         if(paymentStatus.status().equals("SUCCESS")) {

             order.setStatus(OrderStatus.CONFIRMED);

             orderProducer.sendOrderConfirmation(
                     new OrderConfirmation(
                             order.getReference(),
                             order.getTotalAmount(),
                             order.getPaymentMethod(),
                             order.getOrderLines()
                     )
             );

         } else if(paymentStatus.status().equals("FAILED")) {
             order.setStatus(OrderStatus.CANCELED);

             //rollback product which have deducted - product-service
         }

         orderRepository.save(order);
    }
}
