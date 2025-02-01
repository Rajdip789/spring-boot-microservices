package com.rajdip14.ecommerce.kafka;

import com.rajdip14.ecommerce.email.EmailService;
import com.rajdip14.ecommerce.kafka.order.OrderStatus;
import com.rajdip14.ecommerce.kafka.payment.PaymentStatus;
import com.rajdip14.ecommerce.notification.Notification;
import com.rajdip14.ecommerce.notification.NotificationRepository;
import com.rajdip14.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment.status", groupId = "notification-service-group")
    public void consumerPaymentStatus(PaymentStatus paymentStatus) throws MessagingException {
        log.info(format("Consuming the message from payment.status Topic:: %s", paymentStatus));

        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentStatus(paymentStatus)
                        .build()
        );

        // send email
        var customerName = paymentStatus.customer().firstname() + " " + paymentStatus.customer().lastname();
        emailService.sendPaymentUpdateEmail(
                paymentStatus.customer().email(),
                customerName,
                paymentStatus.amount(),
                paymentStatus.orderReference(),
                paymentStatus.status()
        );
    }

    @KafkaListener(topics = "order.status", groupId = "notification-service-group")
    public void consumeOrderStatus(OrderStatus orderStatus) throws MessagingException {
        log.info(format("Consuming the message from order.status Topic:: %s", orderStatus));

        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .orderStatus(orderStatus)
                        .build()
        );

        // send email
        var customerName = orderStatus.customer().firstname() + " " + orderStatus.customer().lastname();
        emailService.sendOrderUpdateEmail(
                orderStatus.customer().email(),
                customerName,
                orderStatus.totalAmount(),
                orderStatus.orderReference(),
                String.valueOf(orderStatus.status())
        );
    }
}
