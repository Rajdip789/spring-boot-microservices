package com.rajdip14.ecommerce.notification;

import com.rajdip14.ecommerce.kafka.order.OrderStatus;
import com.rajdip14.ecommerce.kafka.payment.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
}
